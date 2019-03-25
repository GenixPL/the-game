package com.pwse.gamemaster.controllers;

import com.pwse.gamemaster.controllers.helpers.BoardChecker;
import com.pwse.gamemaster.models.GameData;
import com.pwse.gamemaster.models.exceptions.CordsOutsideBoardException;
import com.pwse.gamemaster.models.exceptions.EnemyAreaException;
import com.pwse.gamemaster.models.exceptions.PlayerPositionException;
import com.pwse.gamemaster.models.exceptions.TwoPiecesPickedException;
import com.pwse.gamemaster.models.goal.Goal;
import com.pwse.gamemaster.models.board.BoardDimensions;
import com.pwse.gamemaster.models.piece.Piece;
import com.pwse.gamemaster.models.player.Player;
import com.pwse.gamemaster.models.team.Team;
import com.pwse.gamemaster.models.team.TeamColor;
import com.pwse.gamemaster.view.BoardPrinter;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * This class is responsible for what happens on the board
 */
public class BoardController {

	private static final int MAX_NUM_OF_PIECES = 6;

	private BoardDimensions bDim;
	private double shamProbability;
	private int pieceSpawnFrequency;
	private int numOfPlayers;

	/**
	 * Player id equals player's position in array
	 */
	private ArrayList<Player> players;
	private ArrayList<Piece> pieces;
	private ArrayList<Goal> goals;
	private Team blueTeam;
	private Team redTeam;
	private boolean isGameEnded;



	public BoardController(GameData gameData) {
		this.bDim = gameData.getBoardDimensions();
		this.shamProbability = gameData.getShamProbability();
		this.pieceSpawnFrequency = gameData.getPieceSpawnFrequency();
		this.numOfPlayers = gameData.getNumOfPlayers();

		this.players = new ArrayList<>(numOfPlayers);
		this.pieces = new ArrayList<>(gameData.getInitNumOfPieces());
		this.goals = new ArrayList<>(gameData.getGoals());
		this.blueTeam = new Team(TeamColor.blue);
		this.redTeam = new Team(TeamColor.red);
		this.isGameEnded = false;

		addPlayers();
		addInitPieces(gameData.getInitNumOfPieces());

		initPieceSpawning();
	}

	public boolean isGameEnded() {
		return isGameEnded;
	}

	public void printBoard() {
		BoardPrinter.print(bDim, players, pieces, goals);
	}

	public void movePlayerTo(int playerId, int posX, int posY) {
		try {
			BoardChecker.canPlayerMoveTo(posX, posY, players.get(playerId), bDim, players);

		} catch (CordsOutsideBoardException e) {
			//TODO: send message
			return;

		} catch (EnemyAreaException e) {
			//TODO: send message
			return;

		} catch (PlayerPositionException e) {
			//TODO: send message
			return;
		}

		//move piece if he has such
		if (players.get(playerId).hasPiece()) {
			for (Piece p : pieces) {
				if (p.getId() == players.get(playerId).getPieceId()) {
					p.moveTo(posX, posY);
				}
			}
		}
		players.get(playerId).moveTo(posX, posY);
	}

	public void pickUpPiece(int playerId) {
		int posX = players.get(playerId).getPosX();
		int posY = players.get(playerId).getPosY();

		int pieceId = -1;
		try {
			pieceId = BoardChecker.isPieceAtPosition(posX, posY, pieces, bDim);
		} catch (CordsOutsideBoardException e) {
			//TODO: message
			return;
		}

		if (pieceId > 0) {
			//there is a piece to pick up
			try {
				players.get(playerId).pickPiece(pieceId);
			} catch (TwoPiecesPickedException e) {
				//TODO: message
				return;
			}

		} else {
			//TODO: message
			return;
		}
	}

	public void dropPiece(int playerId) {
		//TODO
		//TODO: can't drop piece if other is at the same position
		//simple implementation must be changed
		for (Goal g : goals) {
			if (g.getPosX() == players.get(playerId).getPosX() && g.getPosY() == players.get(playerId).getPosY() && players.get(playerId).hasPiece()) {
				if (players.get(playerId).getTeamColor().equals(TeamColor.red)) {
					redTeam.addPoint();
				} else {
					blueTeam.addPoint();
				}
			}
		}

		checkScores();
	}

	public JSONObject getInfoOfPlayerWithId(int id) {
		JSONObject json = null;

		for (Player pl : players) {
			if (pl.getId() == id) {
				JSONObject positionJson = new JSONObject();
				positionJson.put("x", pl.getPosX());
				positionJson.put("y", pl.getPosY());

				json = new JSONObject();
				json.put("id", id);
				json.put("team", pl.getTeamColor());
				json.put("position", positionJson);
			}
		}

		return json;
	}



	private void addPlayers() { //TODO: this will be change due to the algo in which players are distributed
		for (int i = 0; i < numOfPlayers; i++) {
			if (i % 2 == 0) {
				Player newPlayer = new Player(i, TeamColor.blue, blueTeam.getNumOfPlayers(), bDim.getHeight() - 1);
				players.add(newPlayer);
				blueTeam.addPlayer(newPlayer);

			} else {
				Player newPlayer = new Player(i, TeamColor.red, redTeam.getNumOfPlayers(), 0);
				players.add(newPlayer);
				redTeam.addPlayer(newPlayer);
			}
		}
	}

	private void addInitPieces(int numOfInitPieces) {
		for (int i = 0; i < numOfInitPieces; i++) {
			addRandomPiece();
		}
	}

	private void initPieceSpawning() {
		TimerTask spawnPiece = new TimerTask() {
			@Override
			public void run() {
				addRandomPiece();
			}
		};

		new Timer().scheduleAtFixedRate(spawnPiece, pieceSpawnFrequency, pieceSpawnFrequency);
	}

	private void addRandomPiece() {
		if (pieces.size() >= MAX_NUM_OF_PIECES) {
			return;
		}

		Piece newPiece;
		do {
			newPiece = Piece.getRandomInstance(bDim, shamProbability);
		} while (!BoardChecker.isPlaceAvailableForPiece(newPiece.getPosX(), newPiece.getPosY(), pieces));

		pieces.add(newPiece);
	}

	private void checkScores() {
		if (redTeam.getScore() == 1) {
			System.out.println("=== RED TEAM WON ===");
			isGameEnded = true;
		}

		if (blueTeam.getScore() == 1) {
			System.out.println("=== BLUE TEAM WON ===");
			isGameEnded = true;
		}
	}
}

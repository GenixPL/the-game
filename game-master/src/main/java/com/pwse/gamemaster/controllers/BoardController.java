package com.pwse.gamemaster.controllers;

import com.pwse.gamemaster.models.board.Board;
import com.pwse.gamemaster.models.board.BoardDimensions;
import com.pwse.gamemaster.models.board.BoardField;
import com.pwse.gamemaster.models.piece.Piece;
import com.pwse.gamemaster.models.player.Player;
import com.pwse.gamemaster.models.player.Team;
import com.pwse.gamemaster.models.player.TeamColor;

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

	private Board b;
	private ArrayList<Piece> pieces;
	/**
	 * Player id equals player's position in array
	 */
	private ArrayList<Player> players;
	private Team blueTeam;
	private Team redTeam;



	public BoardController(
			BoardDimensions boardDimensions,
			BoardField[] goals,
			int numOfInitialPieces,
			double shamProbability,
			int pieceSpawnFrequency,
			int numOfPlayers
	) {
		this.bDim = boardDimensions;
		this.shamProbability = shamProbability;
		this.pieceSpawnFrequency = pieceSpawnFrequency;
		this.numOfPlayers = numOfPlayers;

		this.pieces = new ArrayList<>(numOfInitialPieces);
		this.players = new ArrayList<>(numOfPlayers);
		this.b = new Board(boardDimensions);
		this.blueTeam = new Team(TeamColor.blue);
		this.redTeam = new Team(TeamColor.red);

		addGoals(goals);
		addInitPieces(numOfInitialPieces);
		addPlayers();
		initPieceSpawning();
	}

	public void printBoard() {
		b.print(players);
	}

	public boolean movePlayerTo(int playerId, int posX, int posY) {
		//TODO: instead of boolean throw different exceptions representing situation
		//check if x and y are in bounds of board
		if (!isEveryCordCorrect(posX, posY)) {
			return false;
		}

		//check if he doesn't go to enemy's goal area
		String color = players.get(playerId).getTeamColor();
		if (color.equals(TeamColor.red)) {
			if (posY >= (bDim.getHeight() - bDim.getHeightOfTeamArea() - 1)) {
				return false;
			}
		} else {
			if (posY <= (bDim.getHeightOfTeamArea() - 1)) {
				return false;
			}
		}

		//TODO: check if he doesn't collide with others

		//TODO: move piece with player if he has one
		players.get(playerId).moveTo(posX, posY);

		return  true;
	}

	private boolean isEveryCordCorrect(int posX, int posY) {
		if (posX < 0 || posX >= bDim.getWidth()) {
			return false;
		}

		if (posY < 0 || posY >= bDim.getHeight()) {
			return false;
		}

		return true;
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

	private void addGoals(BoardField[] goals) {
		for (BoardField bf : goals) {
			b.addGoal(bf.getPosX(), bf.getPosY());
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
		} while (!b.isPlaceAvailableForPiece(newPiece.getPosX(), newPiece.getPosY()));

		pieces.add(newPiece);
		b.addPiece(newPiece);
	}
}

package com.pwse.gamemaster.controllers;

import com.pwse.gamemaster.controllers.helpers.BoardChecker;
import com.pwse.gamemaster.models.GameData;
import com.pwse.gamemaster.models.exceptions.*;
import com.pwse.gamemaster.models.goal.Goal;
import com.pwse.gamemaster.models.board.BoardDimensions;
import com.pwse.gamemaster.models.piece.Piece;
import com.pwse.gamemaster.models.player.Player;
import com.pwse.gamemaster.models.team.Team;
import com.pwse.gamemaster.models.team.TeamColor;
import com.pwse.gamemaster.view.BoardPrinter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * This class is responsible for what happens on the board
 */
public class BoardController {

	private final String TAG = this.getClass().getSimpleName() + ": ";
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

	public boolean canPlayerWithIdMoveTo(int playerId, int posX, int posY) {
		try {
			BoardChecker.canPlayerMoveTo(posX, posY, players.get(playerId), bDim, players);
			return true;

		} catch (CordsOutsideBoardException e) {
			return false;

		} catch (EnemyAreaException e) {
			return false;

		} catch (PlayerPositionException e) {
			return false;
		}
	}

	public void movePlayerTo(int playerId, int posX, int posY) {
		try {
			BoardChecker.canPlayerMoveTo(posX, posY, players.get(playerId), bDim, players);

		} catch (Exception e) {
			//ignore it, because it should be checked before
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

	public boolean canPlayerPickUpPiece(int playerId) {
		int posX = players.get(playerId).getPosX();
		int posY = players.get(playerId).getPosY();

		try {
			if (BoardChecker.isPieceAtPosition(posX, posY, pieces, bDim) == -1) {
				return false;
			}
		} catch (CordsOutsideBoardException e) {
			return false;
		}

		if (players.get(playerId).hasPiece()) {
			return false;
		}

		return true;
	}

	public void pickUpPieceByPlayerWithId(int playerId) {
		int posX = players.get(playerId).getPosX();
		int posY = players.get(playerId).getPosY();
		int pieceId = -1;

		try {
			pieceId = BoardChecker.isPieceAtPosition(posX, posY, pieces, bDim);
		} catch (CordsOutsideBoardException e) {
			//ignore it, because it should be checked before
		}

		try {
			players.get(playerId).pickPiece(pieceId);
		} catch (TwoPiecesPickedException e) {
			//ignore it, because it should be checked before
		}
	}

	public boolean canPlayerWithIdDropPiece(int playerId) {
		int posX = players.get(playerId).getPosX();
		int posY = players.get(playerId).getPosY();

		if (!players.get(playerId).hasPiece()) {
			return false;
		}

		try {
			if (BoardChecker.isPieceAtPosition(posX, posY, pieces, bDim) != -1) {
				return false;
			}
		} catch (CordsOutsideBoardException e) {
			return false;
		}

		return true;
	}

	public void dropPieceByPlayerWithId(int playerId) {
		int pieceId = players.get(playerId).getPieceId();

		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).getId() == pieceId) {
				pieces.remove(i);
			}
		}

		try {
			players.get(playerId).dropPiece();
		} catch (NoPieceToDropException e) {
			//ignore it, it should be check earlier
		}

		for (Goal g : goals) {
			if (g.getPosX() == players.get(playerId).getPosX() && g.getPosY() == players.get(playerId).getPosY()) {
				if (players.get(playerId).getTeamColor().equals(TeamColor.red)) {
					redTeam.addPoint();
				} else {
					blueTeam.addPoint();
				}
			}
		}

		checkScores();
	}

	public JSONObject getInfoAboutPlayerWithId(int playerId) {
		JSONObject json = null;

		for (Player pl : players) {
			if (pl.getId() == playerId) {
				JSONObject positionJson = new JSONObject();
				positionJson.put("x", pl.getPosX());
				positionJson.put("y", pl.getPosY());

				json = new JSONObject();
				json.put("id", playerId);
				json.put("team", pl.getTeamColor());
				json.put("position", positionJson);
			}
		}

		return json;
	}

	public JSONArray getDiscoveryInfoForPlayerWithId(int playerId) {
		JSONArray array = new JSONArray();
		int x = players.get(playerId).getPosX();
		int y = players.get(playerId).getPosY();

		array.put(getFieldInfo(x - 1, y - 1));
		array.put(getFieldInfo(x, y - 1));
		array.put(getFieldInfo(x + 1, y - 1));

		array.put(getFieldInfo(x - 1, y));
		array.put(getFieldInfo(x, y));
		array.put(getFieldInfo(x + 1, y));

		array.put(getFieldInfo(x - 1, y + 1));
		array.put(getFieldInfo(x, y + 1));
		array.put(getFieldInfo(x + 1, y + 1));

		return array;
	}

	public JSONObject getFieldInfo(int x, int y) {
		JSONObject toReturn = new JSONObject();
		toReturn.put("x", x);
		toReturn.put("y", y);

		JSONArray innerArray = new JSONArray();

		if (isCordOnBoard(x, y)) {
			boolean isEmpty = true;

			for (Piece p : pieces) {
				if (p.getPosX() == x && p.getPosY() == y) {
					innerArray.put("piece");
					isEmpty = false;
				}
			}

			for (Player pl : players) {
				if (pl.getPosX() == x && pl.getPosY() == y) {
					innerArray.put("player");
					isEmpty = false;
				}
			}

			for (Goal g : goals) {
				if (g.getPosX() == x && g.getPosY() == y) {
					innerArray.put("goal");
					isEmpty = false;
				}
			}

			if (y < (bDim.getHeightOfTeamArea() - 1)) { //TODO: it may be wrong
				innerArray.put("red-area");
			}

			if (y < (bDim.getHeight() - bDim.getHeightOfTeamArea() - 1)) { //TODO: it may be wrong
				innerArray.put("blue-area");
			}

			if (isEmpty) {
				innerArray.put("non");
			}

		} else {
			innerArray.put("outside");
		}

		toReturn.put("field", innerArray);

		return toReturn;
	}

	/**
	 * Gives information about piece hold by player with given id.
	 *
	 * @param playerId
	 * @return 0 - sham
	 *         1 - proper piece
	 *         -1 - can't check
	 */
	public int testPieceByPlayerWithId(int playerId) {
		for (Player pl : players) {
			if (pl.getId() == playerId) {
				if (pl.hasPiece()) {
					return getPieceInfo(pl.getPieceId());

				} else {
					return -1;
				}
			}
		}

		return 0;
	}

	public boolean canPlayerWithIdDestroyPiece(int playerId) {
		for (Player pl : players) {
			if (pl.getId() == playerId) {
				return pl.hasPiece();
			}
		}

		return false;
	}

	public void destroyPieceByPlayerWithId(int playerId) {
		int pcId;

		for (Player pl : players) {
			if (pl.getId() == playerId) {
				pcId = pl.getPieceId();
				destroyPiece(pcId);

				try {
					pl.dropPiece();
				} catch (NoPieceToDropException e) {
					System.err.println(TAG + e.getMessage());
				}
			}
		}
	}



	/**
	 * Returns piece's sham status
	 * @param id
	 * @return 0 - sham
	 * 	       1 - proper piece
	 * 	       -1 - can't check
	 */
	private int getPieceInfo(int id) {
		for (Piece pc : pieces) {
			if (pc.getId() == id) {
				return pc.isSham() ? 0 : 1;
			}
		}

		return -1;
	}

	private void destroyPiece(int id) {
		int index = -1;

		for (int i = 0; i < pieces.size(); i++) {
			if (id == pieces.get(i).getId()) {
				index = i;
			}
		}

		if (index != -1) {
			pieces.remove(index);
		} else {
			System.err.println(TAG + "cannot remove piece with id: " + id + " because it doesn't exist.");
		}
	}

	private void addPlayers() { //TODO: this will be change due to the algo in which players are distributed or not
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

	private boolean isCordOnBoard(int x, int y) {
		return (x >= 0 && x < bDim.getWidth()) && (y >= 0 && y < bDim.getHeight());
	}

	public int getManhattanDistanceToNearestPiece(int plX, int plY) {
		int smallest = 0;

		for (Piece pc : pieces) {
			int dist = Math.abs(plX - pc.getPosX()) + Math.abs(plY - pc.getPosY());

			if (dist < smallest) {
				smallest = dist;
			}
		}

		return smallest;
	}
}

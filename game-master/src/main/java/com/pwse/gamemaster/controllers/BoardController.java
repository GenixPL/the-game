package com.pwse.gamemaster.controllers;

import com.pwse.gamemaster.models.board.Board;
import com.pwse.gamemaster.models.board.BoardDimensions;
import com.pwse.gamemaster.models.board.BoardField;
import com.pwse.gamemaster.models.piece.Piece;
import com.pwse.gamemaster.models.player.Player;
import com.pwse.gamemaster.models.player.Team;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BoardController {

	private static final int MAX_NUM_OF_PIECES = 6;

	private BoardDimensions bDim;
	private double shamProbability;
	private int pieceSpawnFrequency;
	private int numOfPlayers;

	private Board b;
	private ArrayList<Piece> pieces;
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
		this.blueTeam = new Team(Team.blue);
		this.redTeam = new Team(Team.red);

		addGoals(goals);
		addInitPieces(numOfInitialPieces);
		addPlayers();
		initPieceSpawning();
	}

	public void printBoard() {
		b.print(players);
	}


	private void addPlayers() {
		for (int i = 0; i < numOfPlayers; i++) {
			if (i % 2 == 0) {
				Player newPlayer = new Player(Team.blue, i, i); //TODO: change position
				players.add(newPlayer);
				blueTeam.addPlayer(newPlayer);

			} else {
				Player newPlayer = new Player(Team.red, i, i); //TODO: change position
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

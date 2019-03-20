package com.pwse.gamemaster.controllers;

import com.pwse.gamemaster.models.board.Board;
import com.pwse.gamemaster.models.board.BoardDimensions;
import com.pwse.gamemaster.models.board.BoardField;
import com.pwse.gamemaster.models.piece.Piece;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BoardController {

	private static final int MAX_NUM_OF_PIECES = 6;

	private BoardDimensions bDim;
	private double shamProbability;
	private int pieceSpawnFrequency;

	private Board b;
	private ArrayList<Piece> pieces;
//	Number of players in each of the team



	public BoardController(
			BoardDimensions boardDimensions,
			BoardField[] goals,
			int numOfInitialPieces,
			double shamProbability,
			int pieceSpawnFrequency
	) {
		this.bDim = boardDimensions;
		this.shamProbability = shamProbability;
		this.pieceSpawnFrequency = pieceSpawnFrequency;

		this.pieces = new ArrayList<>(numOfInitialPieces);
		this.b = new Board(boardDimensions);

		addGoals(goals);
		addInitPieces(numOfInitialPieces);
		initPieceSpawning();
	}

	public void printBoard() {
		b.print();
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

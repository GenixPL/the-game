package com.pwse.gamemaster.controllers;

import com.pwse.gamemaster.models.board.Board;
import com.pwse.gamemaster.models.board.BoardDimensions;
import com.pwse.gamemaster.models.piece.Piece;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BoardController {

	private BoardDimensions bDim;
	private Board b;
	private ArrayList<Piece> pieces;
//	Probability of piece being a sham
//	Frequency of placing new pieces on board
//	Initial number of pieces on board
//	Number of players in each of the team
//	goal field's coordinates



	public BoardController(BoardDimensions boardDimensions) {
		this.bDim = boardDimensions;

		this.pieces = new ArrayList<>(0); //TODO: 0 should be changed to passed init value of pieces
		this.b = new Board(boardDimensions);

		initPieceSpawning();
	}

	public void printBoard() {
		b.print();
	}



	private void initPieceSpawning() {
		TimerTask spawnPiece = new TimerTask() {
			@Override
			public void run() {
				addRandomPiece();
			}
		};

		new Timer().scheduleAtFixedRate(spawnPiece, 0, 10000); //TODO: period should be changed to passed spawn frequency
	}

	private void addRandomPiece() {
		//TODO: it should check if there already isn't any piece at given cords (low priority with small spawning frequency)
		Piece newPiece = Piece.getRandomInstance(bDim);
		b.addPiece(newPiece);
	}
}

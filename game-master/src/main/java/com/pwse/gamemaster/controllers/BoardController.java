package com.pwse.gamemaster.controllers;

import com.pwse.gamemaster.models.board.Board;
import com.pwse.gamemaster.models.board.BoardDimensions;
import com.pwse.gamemaster.models.board.BoardField;
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



	public BoardController(BoardDimensions boardDimensions, BoardField[] goals) {
		this.bDim = boardDimensions;

		this.pieces = new ArrayList<>(0); //TODO: 0 should be changed to passed init value of pieces
		this.b = new Board(boardDimensions);

		addGoals(goals);
		initPieceSpawning();
	}

	public void printBoard() {
		b.print();
	}



	private void addGoals(BoardField[] goals) {
		for (BoardField bf : goals) {
			b.addGoal(bf);
		}
	}

	private void initPieceSpawning() {
		TimerTask spawnPiece = new TimerTask() {
			@Override
			public void run() {
				addRandomPiece();
			}
		};

		int time = 10000; //TODO: time should be changed to passed spawn frequency
		new Timer().scheduleAtFixedRate(spawnPiece, time, time);
	}

	private void addRandomPiece() {
		//TODO: it should check if there already isn't any piece at given cords (low priority with small spawning frequency)
		Piece newPiece = Piece.getRandomInstance(bDim);
		pieces.add(newPiece);
		b.addPiece(newPiece);
	}
}

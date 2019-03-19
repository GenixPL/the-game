package com.pwse.gamemaster.controllers;

import com.pwse.gamemaster.models.Board;
import com.pwse.gamemaster.models.BoardDimensions;

public class BoardController {

	private BoardDimensions bDim;
	private Board b;
//	Probability of piece being a sham
//	Frequency of placing new pieces on board
//	Initial number of pieces on board
//	Number of players in each of the team
//	goal field's coordinates



	public BoardController(BoardDimensions boardDimensions) {
		this.bDim = boardDimensions;

		this.b = new Board(boardDimensions);

	}

	public void printBoard() {
		b.print();
	}

}

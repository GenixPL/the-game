package com.pwse.gamemaster.models.board;

import com.pwse.gamemaster.models.piece.Piece;

public class Board {
	private BoardDimensions dim;
	private BoardField[][] fields;



	public Board(BoardDimensions boardDimensions) {
		this.dim = boardDimensions;

		createFields();
	}

	public void print() {
		//make space for readability
		System.out.println();
		System.out.println();

		//print upper line
		printHorizontalLine();

		for (int y = 0; y < dim.getHeight(); y++) {

			//print team areas separators
			if (y == (dim.getHeightOfTeamArea()) || y == (dim.getHeight() - dim.getHeightOfTeamArea())) {
				printHorizontalLine();
			}

			//print board itself
			System.out.print("\t|");
			for (int x = 0; x < dim.getWidth(); x++) {
				fields[x][y].print();
			}
			System.out.print("|\n");
		}

		//print bottom line
		printHorizontalLine();
	}

	/* PIECE */
	public void addPiece(Piece piece) {
		fields[piece.getPosX()][piece.getPosY()].setAsPiece();
	}

	/**
	 * It move piece's board position using current and previous positions from Piece
	 * @param piece Piece to move
	 * @throws Exception Throws exception if piece wasn't moved (its previous positions equal -1)
	 */
	public void movePiece(Piece piece) throws Exception {
		if (piece.getPrevPosX() < 0 || piece.getPrevPosY() < 0) {
			System.err.println("Failed during piece moving, given piece couldn't have been in given place");
			throw new Exception("Moving piece without previous positions");
		}

		fields[piece.getPrevPosX()][piece.getPrevPosY()].setAsBackground();
		fields[piece.getPosX()][piece.getPosY()].setAsPiece();
	}

	public boolean isPlaceAvailableForPiece(int posX, int posY) {
		return fields[posX][posY].isAvailableForPiece();
	}

	public void addGoal(int posX, int posY) {
		fields[posX][posY].setAsGoal();
	}

	public void removeGoal(int posX, int posY) {
		fields[posX][posY].setAsBackground();
	}



	private void printHorizontalLine() {
		System.out.print("\t|");
		for (int x = 0; x < dim.getWidth(); x++) {
			System.out.print("-");
		}
		System.out.print("|\n");
	}

	private void createFields() {
		fields = new BoardField[dim.getWidth()][dim.getHeight()];

		for (int x = 0; x < dim.getWidth(); x++) {
			for (int y = 0; y < dim.getHeight(); y++) {
				fields[x][y] = new BoardField(x, y, false);
				fields[x][y].setAsBackground();
			}
		}
	}
}

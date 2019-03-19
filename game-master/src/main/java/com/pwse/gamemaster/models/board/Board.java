package com.pwse.gamemaster.models.board;

import com.pwse.gamemaster.models.piece.Piece;

public class Board {
	private BoardDimensions dim;
	private BoardField[][] fields;



	public Board(BoardDimensions boardDimensions) {
		this.dim = boardDimensions;

		createFields();
		initFields();
	}

	public void print() {
		//make space for readability
		System.out.println();
		System.out.println();

		//print upper line
		printFrame();

		//print board itself
		for (int y = 0; y < dim.getHeight(); y++) {
			System.out.print("\t|");
			for (int x = 0; x < dim.getWidth(); x++) {
				fields[x][y].print();
			}
			System.out.print("|\n");
		}

		//print bottom line
		printFrame();
	}

	/* PIECE */
	public void addPiece(Piece piece) {
		setFieldAsPiece(piece.getPosX(), piece.getPosY());
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

		setFieldAsTask(piece.getPrevPosX(), piece.getPrevPosY());
		setFieldAsPiece(piece.getPosX(), piece.getPosY());
	}




	private void createFields() {
		fields = new BoardField[dim.getWidth()][dim.getHeight()];

		for (int x = 0; x < dim.getWidth(); x++) {
			for (int y = 0; y < dim.getHeight(); y++) {
				fields[x][y] = new BoardField();
			}
		}
	}

	/**
	 * t - tasks area
	 * g - goal area
	 */
	private void initFields() {
		for (int x = 0; x < dim.getWidth(); x++) {
			for (int y = 0; y < dim.getHeight(); y++) {
				if (y < dim.getHeightOfTeamArea()) {
					//mark red goal area
					fields[x][y].fieldChar = 'g'; //TODO: all of those should be moved to separate functions, not sure which class should be responsible
					fields[x][y].fieldColor = FieldColors.redTeamColor;

				} else if (y >= (dim.getHeight() - dim.getHeightOfTeamArea())) {
					//mark blue goal area
					fields[x][y].fieldChar = 'g';
					fields[x][y].fieldColor = FieldColors.blueTeamColor;

				} else {
					//mark tasks area
					fields[x][y].fieldChar = 't';
					fields[x][y].fieldColor = FieldColors.taskFieldColor;
				}
			}
		}
	}

	private void printFrame() {
		System.out.print("\t|");
		for (int x = 0; x < dim.getWidth(); x++) {
			System.out.print("-");
		}
		System.out.print("|\n");
	}

	/* SET FIELD */
	private void setFieldAsTask(int posX, int posY) {
		fields[posX][posY].fieldChar = 't';
		fields[posX][posY].fieldColor = FieldColors.taskFieldColor;
	}

	private void setFieldAsPiece(int posX, int posY) {
		fields[posX][posY].fieldChar = 'p';
		fields[posX][posY].fieldColor = FieldColors.pieceColor;
	}
}

package com.pwse.gamemaster.models.piece;

import com.pwse.gamemaster.models.Colors;
import com.pwse.gamemaster.models.board.BoardDimensions;

import java.util.Random;



public class Piece {

	/**
	 * It is used to create piece id, starting from 1 in order to miss default int.
	 */
	private static int piecesCount = 1;

	private int id = -1;
	private int posX;
	private int posY;
	private boolean isSham;



	public static Piece getPositionedInstance(int posX, int posY, double shamProbability) {
		Random random = new Random();
		boolean isSham = (random.nextFloat() < shamProbability);

		return new Piece(posX, posY, isSham);
	}

	public static Piece getRandomInstance(BoardDimensions dim, double shamProbability) {
		Random random = new Random();
		int posX = random.nextInt(dim.getWidth());
		int posY = random.nextInt(dim.getHeight() - 2 * dim.getHeightOfTeamArea()) + dim.getHeightOfTeamArea();
		boolean isSham = (random.nextFloat() < shamProbability);

		return new Piece(posX, posY, isSham);
	}



	private Piece(int posX, int posY, boolean isSham) {
		this.id = piecesCount++;
		this.posX = posX;
		this.posY = posY;
		this.isSham = isSham;
	}



	public void moveTo(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public boolean isSham() {
		return isSham;
	}

	public String getPrintSting() {
		return Colors.piece + 'p' + Colors.ANSI_RESET;
	}

	public int getId() {
		return id;
	}
}

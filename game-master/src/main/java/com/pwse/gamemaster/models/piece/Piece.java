package com.pwse.gamemaster.models.piece;

import com.pwse.gamemaster.models.board.BoardDimensions;

import java.util.Random;

public class Piece {
	private int posX;
	private int posY;
	private boolean isPicked;
	private boolean isSham;

	private int prevPosX = -1;
	private int prevPosY = -1;



	public static Piece getPositionedInstance(int posX, int posY) {
		Random random = new Random();
		boolean isSham = random.nextBoolean();

		return new Piece(posX, posY, isSham);
	}

	public static Piece getRandomInstance(BoardDimensions dim) {
		Random random = new Random();
		int posX = random.nextInt(dim.getWidth());
		int posY = random.nextInt(dim.getHeight() - 2 * dim.getHeightOfTeamArea()) + dim.getHeightOfTeamArea();
		boolean isSham = random.nextBoolean();

		return new Piece(posX, posY, isSham);
	}



	public void moveTo(int posX, int posY) {
		prevPosX = this.posX;
		prevPosY = this.posY;

		this.posX = posX;
		this.posY = posY;
	}

	public int getPrevPosX() {
		return prevPosX;
	}

	public int getPrevPosY() {
		return prevPosY;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public boolean isPicked() {
		return isPicked;
	}

	public boolean isSham() {
		return isSham;
	}

	public void setPicked(boolean isPicked) {
		this.isPicked = isPicked;
	}



	private Piece(int posX, int posY, boolean isSham) {
		this.posX = posX;
		this.posY = posY;
		this.isPicked = false;
		this.isSham = isSham;
	}

}

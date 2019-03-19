package com.pwse.gamemaster.models.board;

public class BoardField {
	private static final String ANSI_RESET  = "\u001B[0m";

	private boolean isGoal;
	private int posX;
	private int posY;

	public char fieldChar;
	public String fieldColor;



	public BoardField(int posX, int posY, boolean isGoal) {
		this.posX = posX;
		this.posY = posY;
		this.isGoal = isGoal;
	}

	public void print() {
		System.out.print(fieldColor + fieldChar + ANSI_RESET);
	}

	public boolean isGoal() {
		return isGoal;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}
}

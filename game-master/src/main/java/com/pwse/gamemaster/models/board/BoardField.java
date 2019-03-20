package com.pwse.gamemaster.models.board;

public class BoardField {
	private static final String ANSI_RESET  = "\u001B[0m";

	private boolean isGoal;
	private int posX;
	private int posY;

	private char fieldStatus;
	private char prevFieldStatus;
	public String fieldColor;



	public BoardField(int posX, int posY, boolean isGoal) {
		this.posX = posX;
		this.posY = posY;
		this.isGoal = isGoal;
	}

	public void print() {
		System.out.print(fieldColor + fieldStatus + ANSI_RESET);
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

	/* !!! should be used everywhere !!! */
	public char getFieldStatus() {
		return fieldStatus;
	}

	public void setFieldStatus(char fieldStatus) {
		this.prevFieldStatus = this.fieldStatus;
		this.fieldStatus = fieldStatus;
	}

	public char getPrevFieldStatus() {
		return prevFieldStatus;
	}

	public void setAsBackground() {
		setFieldStatus(FieldStatus.background);
		fieldColor = FieldColors.background;
	}

	public void setAsGoal() {
		setFieldStatus(FieldStatus.goal);
		fieldColor = FieldColors.goal;
	}

	public void setAsPiece() {
		setFieldStatus(FieldStatus.piece);
		fieldColor = FieldColors.piece;
	}

	public boolean isAvailableForPiece() {
		if (getFieldStatus() == FieldStatus.background) {
			return true;
		}

		if (getFieldStatus() == FieldStatus.goal) {
			return false;
		}

		if (getFieldStatus() == FieldStatus.piece) {
			return false;
		}

		return false; //shouldn't come so far
	}
}

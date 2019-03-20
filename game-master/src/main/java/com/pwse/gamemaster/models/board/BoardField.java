package com.pwse.gamemaster.models.board;

import com.pwse.gamemaster.models.Colors;

public class BoardField {

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
		System.out.print(fieldColor + fieldStatus + Colors.ANSI_RESET);
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
		fieldColor = Colors.background;
	}

	public void setAsGoal() {
		setFieldStatus(FieldStatus.goal);
		fieldColor = Colors.goal;
	}

	public void setAsPiece() {
		setFieldStatus(FieldStatus.piece);
		fieldColor = Colors.piece;
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

package com.pwse.gamemaster.models;

public class BoardDimensions {
	private int width;
	private int height;
	private int heightOfTeamArea;

	public BoardDimensions(int width, int height, int heightOfGoalArea) {
		this.width = width;
		this.height = height;
		this.heightOfTeamArea = heightOfGoalArea;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getHeightOfTeamArea() {
		return heightOfTeamArea;
	}
}

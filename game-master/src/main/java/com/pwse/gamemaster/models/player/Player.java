package com.pwse.gamemaster.models.player;

import com.pwse.gamemaster.models.Colors;


public class Player {

	private String teamColor;
	private int posX;
	private int posY;
	private int id;



	public Player(int id, String teamColor, int posX, int posY) {
		this.teamColor = teamColor;
		this.posX = posX;
		this.posY = posY;
	}

	public int getPosY() {
		return posY;
	}

	public int getPosX() {
		return posX;
	}

	public int getId() {
		return id;
	}

	public void print() {
		if (teamColor.equals(TeamColor.blue)) {
			System.out.print(Colors.blueTeam + "P" + Colors.ANSI_RESET);

		} else if (teamColor.equals(TeamColor.red)) {
			System.out.print(Colors.redTeam + "P" + Colors.ANSI_RESET);

		} else {
			System.out.println();
			System.err.println("WTF wrong team color");
			System.out.println();
		}
	}

	public String getTeamColor() {
		return teamColor;
	}

	public void moveTo(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}
}

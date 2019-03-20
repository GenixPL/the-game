package com.pwse.gamemaster.models.player;

import com.pwse.gamemaster.models.Colors;

public class Player {

	private String teamColor;
	private int posX;
	private int poxY;



	public Player(String teamColor, int posX, int posY) {
		this.teamColor = teamColor;
		this.posX = posX;
		this.poxY = posY;
	}

	public int getPoxY() {
		return poxY;
	}

	public int getPosX() {
		return posX;
	}

	public void print() {
		if (teamColor.equals(Team.blue)) {
			System.out.print(Colors.blueTeam + "P" + Colors.ANSI_RESET);

		} else if (teamColor.equals(Team.red)) {
			System.out.print(Colors.redTeam + "P" + Colors.ANSI_RESET);

		} else {
			System.out.println();
			System.err.println("WTF wrong team color");
			System.out.println();
		}
	}
}

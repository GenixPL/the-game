package com.pwse.gamemaster.models.player;

import com.pwse.gamemaster.models.Colors;
import com.pwse.gamemaster.models.exceptions.NoPieceToDropException;
import com.pwse.gamemaster.models.exceptions.TwoPiecesPickedException;


public class Player {

	private String teamColor;
	private int posX;
	private int posY;
	private int id;
	private boolean hasPiece;



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

	public void pickPiece() throws TwoPiecesPickedException {
		if (hasPiece) {
			throw new TwoPiecesPickedException();
		}

		hasPiece = true;
	}

	public void dropPiece() throws NoPieceToDropException {
		if (!hasPiece) {
			throw new NoPieceToDropException();
		}

		hasPiece = false;
	}
}

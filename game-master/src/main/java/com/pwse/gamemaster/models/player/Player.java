package com.pwse.gamemaster.models.player;

import com.pwse.gamemaster.models.Colors;
import com.pwse.gamemaster.models.exceptions.NoPieceToDropException;
import com.pwse.gamemaster.models.exceptions.TwoPiecesPickedException;
import com.pwse.gamemaster.models.team.TeamColor;


public class Player {

	private String teamColor;
	private int posX;
	private int posY;
	private int id;
	private boolean hasPiece;
	private int pieceId = -1;



	public Player(int id, String teamColor, int posX, int posY) {
		this.id = id;
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

	public String getPrintString() {
		if (teamColor.equals(TeamColor.blue)) {
			return Colors.blueTeam + 'P' + Colors.ANSI_RESET;

		} else if (teamColor.equals(TeamColor.red)) {
			return Colors.redTeam + 'P' + Colors.ANSI_RESET;

		} else {
			return Colors.error + ' ' + Colors.ANSI_RESET;
		}
	}

	public String getTeamColor() {
		return teamColor;
	}

	public void moveTo(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	public boolean hasPiece() {
		return hasPiece;
	}

	public void pickPiece(int pieceId) throws TwoPiecesPickedException {
		if (hasPiece) {
			throw new TwoPiecesPickedException();
		}

		this.pieceId = pieceId;
		hasPiece = true;
	}

	public void dropPiece() throws NoPieceToDropException {
		if (!hasPiece) {
			throw new NoPieceToDropException();
		}

		pieceId = -1;
		hasPiece = false;
	}

	public int getPieceId() {
		return pieceId;
	}
}

package com.pwse.player.controllers.helpers;

import com.pwse.player.models.Board.BoardInfo;
import com.pwse.player.models.BoardDimensions;
import com.pwse.player.models.Exceptions.WrongTeamNameException;
import com.pwse.player.models.Position;
import com.pwse.player.models.player.PlayerInfo;

public class InfoSingleton {

	private static InfoSingleton self;

	private BoardInfo boardInfo;
	private PlayerInfo playerInfo;



	public static void init(BoardDimensions boardDimensions, Position position, String teamColor) throws WrongTeamNameException {
		if (self != null) {
			return;
		}

		self = new InfoSingleton(boardDimensions, position, teamColor);
	}

	public static InfoSingleton getInstance() {
		return self;
	}



	private InfoSingleton(BoardDimensions boardDimensions, Position position, String teamColor) throws WrongTeamNameException {
		this.boardInfo = new BoardInfo(boardDimensions);

		try {
			this.playerInfo = new PlayerInfo(position, teamColor);
		} catch (WrongTeamNameException e) {
			throw e;
		}
	}



	public BoardInfo getBoardInfo() {
		return boardInfo;
	}

	public PlayerInfo getPlayerInfo() {
		return playerInfo;
	}
}

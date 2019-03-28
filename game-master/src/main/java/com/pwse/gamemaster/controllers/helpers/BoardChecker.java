package com.pwse.gamemaster.controllers.helpers;

import com.pwse.gamemaster.models.board.BoardDimensions;
import com.pwse.gamemaster.models.exceptions.CordsOutsideBoardException;
import com.pwse.gamemaster.models.exceptions.EnemyAreaException;
import com.pwse.gamemaster.models.exceptions.PlayerPositionException;
import com.pwse.gamemaster.models.piece.Piece;
import com.pwse.gamemaster.models.player.Player;
import com.pwse.gamemaster.models.team.TeamColor;

import java.util.ArrayList;



public class BoardChecker {

	private BoardChecker() { }



	public static boolean isPlaceAvailableForPiece(int posX, int posY, ArrayList<Piece> pieces) {
		for (Piece p : pieces) {
			if (p.getPosX() == posX && p.getPosY() == posY) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Function checks if there is a piece at given position and return its id or -1.
	 * @param posX
	 * @param posY
	 * @param pieces
	 * @param dim
	 * @return piece id or -1 if there is no piece
	 * @throws CordsOutsideBoardException
	 */
	public static int isPieceAtPosition(
			int posX,
			int posY,
			ArrayList<Piece> pieces,
			BoardDimensions dim
	) throws CordsOutsideBoardException {

		if (!isEveryCordCorrect(posX, posY, dim)) {
			throw new CordsOutsideBoardException();
		}

		for (Piece p : pieces) {
			if (p.getPosX() == posX && p.getPosY() == posY) {
				return p.getId();
			}
		}

		return -1;
	}

	public static boolean canPlayerMoveTo(
			int posX,
			int posY,
			Player player,
			BoardDimensions dim,
			ArrayList<Player> players
	) throws CordsOutsideBoardException, EnemyAreaException, PlayerPositionException {

		//check if x and y are in bounds of board
		if (!isEveryCordCorrect(posX, posY, dim)) {
			throw new CordsOutsideBoardException();
		}

		//check if he doesn't go to enemy's goal area
		String color = player.getTeamColor();
		if (color.equals(TeamColor.red)) {
			if (posY > (dim.getHeight() - dim.getHeightOfTeamArea() - 1)) {
				throw new EnemyAreaException();
			}
		} else {
			if (posY < dim.getHeightOfTeamArea()) {
				throw new EnemyAreaException();
			}
		}

		//check if he doesn't collide with other players
		for (Player p : players) {
			if (p.getId() != player.getId()) {
				if (p.getPosX() == posX && p.getPosY() == posY) {
					throw new PlayerPositionException();
				}
			}
		}

		if (posX < player.getPosX() - 1 || posX > player.getPosX() + 1 || posY < player.getPosY() - 1 || posY > player.getPosY() + 1) {
			return true;
		}

		return true;
	}

	private static boolean isEveryCordCorrect(int posX, int posY, BoardDimensions dim) {
		if (posX < 0 || posX >= dim.getWidth()) {
			return false;
		}

		if (posY < 0 || posY >= dim.getHeight()) {
			return false;
		}

		return true;
	}
}

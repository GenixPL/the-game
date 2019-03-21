package com.pwse.player.controllers;


import com.pwse.player.controllers.helpers.InfoSingleton;
import com.pwse.player.models.player.PlayerInfo;
import com.pwse.player.models.BoardDimensions;
import com.pwse.player.models.Exceptions.WrongMoveException;



public class MovementController {

	public void moveUp() throws WrongMoveException {
		int currentX = InfoSingleton.getInstance().getPlayerInfo().getPosX();
		int currentY = InfoSingleton.getInstance().getPlayerInfo().getPosY();

		if (!isMoveCorrect(currentX, currentY - 1)) {
			throw new WrongMoveException();
		}

		InfoSingleton.getInstance().getPlayerInfo().setPosY(currentY - 1);
	}

	public void moveDown() throws WrongMoveException {
		int currentX = InfoSingleton.getInstance().getPlayerInfo().getPosX();
		int currentY = InfoSingleton.getInstance().getPlayerInfo().getPosY();

		if (!isMoveCorrect(currentX, currentY + 1)) {
			throw new WrongMoveException();
		}

		InfoSingleton.getInstance().getPlayerInfo().setPosY(currentY + 1);
	}

	public void moveLeft() throws WrongMoveException {
		int currentX = InfoSingleton.getInstance().getPlayerInfo().getPosX();
		int currentY = InfoSingleton.getInstance().getPlayerInfo().getPosY();

		if (!isMoveCorrect(currentX - 1, currentY)) {
			throw new WrongMoveException();
		}

		InfoSingleton.getInstance().getPlayerInfo().setPosX(currentX - 1);
	}

	public void moveRight() throws WrongMoveException {
		int currentX = InfoSingleton.getInstance().getPlayerInfo().getPosX();
		int currentY = InfoSingleton.getInstance().getPlayerInfo().getPosY();

		if (!isMoveCorrect(currentX + 1, currentY)) {
			throw new WrongMoveException();
		}

		InfoSingleton.getInstance().getPlayerInfo().setPosX(currentX + 1);
	}



	private boolean isMoveCorrect(int posX, int posY) {
		int height = InfoSingleton.getInstance().getBoardInfo().getDimensions().getHeight();
		int width = InfoSingleton.getInstance().getBoardInfo().getDimensions().getWidth();
		int teamArea = InfoSingleton.getInstance().getBoardInfo().getDimensions().getHeightOfTeamArea();

		if (posX < 0 || posX >= width) {
			return false;
		}

		if (posY < 0 || posY >= height) {
			return false;
		}

		if (InfoSingleton.getInstance().getPlayerInfo().getTeam().equals("red")) {
			if (posY > (height - teamArea - 1)) {
				return false;
			}
		}

		if (InfoSingleton.getInstance().getPlayerInfo().getTeam().equals("blue")) {
			if (posY < (teamArea - 1)) {
				return false;
			}
		}

		return true;
	}

}

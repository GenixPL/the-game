package com.pwse.player.controllers;


import com.pwse.player.controllers.helpers.InfoSingleton;
import com.pwse.player.models.Position;
import com.pwse.player.models.Exceptions.WrongMoveException;


public class MovementController {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	/**
	 * Base goal position - (0,0) for red team and other end of board for blue team,
	 * player goes there before starting searching other goal fields.
	 */
	private boolean wasInBaseGoalPosition = false;

	/**
	 * Base piece position - (0, hota - 1) for red team and (0, h - hota - 1) for blue team,
	 * player goes there before starting searching other piece fields (area in which pieces are spawned).
	 */
	private boolean wasInBasePiecePosition = false;

	private boolean isMovingRight = false;



	public Position getMoveUpCords() throws WrongMoveException {
		int currentX = InfoSingleton.getInstance().getPlayerInfo().getPosX();
		int currentY = InfoSingleton.getInstance().getPlayerInfo().getPosY();

		if (!isMoveCorrect(currentX, currentY - 1)) {
			throw new WrongMoveException();
		}

		return new Position(currentX, currentY - 1);
	}

	public Position getMoveDownCords() throws WrongMoveException {
		int currentX = InfoSingleton.getInstance().getPlayerInfo().getPosX();
		int currentY = InfoSingleton.getInstance().getPlayerInfo().getPosY();

		if (!isMoveCorrect(currentX, currentY + 1)) {
			throw new WrongMoveException();
		}

		return new Position(currentX, currentY + 1);
	}

	public Position getMoveLeftCords() throws WrongMoveException {
		int currentX = InfoSingleton.getInstance().getPlayerInfo().getPosX();
		int currentY = InfoSingleton.getInstance().getPlayerInfo().getPosY();

		if (!isMoveCorrect(currentX - 1, currentY)) {
			throw new WrongMoveException();
		}

		return new Position(currentX - 1, currentY);
	}

	public Position getMoveRightCords() throws WrongMoveException {
		int currentX = InfoSingleton.getInstance().getPlayerInfo().getPosX();
		int currentY = InfoSingleton.getInstance().getPlayerInfo().getPosY();

		if (!isMoveCorrect(currentX + 1, currentY)) {
			throw new WrongMoveException();
		}

		return new Position(currentX + 1, currentY);
	}

	public void moveTo(Position pos) throws WrongMoveException {
		if (!isMoveCorrect(pos.getX(), pos.getY())) {
			throw new WrongMoveException();
		}

		InfoSingleton.getInstance().getPlayerInfo().setPosX(pos.getX());
		InfoSingleton.getInstance().getPlayerInfo().setPosY(pos.getY());
	}

	public Position getMoveToGoalCords() {
		wasInBasePiecePosition = false;
		String team = InfoSingleton.getInstance().getPlayerInfo().getTeam();
		int h = InfoSingleton.getInstance().getBoardInfo().getDimensions().getHeight();
		int hota = InfoSingleton.getInstance().getBoardInfo().getDimensions().getHeightOfTeamArea();
		int x = InfoSingleton.getInstance().getPlayerInfo().getPosX();
		int y = InfoSingleton.getInstance().getPlayerInfo().getPosY();

		if (isInGoalArea(y)) {
			//TODO: here might be an error if player is in base position by accident - those methods didn't get him there
			if (!wasInBaseGoalPosition) {
				Position nextMove = moveToBasePosition(x, y);

				if (team.equals("red")) {
					if (nextMove.getX() == 0 && nextMove.getY() == 0) {
						isMovingRight = true;
						wasInBaseGoalPosition = true;
					}

				} else {
					if (nextMove.getX() == 0 && nextMove.getY() == h - 1) {
						isMovingRight = true;
						wasInBaseGoalPosition = true;
					}
				}

				return nextMove;
			} else {
				return makeNextMoveInArea(x, y);
			}

		} else {
			return moveToGoalAreaInStraightLine(x, y);
		}
	}

	public Position getMoveToPieceCords() {
		wasInBaseGoalPosition = false;
		String team = InfoSingleton.getInstance().getPlayerInfo().getTeam();
		int h = InfoSingleton.getInstance().getBoardInfo().getDimensions().getHeight();
		int hota = InfoSingleton.getInstance().getBoardInfo().getDimensions().getHeightOfTeamArea();
		int x = InfoSingleton.getInstance().getPlayerInfo().getPosX();
		int y = InfoSingleton.getInstance().getPlayerInfo().getPosY();

		if (isInPieceArea(y)) {
			//TODO: here might be an error if player is in base position by accident - those methods didn't get him there
			if (!wasInBasePiecePosition) {
				Position nextMove = moveToBasePosition(x, y);

				if (team.equals("red")) {
					if (nextMove.getX() == 0 && nextMove.getY() == hota) {
						isMovingRight = true;
						wasInBasePiecePosition = true;
					}

				} else {
					if (nextMove.getX() == 0 && nextMove.getY() == h - hota - 1) {
						isMovingRight = true;
						wasInBasePiecePosition = true;
					}
				}

				return nextMove;
			} else {
				return makeNextMoveInArea(x, y);
			}

		} else {
			return moveToPieceAreaInStraightLine(x, y);
		}
	}

	public Position getRandomMove(int a) {
		if (a == 0) { //move up or down if cannot up
			try {
				return getMoveUpCords();
			} catch (WrongMoveException e) {
				try {
					return getMoveDownCords();
				} catch (WrongMoveException e1) {
					//impossible
				}
			}

		} else if (a == 1) { //move left or right if cannot left
			try {
				return getMoveLeftCords();
			} catch (WrongMoveException e) {
				try {
					return getMoveRightCords();
				} catch (WrongMoveException e1) {
					//impossible
				}
			}

		} else if (a == 2) { //move right or left if cannot right
			try {
				return getMoveRightCords();
			} catch (WrongMoveException e) {
				try {
					return getMoveLeftCords();
				} catch (WrongMoveException e1) {
					//impossible
				}
			}
		} else { //move down or up if cannot down
			try {
				return getMoveDownCords();
			} catch (WrongMoveException e) {
				try {
					return getMoveUpCords();
				} catch (WrongMoveException e1) {
					//impossible
				}
			}
		}

		System.err.println(TAG + "function getRandomMove went to far, CHECK IT!");
		return new Position(0, 0); //it should not come so far
	}



	private Position moveToPieceAreaInStraightLine(int posX, int posY) {
		String team = InfoSingleton.getInstance().getPlayerInfo().getTeam();

		if (team.equals("red")) {
			System.out.println(TAG + "\t\tmove down");
			return new Position(posX, posY + 1); //move down
		} else {
			System.out.println(TAG + "\t\tmove up");
			return new Position(posX, posY - 1); //move up
		}
	}

//	private Position moveToBasePiecePosition(int posX, int posY) {
//		String team = InfoSingleton.getInstance().getPlayerInfo().getTeam();
//
//		if (team.equals("red")) {
//			//moving to (0, hota)
//			if (posX != 0) {
//				System.out.println(TAG + "\t\tmove left");
//				return new Position(posX - 1, posY); //move left
//
//			} else {
//
//				System.out.println(TAG + "\t\tmove down");
//				return new Position(posX, posY + 1); //move down
//			}
//
//		} else { //blue team
//			//moving to (0, h - hota - 1)
//			if (posX != 0) {
//				System.out.println(TAG + "\t\tmove left");
//				return new Position(posX - 1, posY); //move left
//
//			} else {
//
//				System.out.println(TAG + "\t\tmove up");
//				return new Position(posX, posY - 1); //move up
//			}
//		}
//	}

	private boolean isInPieceArea(int y) {
		int h = InfoSingleton.getInstance().getBoardInfo().getDimensions().getHeight();
		int hota = InfoSingleton.getInstance().getBoardInfo().getDimensions().getHeightOfTeamArea();

		return (y >= hota && y <= h - hota -1);
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

	private boolean isInGoalArea(int y) {
		String team = InfoSingleton.getInstance().getPlayerInfo().getTeam();
		int h = InfoSingleton.getInstance().getBoardInfo().getDimensions().getHeight();
		int hota = InfoSingleton.getInstance().getBoardInfo().getDimensions().getHeightOfTeamArea();

		if (team.equals("red")) {
			return y < hota;
		} else {
			return y >= h - hota;
		}
	}

	private Position moveToGoalAreaInStraightLine(int posX, int posY) {
		String team = InfoSingleton.getInstance().getPlayerInfo().getTeam();

		if (team.equals("red")) {
			System.out.println(TAG + "\t\tmove up");
			return new Position(posX, posY - 1); //move up

		} else {
			System.out.println(TAG + "\t\tmove down");
			return new Position(posX, posY + 1); //move down
		}
	}

	private Position moveToBasePosition(int posX, int posY) {
		String team = InfoSingleton.getInstance().getPlayerInfo().getTeam();
		int h = InfoSingleton.getInstance().getBoardInfo().getDimensions().getHeight();

		if (team.equals("red")) {
			//moving to (0,0)
			if (posX != 0) {
				System.out.println(TAG + "\t\tmove left");
				return new Position(posX - 1, posY); //move left

			} else {

				System.out.println(TAG + "\t\tmove up");
				return new Position(posX, posY - 1); //move up
			}

		} else { //blue team
			//moving to (0, h-1)
			if (posX != 0) {
				System.out.println(TAG + "\t\tmove left");
				return new Position(posX - 1, posY); //move left

			} else {

				System.out.println(TAG + "\t\tmove down");
				return new Position(posX, posY + 1); //move down
			}
		}
	}

	/**
	 * red team should always go from top to bottom and left to right
	 * blue team should always go from bottom to top and left to right
	 */
	private Position makeNextMoveInArea(int posX, int posY) {
		String team = InfoSingleton.getInstance().getPlayerInfo().getTeam();
		int w = InfoSingleton.getInstance().getBoardInfo().getDimensions().getWidth();

		if (posX == 0 && isMovingRight) {
			//move right
			System.out.println(TAG + "\t\tmove right");
			return new Position(posX + 1, posY);
		}

		if (posX == 0 && !isMovingRight) {
			//move up/down and change to right
			isMovingRight = true;
			if (team.equals("red")) {
				System.out.println(TAG + "\t\tmove up");
				return new Position(posX, posY  + 1);
			} else {
				System.out.println(TAG + "\t\tmove down");
				return new Position(posX, posY - 1);
			}
		}

		if (posX == w - 1 && isMovingRight) {
			//move up/down and change to left
			isMovingRight = false;
			if (team.equals("red")) {
				System.out.println(TAG + "\t\tmove up");
				return new Position(posX, posY + 1);
			} else {
				System.out.println(TAG + "\t\tmove down");
				return new Position(posX, posY - 1);
			}
		}

		if (posX == w - 1 && !isMovingRight) {
			//move left
			System.out.println(TAG + "\t\tmove left");
			return new Position(posX - 1, posY);
		}

		if (isMovingRight) {
			//move right
			System.out.println(TAG + "\t\tmove right");
			return new Position(posX + 1, posY);

		} else {
			//move left
			System.out.println(TAG + "\t\tmove left");
			return new Position(posX - 1, posY);
		}
	}


}

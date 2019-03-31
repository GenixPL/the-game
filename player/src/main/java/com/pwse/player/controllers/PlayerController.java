package com.pwse.player.controllers;



import com.pwse.player.models.BoardDimensions;
import com.pwse.player.models.Exceptions.WrongMoveException;
import com.pwse.player.models.Position;

import java.util.Random;

public class PlayerController {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private MovementController mController;
	private boolean hasPiece;



	public PlayerController(BoardDimensions boardDimensions) {
		this.mController = new MovementController();
		this.hasPiece = false;
	}

	public void moveTo(Position pos) throws WrongMoveException {
		if (hasPiece) {
			System.out.println(TAG + "\t\tmoving with piece");
		} else {
			System.out.println(TAG + "\t\tmoving without piece");
		}
		mController.moveTo(pos);
	}

	public Position getMoveUpCords() throws WrongMoveException {
		return mController.getMoveUpCords();
	}

	public Position getMoveDownCords() throws WrongMoveException {
		return mController.getMoveDownCords();
	}

	public Position getMoveLeftCords() throws WrongMoveException {
		return mController.getMoveLeftCords();
	}

	public Position getMoveRightCords() throws WrongMoveException {
		return mController.getMoveRightCords();
	}

	public void pickUpPiece() {
		System.out.println(TAG + "\t\t picking up");
		hasPiece = true;
	}

	public void dropPiece() {
		System.out.println(TAG + "\t\t dropping");
		hasPiece = false;
	}

	public void testPiece() {
		//TODO
	}

	public void destroyPiece() {
		//TODO
		//InfoSingleton.getInstance().getBoardInfo().removePiece();
	}

	public Position getNextMove() {
		if (hasPiece) {
			return mController.getMoveToGoalCords();
		} else {
			return mController.getMoveToPieceCords();
		}
	}

	public Position getNextMovePossibleRandom() {
		Random random = new Random();
		int a = random.nextInt(9);

		if (a < 5) {
			return mController.getRandomMove(a);
		} else {
			return getNextMove();
		}
	}

	public boolean hasPiece() {
		return hasPiece;
	}
}

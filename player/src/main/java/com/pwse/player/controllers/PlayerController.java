package com.pwse.player.controllers;



import com.pwse.player.models.BoardDimensions;
import com.pwse.player.models.Exceptions.WrongMoveException;
import com.pwse.player.models.Position;

public class PlayerController {

	private MovementController mController;
	private boolean hasPiece;



	public PlayerController(BoardDimensions boardDimensions) {
		this.mController = new MovementController();
		this.hasPiece = false;
	}

	public void moveTo(Position pos) throws WrongMoveException {
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
		//TODO: throw
		hasPiece = true;
	}

	public void dropPiece() {
		//TODO: throw
		hasPiece = false;
	}

	public void testPiece() {
		//TODO
	}

	public void destroyPiece() {
		//TODO
		//InfoSingleton.getInstance().getBoardInfo().removePiece();
	}
}

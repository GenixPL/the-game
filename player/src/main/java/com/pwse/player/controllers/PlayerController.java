package com.pwse.player.controllers;



import com.pwse.player.models.BoardDimensions;
import com.pwse.player.models.Exceptions.WrongMoveException;

public class PlayerController {

	private MovementController mController;
	private boolean hasPiece;



	public PlayerController(BoardDimensions boardDimensions) {
		this.mController = new MovementController();
		this.hasPiece = false;
	}

	public void moveUp() throws WrongMoveException {
		mController.moveUp();
	}

	public void moveDown() throws WrongMoveException {
		mController.moveDown();
	}

	public void moveLeft() throws WrongMoveException {
		mController.moveLeft();
	}

	public void moveRight() throws WrongMoveException {
		mController.moveRight();
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

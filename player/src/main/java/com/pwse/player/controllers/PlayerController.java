package com.pwse.player.controllers;



import com.pwse.player.models.BoardDimensions;
import com.pwse.player.models.Exceptions.WrongMoveException;

public class PlayerController {

	private MovementController mController;
	private ConnectionController cController;



	public PlayerController(BoardDimensions boardDimensions, ConnectionController connectionController) {
		this.mController = new MovementController();
	}

	public void askForInfo() {
		//TODO
		cController.sendMessage("ask for info");
	}

	public void discoverSurrounding() {
		//TODO
		cController.sendMessage("ask about surrounding");
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
}

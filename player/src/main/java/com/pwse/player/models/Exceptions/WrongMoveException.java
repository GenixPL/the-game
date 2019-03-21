package com.pwse.player.models.Exceptions;

public class WrongMoveException extends Exception {
	@Override
	public String getMessage() {
		return "Player can not move in given direction";
	}
}

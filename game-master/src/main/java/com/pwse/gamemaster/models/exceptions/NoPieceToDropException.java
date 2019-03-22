package com.pwse.gamemaster.models.exceptions;

public class NoPieceToDropException extends Exception {
	@Override
	public String getMessage() {
		return "Player has no piece, so he can't drop one";
	}
}

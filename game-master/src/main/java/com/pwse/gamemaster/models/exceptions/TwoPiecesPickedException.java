package com.pwse.gamemaster.models.exceptions;

public class TwoPiecesPickedException extends Exception {
	@Override
	public String getMessage() {
		return "Player can pick only one piece at a time";
	}
}

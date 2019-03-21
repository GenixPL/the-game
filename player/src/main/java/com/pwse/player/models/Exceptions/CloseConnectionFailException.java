package com.pwse.player.models.Exceptions;

public class CloseConnectionFailException extends Exception {
	@Override
	public String getMessage() {
		return "Failed to close connection";
	}
}

package com.pwse.player.models.Exceptions;

public class OpenConnectionFailException extends Exception {
	@Override
	public String getMessage() {
		return "Failed to open connection";
	}
}

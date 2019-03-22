package com.pwse.gamemaster.models.exceptions;

public class CloseConnectionFailException extends Exception {
	@Override
	public String getMessage() {
		return "Failed to disconnect connection";
	}
}

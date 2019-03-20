package com.pwse.gamemaster.models.exceptions;

public class OpenConnectionFailException extends Exception {
	@Override
	public String getMessage() {
		return "Failed to open connection";
	}
}

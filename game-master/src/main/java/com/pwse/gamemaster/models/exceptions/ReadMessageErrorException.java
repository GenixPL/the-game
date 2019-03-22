package com.pwse.gamemaster.models.exceptions;

public class ReadMessageErrorException extends Exception {
	@Override
	public String getMessage() {
		return "Failed to read message.";
	}
}

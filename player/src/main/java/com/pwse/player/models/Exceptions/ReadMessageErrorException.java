package com.pwse.player.models.Exceptions;

public class ReadMessageErrorException extends Exception {
	@Override
	public String getMessage() {
		return "Failed to read message.";
	}
}

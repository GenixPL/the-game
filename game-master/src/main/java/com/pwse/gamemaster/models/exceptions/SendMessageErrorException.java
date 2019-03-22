package com.pwse.gamemaster.models.exceptions;

public class SendMessageErrorException extends Exception {
	@Override
	public String getMessage() {
		return "Failed to send message.";
	}
}

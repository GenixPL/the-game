package com.pwse.player.models.Exceptions;

public class SendMessageErrorException extends Exception {
	@Override
	public String getMessage() {
		return "Failed to send message.";
	}
}

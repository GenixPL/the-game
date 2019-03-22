package com.pwse.communicationserver.models.exceptions;

public class ReadMessageErrorException extends Exception {
	@Override
	public String getMessage() {
		return "Failed to read message.";
	}
}

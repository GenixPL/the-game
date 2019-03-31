package com.pwse.communicationserver.models.exceptions;

public class NoSuchJsonKeyException extends Exception {
	@Override
	public String getMessage() {
		return "There is no such key inside given json.";
	}
}

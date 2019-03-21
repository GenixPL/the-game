package com.pwse.communicationserver.models.exceptions;

public class PlConnectionFailedException extends Exception {
	@Override
	public String getMessage() {
		return "Failed to connect with pl.";
	}
}

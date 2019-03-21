package com.pwse.communicationserver.models.exceptions;

public class GmConnectionFailedException extends Exception {
	@Override
	public String getMessage() {
		return "Failed to connect with gm.";
	}
}

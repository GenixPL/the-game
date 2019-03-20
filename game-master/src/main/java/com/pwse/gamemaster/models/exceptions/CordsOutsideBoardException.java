package com.pwse.gamemaster.models.exceptions;

public class CordsOutsideBoardException extends Exception {
	@Override
	public String getMessage() {
		return "Specified coordinates are outside board area";
	}
}

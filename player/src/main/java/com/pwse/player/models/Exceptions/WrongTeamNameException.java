package com.pwse.player.models.Exceptions;

public class WrongTeamNameException extends Exception {
	@Override
	public String getMessage() {
		return "Team can not be named like this.";
	}
}

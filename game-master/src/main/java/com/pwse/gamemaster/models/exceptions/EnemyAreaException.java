package com.pwse.gamemaster.models.exceptions;


/**
 * Exception thrown when player wants to go to enemy's goal area
 */
public class EnemyAreaException extends Exception {
	@Override
	public String getMessage() {
		return "Player can not move to enemy's goal area";
	}
}

package com.pwse.gamemaster.models;

public class BoardField {
	private static final String ANSI_RESET  = "\u001B[0m";

	public char fieldChar;
	public String fieldColor;

	public void print() {
		System.out.print(fieldColor + fieldChar + ANSI_RESET);
	}
}

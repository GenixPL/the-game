package com.pwse.gamemaster.models;


/**
 * This class represents colors in which different things are printed out
 * See <a href="https://dev.to/awwsmm/coloured-terminal-output-with-java-57l3">doc</a>()
 */
public class Colors {
	public static final String blueTeam = "\u001B[34m";
	public static final String redTeam = "\u001B[31m";
	public static final String background = "\u001B[90m";
	public static final String piece = "\u001B[33m";
	public static final String goal = "\u001B[30m" + "\u001B[43m"; //foreground + background

	public static final String error = "\u001B[37m" + "\u001B[47m";//foreground + background

	public static final String ANSI_RESET  = "\u001B[0m";



	private Colors() { }
}

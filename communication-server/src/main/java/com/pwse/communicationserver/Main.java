package com.pwse.communicationserver;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Main {

	private static final String ARGS_PATTERN = "[current year]-[group id]-cs --port [port number] --conf [path to config files]";


	public static void main(String[] args) {
		if (!isEveryArgCorrect(args)) {
			return;
		}

		startServer();
	}

	private static void startServer() {
		System.out.println("Communication server starts...");

		while (true) {
			try {
				Thread.sleep(2000);
				System.out.println("Communication server works...");

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void shutDownServer() {
		System.out.println("Communication server shuts down...");
	}


	private static void informAboutWrongArgsPattern() {
		System.err.println("You should use following pattern for input arguments: `" + ARGS_PATTERN + "`");
	}

	private static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;

		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static boolean isEveryArgCorrect(String[] args) {
		if (args.length != 5) {
			informAboutWrongArgsPattern();
			return false;

		}
		//check if first arg ends with "cs"
		if (!args[0].substring(args[0].length() - 2).equals("cs")) {
			informAboutWrongArgsPattern();
			return false;
		}

		//check if second arg is "--port"
		if (!args[1].equals("--port")) {
			informAboutWrongArgsPattern();
			return false;
		}

		//check if third arg is an int
		if (!isInt(args[2])) {
			informAboutWrongArgsPattern();
			return false;
		}

		//check if port is avaliable
		if (!isPortAvailable(Integer.parseInt(args[2]))) {
			System.err.println("Given port is not available");
			return false;
		}

		//check if fourth arg is "--conf"
		if (!args[3].equals("--conf")) {
			informAboutWrongArgsPattern();
			return false;
		}

		//check if fifth arg gives correct file
		if (!isFileCorrect(args[4])) {
			System.err.println("Given config file is not correct");
			return false;
		}

		return true;
	}

	private static boolean isPortAvailable(int port) {
		try {
			new Socket("localhost", port);

			// If the code makes it this far without an exception it mean something is using the port and has responded.
			return false;

		} catch (IOException e) {
			return true;
		}
	}

	private static boolean isFileCorrect(String path) {
		File file = new File(path);

		if (!file.exists()) {
			return false;
		}

		//check if it is .json file
		String fileName = file.getName();
		if (!fileName.substring(fileName.lastIndexOf('.') + 1).equals("json")) {
			return false;
		}

		return true;
	}
}

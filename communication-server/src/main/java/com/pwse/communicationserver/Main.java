package com.pwse.communicationserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Main {

	private static final String ARGS_PATTERN = "[current year]-[group id]-cs --port [port number] --conf [path to config file]";
	private static int port;
	private static int numOfPlayers;



	public static void main(String args[]) {
		if (!isEveryArgCorrect(args)) {
			return;
		}

		port = Integer.parseInt(args[2]);
		numOfPlayers = getNumOfPlayers(args[4]);

		startServer();
	}


	private static void startServer() {
		System.out.println("Communication server starts...");

		Work work = new Work(port, numOfPlayers);
		work.run();

		shutDownServer();
	}

	private static void shutDownServer() {
		System.out.println("Communication server shuts down...");
		System.exit(0);
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

	private static boolean isEveryArgCorrect(String args[]) {
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

	private static int getNumOfPlayers(String filePath) {
		String fileContent = null;
		try {
			fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject object = new JSONObject(fileContent);

		return object.getInt("number-of-players");
	}
}

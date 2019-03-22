package com.pwse.communicationserver;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.pwse.communicationserver.controllers.WorkController;
import org.json.JSONObject;

public class Main {

	private static final String ARGS_PATTERN = "[current year]-[group id]-cs --port [port number] --conf [path to config file]";



	public static void main(String args[]) {
		if (!isEveryArgCorrect(args)) {
			return;
		}

		startServer(args);
	}



	private static void startServer(String[] args) {
		int numOfPlayers = getNumOfPlayers(args[4]);

		int gmPort = Integer.parseInt(args[2]);
		int[] plPorts = new int[numOfPlayers];

		for (int i = 0; i < numOfPlayers; i++) {
			plPorts[i] = gmPort + 1 + i;
		}

		WorkController workController = new WorkController(gmPort, plPorts);
		workController.run();
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

		//check if port is avaliable
		if (!isEveryPortAvailable(Integer.parseInt(args[2]), getNumOfPlayers(args[4]))) {
			System.err.println("Given ports are not available");
			return false;
		}

		return true;
	}

	/**
	 * We are checking many ports:
	 *  port (first) - for gm
	 *  port+i - for each player
	 */
	private static boolean isEveryPortAvailable(int port, int numOfPlayers) {
		for (int i = 0; i < numOfPlayers + 1; i++) {
			try {
				new Socket("localhost", port + i);

				// If the code makes it this far without an exception it mean something is using the port and has responded.
				return false;

			} catch (IOException e) { }
		}

		return true;
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

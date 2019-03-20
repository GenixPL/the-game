package com.pwse.gamemaster;

import com.pwse.gamemaster.models.board.Board;
import com.pwse.gamemaster.models.board.BoardDimensions;
import com.pwse.gamemaster.models.board.BoardField;
import com.pwse.gamemaster.models.piece.Piece;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

	private static final String ARGS_PATTERN = "[current year]-[group id]-gm --address [server address] --port [port number] --conf [path to config file]";
	private static int csPort;
	private static int numOfPlayers;
	private static String csAddress;



	public static void main(String[] args) {
		if (!isEveryArgCorrect(args)) {
			return;
		}

		csPort = Integer.parseInt(args[4]);
		numOfPlayers = getNumOfPlayers(args[6]);
		csAddress = args[2];

		startGame(args);
	}



	private static void startGame(String[] args) {
		System.out.println("GM starts");
		String filePath = args[6];
		BoardDimensions dim = getBoardDimensions(filePath);
		BoardField[] goals = getGoals(filePath).toArray(new BoardField[0]);
		int numOfPieces = getNumberOfPieces(filePath);
		double shamProbability = getShamProbability(filePath);
		int pieceSpawnFrequency = getPieceSpawnFrequency(filePath);

		Work work = new Work(csPort, numOfPlayers, csAddress, dim, goals, numOfPieces, shamProbability, pieceSpawnFrequency);
		work.run();

		shutDownGame();
	}

	private static void shutDownGame() {
		System.out.println("GM shuts down");
		System.exit(0);
	}

	private static boolean isEveryArgCorrect(String[] args) {
		if (args.length != 7) {
			informAboutWrongArgsPattern();
			return false;

		}
		//check if first arg ends with "gm"
		if (!args[0].substring(args[0].length() - 2).equals("gm")) {
			informAboutWrongArgsPattern();
			return false;
		}

		//check if second arg is "--address"
		if (!args[1].equals("--address")) {
			informAboutWrongArgsPattern();
			return false;
		}

		//check if third arg is a proper address
		if (!isProperAddress(args[2])) {
			System.err.println("Given address is not proper");
			return false;
		}

		//check if fourth arg is "--port"
		if (!args[3].equals("--port")) {
			informAboutWrongArgsPattern();
			return false;
		}

		//check if fifth arg is an int
		if (!isInt(args[4])) {
			System.err.println("Given port number is not an int");
			return false;
		}

		//check if port is available
//        if (!isPortOpened(Integer.parseInt(args[4]))) {
//            System.err.println("Given port is not opened");
//            return false;
//        } commented for board debugging

		//check if sixth arg is "--conf"
		if (!args[5].equals("--conf")) {
			informAboutWrongArgsPattern();
			return false;
		}

		//check if seventh arg gives correct file
		if (!isFileCorrect(args[6])) {
			System.err.println("Given config file is not correct");
			return false;
		}

		return true;
	}

	private static void informAboutWrongArgsPattern() {
		System.err.println("You should use following pattern for input arguments: `" + ARGS_PATTERN + "`");
	}

	private static boolean isProperAddress(String str) {
		return true; //xD
	}

	private static boolean isPortOpened(int port) {
		try {
			new Socket("localhost", port);

			// If the code makes it this far without an exception it means something is using the port and has responded.
			return true;

		} catch (IOException e) {
			return false;
		}
	}

	private static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;

		} catch (NumberFormatException e) {
			return false;
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
		JSONObject file = new JSONObject(getFileContent(filePath));

		return file.getInt("number-of-players");
	}

	private static BoardDimensions getBoardDimensions(String filePath) {
		JSONObject file = new JSONObject(getFileContent(filePath));
		JSONObject board = file.getJSONObject("board-dimensions");
		int w = board.getInt("width");
		int h = board.getInt("height");
		int hoga = board.getInt("height-of-goal-area");

		return new BoardDimensions(w, h, hoga);
	}

	private static ArrayList<BoardField> getGoals(String filePath) {
		JSONObject file = new JSONObject(getFileContent(filePath));
		JSONArray goalsArr = file.getJSONArray("goals");

		ArrayList<BoardField> goals = new ArrayList<>(0);
		for (int i = 0; i < goalsArr.length(); i++) {
			JSONObject piece = goalsArr.getJSONObject(i);
			int posX = piece.getInt("x");
			int posY = piece.getInt("y");
			goals.add(new BoardField(posX, posY, true));
		}

		return goals;
	}

	private static int getNumberOfPieces(String filePath) {
		JSONObject file = new JSONObject(getFileContent(filePath));

		return file.getInt("number-of-pieces");
	}

	private static double getShamProbability(String filePath) {
		JSONObject file = new JSONObject(getFileContent(filePath));

		return file.getDouble("probability-of-sham");
	}

	private static int getPieceSpawnFrequency(String filePath) {
		JSONObject file = new JSONObject(getFileContent(filePath));

		return file.getInt("piece-spawn-frequency");
	}

	private static String getFileContent(String filePath) {
		String fileContent = null;
		try {
			fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		return fileContent;
	}
}

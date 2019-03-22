package com.pwse.player;

import com.pwse.player.controllers.WorkController;
import com.pwse.player.models.BoardDimensions;
import com.pwse.player.models.ConnectionData;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Main {

    private static final String ARGS_PATTERN = "[current year]-[group id]-pl --address [server address] --port [port number] --conf [path to config file]";



    public static void main(String[] args) {
        if (!isEveryArgCorrect(args)) {
            return;
        }

        startGame(args);
    }



    private static void startGame(String[] args) {
        String filePath = args[6];

        WorkController workController = new WorkController(getConnectionData(args), getBoardDimensions(filePath));
        workController.run();
    }

    private static boolean isEveryArgCorrect(String args[]) {
        if (args.length != 7) {
            informAboutWrongArgsPattern();
            return false;

        }
        //check if first arg ends with "pl"
        if (!args[0].substring(args[0].length() - 2).equals("pl")) {
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
        if (!isPortOpened(Integer.parseInt(args[4]))) {
            System.err.println("Given port is not opened");
            return false;
        }

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
//        try {
//            new Socket("localhost", port);
//
//            // If the code makes it this far without an exception it means something is using the port and has responded.
//            return true;
//
//        } catch (IOException e) {
//            return false;
//        }
        return true; //TODO: this is wrong way
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

    private static BoardDimensions getBoardDimensions(String filePath) {
        JSONObject file = new JSONObject(getFileContent(filePath));
        JSONObject board = file.getJSONObject("board-dimensions");
        int w = board.getInt("width");
        int h = board.getInt("height");
        int hoga = board.getInt("height-of-goal-area");

        return new BoardDimensions(w, h, hoga);
    }

    private static ConnectionData getConnectionData(String[] args) {
        int port = Integer.parseInt(args[4]);
        String address = args[2];

        return new ConnectionData(port, address);
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

package com.pwse.gamemaster;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

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

        startGame();
    }



    private static void startGame() {
        System.out.println("GM starts");

        Work work = new Work(csPort, numOfPlayers, csAddress);
        work.run();

        shutDownGame();
    }

    private static void shutDownGame() {
        System.out.println("GM shuts down");
        System.exit(0);
    }

    private static boolean isEveryArgCorrect(String args[]) {
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

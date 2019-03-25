package com.pwse.gamemaster.controllers;

import com.pwse.gamemaster.controllers.helpers.BoardChecker;
import com.pwse.gamemaster.controllers.helpers.Messenger;
import com.pwse.gamemaster.models.ConnectionData;
import com.pwse.gamemaster.models.GameData;
import com.pwse.gamemaster.models.exceptions.CloseConnectionFailException;
import com.pwse.gamemaster.models.exceptions.OpenConnectionFailException;
import com.pwse.gamemaster.models.exceptions.ReadMessageErrorException;
import com.pwse.gamemaster.models.exceptions.SendMessageErrorException;
import com.pwse.gamemaster.view.BoardPrinter;
import org.json.JSONObject;


public class WorkController {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private ConnectionController cController;
	private BoardController bController;
	private int numOfPlayers;


	public WorkController(ConnectionData connectionData, GameData gameData) {
		this.cController = new ConnectionController(connectionData);
		this.bController = new BoardController(gameData);
		this.numOfPlayers = gameData.getNumOfPlayers();
	}

	public void run() {
		start();
	}


	private void start() {
		try {
			cController.connect();
		} catch (OpenConnectionFailException e) {
			System.exit(-1);
		}

		doWork();
	}

	private void stop() {
		try {
			cController.disconnect();
		} catch (CloseConnectionFailException e) {
			System.exit(-1);
		}

		System.exit(0);
	}

	private void doWork() {
		System.out.println(TAG + "starting work");
		bController.printBoard();

		exchangePlayersInfo();

		waitForStartMessage();

		while (!bController.isGameEnded()) {
			JSONObject json;
			try {
				json = cController.getMessage();
				reactToMsg(json);

			} catch (ReadMessageErrorException e) {
				System.err.println(e.getMessage());
			}

			bController.printBoard();
		}

		//send message informing that game has ended
		try {
			cController.sendMessage(Messenger.createMsgWithAction("end"));
		} catch (SendMessageErrorException e) {
			System.err.println(TAG + e.getMessage());
		}


		System.out.println(TAG + "stopping work");
		stop();
	}

	private void exchangePlayersInfo() {
		for (int i = 0; i < numOfPlayers; i++) {
			try {
				System.out.println(TAG + "waiting for player-info message for player with id: " + i);
				JSONObject json = cController.getMessage();
				if (!Messenger.getActionFromJson(json).equals("player-info")) {
					System.err.println(TAG + "received message with wrong action");
					System.exit(-1);
				}
				System.out.println(TAG + "message received");

			} catch (ReadMessageErrorException e) {
				System.err.println(TAG + e.getMessage());
				System.exit(-1);
			}

			try {
				System.out.println(TAG + "sending player-info for player with id: " + i);
				JSONObject answerJson = bController.getInfoOfPlayerWithId(i);
				answerJson.put("action", "player-info");
				cController.sendMessage(answerJson);

			} catch (SendMessageErrorException e) {
				System.err.println(TAG + e.getMessage());
				System.exit(-1);
			}
		}
	}

	private void waitForStartMessage() {
		try {
			System.out.println(TAG + "waiting for start message");
			JSONObject json = cController.getMessage();
			if (!Messenger.getActionFromJson(json).equals("start")) {
				System.err.println(TAG + "received message with wrong action");
				System.exit(-1);
			}
			System.out.println(TAG + "message received");

		} catch (ReadMessageErrorException e) {
			System.err.println(TAG + e.getMessage());
			System.exit(-1);
		}
	}

	private void reactToMsg(JSONObject json) {

	}

}

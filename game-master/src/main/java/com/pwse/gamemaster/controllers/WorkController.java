package com.pwse.gamemaster.controllers;

import com.pwse.gamemaster.controllers.helpers.Messenger;
import com.pwse.gamemaster.models.ConnectionData;
import com.pwse.gamemaster.models.GameData;
import com.pwse.gamemaster.models.exceptions.CloseConnectionFailException;
import com.pwse.gamemaster.models.exceptions.OpenConnectionFailException;
import com.pwse.gamemaster.models.exceptions.ReadMessageErrorException;
import com.pwse.gamemaster.models.exceptions.SendMessageErrorException;
import org.json.JSONArray;
import org.json.JSONObject;


public class WorkController {

	//TODO: there should be Logger which would take some value and would block or allow to print certain logs
	// (without debugging there is no need for such spam)

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
				printExceptionAndEnd(e);
			}

			bController.printBoard();
		}

		sendEndOfGameMsg();

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
				JSONObject answerJson = bController.getInfoAboutPlayerWithId(i);
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

	private void sendEndOfGameMsg() {
		try {
			cController.sendMessage(Messenger.createMsgWithAction("end"));
		} catch (SendMessageErrorException e) {
			System.err.println(TAG + e.getMessage());
		}
	}

	private void reactToMsg(JSONObject json) {
		String action = json.getString("action");

		if (action.equals("move")) {
			handleMoveAction(json);

		} else if (action.equals("pick-up")) {
			handlePickUpAction(json);

		} else if (action.equals("drop")) {
			handleDropAction(json);

		} else if (action.equals("discover")) {
			handleDiscoverAction(json);

		} else if (action.equals("test-piece")) {
			handleTestPiece(json);

		} else if (action.equals("destroy-piece")) {
			handledDestroyPiece(json);

		} else {
			System.err.println(TAG + "received message with wrong action");
		}
	}

	private void handleMoveAction(JSONObject json) {
		int id = json.getInt("id");
		int x = json.getInt("x");
		int y = json.getInt("y");

		if (bController.canPlayerWithIdMoveTo(id, x, y)) {
			bController.movePlayerWithIdTo(id, x, y);
			json.put("approved", true);
			json.put("manhattan", bController.getManhattanDistanceToNearestPiece(x, y));
			json.put("field", bController.getFieldInfo(x, y));

		} else {
			json.put("approved", false);
			json.put("manhattan", -1);
			json.put("field", new JSONArray());
		}

		try {
			cController.sendMessage(json);
		} catch (SendMessageErrorException e) {
			printExceptionAndEnd(e);
		}
	}

	private void handlePickUpAction(JSONObject json) {
		int id = json.getInt("id");

		if (bController.canPlayerPickUpPiece(id)) {
			bController.pickUpPieceByPlayerWithId(id);
			json.put("approved", true);


		} else {
			json.put("approved", false);
		}

		try {
			cController.sendMessage(json);
		} catch (SendMessageErrorException e) {
			printExceptionAndEnd(e);
		}
	}

	private void handleDropAction(JSONObject json) {
		int id = json.getInt("id");
		//TODO:error
		if (bController.canPlayerWithIdDropPiece(id)) {
			bController.dropPieceByPlayerWithId(id);
			json.put("approved", true);

		} else {
			json.put("approved", false);
		}

		try {
			cController.sendMessage(json);
		} catch (SendMessageErrorException e) {
			printExceptionAndEnd(e);
		}
	}

	private void handleDiscoverAction(JSONObject json) {
		int id = json.getInt("id");

		json.put("fields", bController.getDiscoveryInfoForPlayerWithId(id));

		try {
			cController.sendMessage(json);
		} catch (SendMessageErrorException e) {
			printExceptionAndEnd(e);
		}
	}

	private void handleTestPiece(JSONObject json) {
		int id = json.getInt("id");

		int p;

		if ((p = bController.testPieceByPlayerWithId(id)) != -1) {
			json.put("approved", true);
			json.put("is-correct", p);

		} else {
			json.put("approved", false);
			json.put("is-correct", -1);
		}

		try {
			cController.sendMessage(json);
		} catch (SendMessageErrorException e) {
			printExceptionAndEnd(e);
		}
	}

	private void handledDestroyPiece(JSONObject json) {
		int id = json.getInt("id");

		if (bController.canPlayerWithIdDestroyPiece(id)) {
			bController.destroyPieceByPlayerWithId(id);
			json.put("approved", true);

		} else {
			json.put("approved", false);
		}

		try {
			cController.sendMessage(json);
		} catch (SendMessageErrorException e) {
			printExceptionAndEnd(e);
		}
	}

	private void printExceptionAndEnd(Exception e) {
		System.err.println(TAG + e.getMessage());
		e.printStackTrace();
		System.exit(-1);
	}

}

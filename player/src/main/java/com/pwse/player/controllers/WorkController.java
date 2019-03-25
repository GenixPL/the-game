package com.pwse.player.controllers;

import com.pwse.player.controllers.helpers.InfoSingleton;
import com.pwse.player.controllers.helpers.Messenger;
import com.pwse.player.models.BoardDimensions;
import com.pwse.player.models.ConnectionData;
import com.pwse.player.models.Exceptions.*;
import com.pwse.player.models.Position;
import com.pwse.player.models.player.PlayerInfo;
import org.json.JSONObject;


/**
 * This class should be responsible for responding to received messages and telling PlayerController what to do.
 */
public class WorkController {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private ConnectionController cController;
	private PlayerController pController;
	private BoardDimensions bDim;

	private boolean shouldWork = true;



	public WorkController(ConnectionData connectionData, BoardDimensions boardDimensions) {
		this.cController = new ConnectionController(connectionData);
		this.pController = new PlayerController(boardDimensions);
		this.bDim = boardDimensions;
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
		System.out.println(TAG + "stopping work");

		try {
			cController.disconnect();
		} catch (CloseConnectionFailException e) {
			System.exit(-1);
		}

		System.exit(0);
	}

	private void doWork() {
		System.out.println(TAG + "starting work");

		exchangePlayerInfo();

		waitForStartMessage();

		//work until "end" message appears
		while (shouldWork) {
			JSONObject json;
			try {
				json = cController.getMessage();
				reactToMsg(json);
			} catch (ReadMessageErrorException e) {
				System.err.println(e.getMessage());
			}
		}

		System.out.println(TAG + "ending work");
		stop();
	}

	private void reactToMsg(JSONObject json) {
		if (Messenger.getActionFromJson(json).equals("end")) {
			shouldWork = false;
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

	private void exchangePlayerInfo() {
		try {
			System.out.println(TAG + "waiting for player-info message");
			JSONObject json = cController.getMessage();
			if (!Messenger.getActionFromJson(json).equals("player-info")) {
				System.err.println(TAG + "received message with wrong action");
				System.exit(-1);
			}
			System.out.println(TAG + "message received");

			try {
				Position pos = new Position(json.getJSONObject("position").getInt("x"), json.getJSONObject("position").getInt("y"));
				InfoSingleton.init(bDim, pos, json.getString("team"));

			} catch (WrongTeamNameException e) {
				System.err.println(TAG + e.getMessage());
				System.exit(-1);
			}

		} catch (ReadMessageErrorException e) {
			System.err.println(TAG + e.getMessage());
			System.exit(-1);
		}

		try {
			System.out.println(TAG + "sending ready message");
			cController.sendMessage(Messenger.createMsgWithAction("ready"));
			System.out.println(TAG + "message sent");

		} catch (SendMessageErrorException e) {
			System.err.println(TAG + e.getMessage());
			System.exit(-1);
		}
	}

	private void exchangeInfo() {
		//TODO: it may be good idea to create some kind of "DecisionMaker" class
	}

	private void askForSurroundings() {
		//TODO: it may be good idea to create some kind of "DecisionMaker" class
	}
}

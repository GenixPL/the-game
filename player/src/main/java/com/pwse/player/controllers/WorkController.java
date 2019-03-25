package com.pwse.player.controllers;

import com.pwse.player.controllers.helpers.InfoSingleton;
import com.pwse.player.controllers.helpers.Messenger;
import com.pwse.player.models.BoardDimensions;
import com.pwse.player.models.ConnectionData;
import com.pwse.player.models.Exceptions.CloseConnectionFailException;
import com.pwse.player.models.Exceptions.OpenConnectionFailException;
import com.pwse.player.models.Exceptions.ReadMessageErrorException;
import com.pwse.player.models.Exceptions.WrongTeamNameException;
import com.pwse.player.models.Position;
import org.json.JSONObject;


/**
 * This class should be responsible for responding to received messages and telling PlayerController what to do.
 */
public class WorkController {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	ConnectionController cController;
	PlayerController pController;

	private boolean shouldWork = true;


	public WorkController(ConnectionData connectionData, BoardDimensions boardDimensions) {
		this.cController = new ConnectionController(connectionData);

		//TODO: wait for connection and receive starting position before creating player
		try {
			InfoSingleton.init(boardDimensions, new Position(0, 0), "red");
			this.pController = new PlayerController(boardDimensions);

		} catch (WrongTeamNameException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
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

		//read until "start" message appears
		String msg = "";
		while (!msg.equals("start")) {
			try {
				msg = Messenger.getActionFromJson(cController.getMessage());
			} catch (ReadMessageErrorException e) {
				e.printStackTrace();
			}
		}

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

	private void exchangeInfo() {
		//TODO: it may be good idea to create some kind of "DecisionMaker" class
	}

	private void askForSurroundings() {
		//TODO: it may be good idea to create some kind of "DecisionMaker" class
	}
}

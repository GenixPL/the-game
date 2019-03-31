package com.pwse.player.controllers;

import com.pwse.player.controllers.helpers.InfoSingleton;
import com.pwse.player.controllers.helpers.Messenger;
import com.pwse.player.models.BoardDimensions;
import com.pwse.player.models.ConnectionData;
import com.pwse.player.models.Exceptions.*;
import com.pwse.player.models.Position;
import org.json.JSONObject;

import java.util.Random;


/**
 * This class should be responsible for responding to received messages and telling PlayerController what to do.
 */
public class WorkController {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private ConnectionController cController;
	private PlayerController pController;
	private BoardDimensions bDim;

	private boolean shouldWork = true;
	private boolean shouldTryRandomMove = false;


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

			if (shouldTryRandomMove) {
				System.out.println(TAG + "making random move");
				cController.sendMessage(createMoveMsg(pController.getNextMovePossibleRandom()));

				shouldTryRandomMove = false;

			} else {
				JSONObject decision = getJsonWithDecision();
				cController.sendMessage(decision);
			}

			try {
				JSONObject response;
				response = cController.getMessage();
				reactToMsg(response);

			} catch (ReadMessageErrorException e) {
				System.err.println(e.getMessage());
			}

			//TODO: this delay should be moved to GM but due to bugs with threads (as I believe), I will leave it here for now
			try {

				Thread.sleep(new Random().nextInt(1000) + 1000);
//				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("\t\t\t===NEXT TURN===");
		}

		System.out.println(TAG + "ending work");
		stop();
	}

	private JSONObject getJsonWithDecision() {
		if (pController.hasPiece()) {
			if (cController.isGoalInCurrentPlace()) {
				return createDropMsg();

			} else {
				return createMoveMsg(pController.getNextMove());
			}

		} else { //has no piece
			if (cController.isPieceInCurrentPlace()) {
				return createPickUpMsg();

			} else {
				return createMoveMsg(pController.getNextMove());
			}
		}
	}

	private void reactToMsg(JSONObject json) {
		String action = Messenger.getActionFromJson(json);

		if (action.equals("end")) {
			shouldWork = false;

		} else if (action.equals("move")) {
			handleMoveResponse(json);

		} else if (action.equals("drop")) {
			handleDropResponse(json);

		} else if (action.equals("pick-up")) {
			handlePickUpResponse(json);

		} else {
			System.err.println("Unknown action");
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


		System.out.println(TAG + "sending ready message");
		cController.sendMessage(Messenger.createMsgWithAction("ready"));
		System.out.println(TAG + "message sent");
	}

	private JSONObject createMoveMsg(Position pos) {
		JSONObject json = new JSONObject();
		json.put("action", "move");
		json.put("x", pos.getX());
		json.put("y", pos.getY());

		return json;
	}

	private JSONObject createPickUpMsg() {
		JSONObject json = new JSONObject();
		json.put("action", "pick-up");

		return json;
	}

	private JSONObject createDropMsg() {
		JSONObject json = new JSONObject();
		json.put("action", "drop");

		return json;
	}

	private void handleMoveResponse(JSONObject response) {
		if (response.getBoolean("approved")) {
			try {
				pController.moveTo(new Position(response.getInt("x"), response.getInt("y")));
			} catch (WrongMoveException e) {
				System.err.println(TAG + "WRONG MOVE EXCEPTION");
			}

		} else {
			System.err.println(TAG + "Wrong move");
			shouldTryRandomMove = true;
		}
	}

	private void handleDropResponse(JSONObject response) {
		if (response.getBoolean("approved")) {
			pController.dropPiece();
		} else {
			System.err.println(TAG + "cannot drop piece");
		}
	}

	private void handlePickUpResponse(JSONObject response) {
		if (response.getBoolean("approved")) {
			pController.pickUpPiece();
		} else {
			System.err.println(TAG + "cannot pick up piece");
		}
	}

	private void exchangeInfo() {
		//TODO: it may be good idea to create some kind of "DecisionMaker" class
	}

	private void askForSurroundings() {
		//TODO: it may be good idea to create some kind of "DecisionMaker" class
	}
}

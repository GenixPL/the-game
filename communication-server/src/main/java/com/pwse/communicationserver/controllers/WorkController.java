package com.pwse.communicationserver.controllers;

import com.pwse.communicationserver.controllers.helpers.Messenger;
import com.pwse.communicationserver.models.exceptions.GmConnectionFailedException;
import com.pwse.communicationserver.models.exceptions.PlConnectionFailedException;
import com.pwse.communicationserver.models.exceptions.ReadMessageErrorException;
import com.pwse.communicationserver.models.exceptions.SendMessageErrorException;
import com.sun.xml.internal.ws.api.model.MEP;
import org.json.JSONObject;


public class WorkController {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private GmCommunicator gmCommunicator;
	private PlCommunicator plCommunicator;
	private int numOfPlayers;

	private boolean shouldWork = true;



	public WorkController(int gmPort, int[] plPorts) {
		this.gmCommunicator = new GmCommunicator(gmPort);
		this.plCommunicator = new PlCommunicator(plPorts);
		this.numOfPlayers = plPorts.length;
	}

	public void run() {
		start();
	}



	private void start() {
		try {
			System.out.println(TAG + "connecting with gm");
			gmCommunicator.openSocket();
			gmCommunicator.connect();

		} catch (GmConnectionFailedException e) {
			printExceptionAndEnd(e);
		}

		try {
			System.out.println(TAG + "connecting with players");
			plCommunicator.openSockets();
			plCommunicator.connect();

		} catch (PlConnectionFailedException e) {
			printExceptionAndEnd(e);
		}

		doWork();
	}

	private void stop() {
		try {
			System.out.println(TAG + "disconnecting with gm");
			gmCommunicator.disconnect();
			gmCommunicator.closeSocket();

		} catch (GmConnectionFailedException e) {
			printExceptionAndEnd(e);
		}

		try {
			System.out.println(TAG + "disconnecting with players");
			plCommunicator.disconnect();
			plCommunicator.closeSockets();

		} catch (PlConnectionFailedException e) {
			printExceptionAndEnd(e);
		}
	}

	private void doWork() {
		System.out.println(TAG + "starting work");

		exchangePlayersInfo();

		sendStartGameMessage();

		while (shouldWork) {
			if (gmCommunicator.isMessageWaiting()) {
				try {
					JSONObject json = gmCommunicator.getMessage();
					System.out.println(TAG + "new message from gm: " + json.toString());
					reactToMsgFromGm(json);

				} catch (ReadMessageErrorException e) {
					printExceptionAndEnd(e);
				}
			}

			for (int i = 0; i < numOfPlayers; i++) {
				if (plCommunicator.isMessageWaitingFromPlayerWithId(i)) {
					try {
						JSONObject json = plCommunicator.getMessageFromPlayerWithId(i);
						System.out.println(TAG + "new message from player with id: " + i + " msg: " + json);
						reactToMsgFromPlayerWithId(i, json);

					} catch (ReadMessageErrorException e) {
						printExceptionAndEnd(e);
					}
				}
			}
		}

		stop();
	}

	private void exchangePlayersInfo() {
		for (int i = 0; i < numOfPlayers; i++) {
			//ask for player's info
			try {
				System.out.println(TAG + "asking for location and team of player with id: " + i);
				JSONObject playerInfoJson = new JSONObject();
				playerInfoJson.put("action", "player-info");
				playerInfoJson.put("id", i);
				gmCommunicator.sendMessage(playerInfoJson);
				System.out.println(TAG + "message sent");

			} catch (SendMessageErrorException e) {
				printExceptionAndEnd(e);
			}

			//get player's info
			JSONObject receivedPlayerInfoJson = null;
			try {
				System.out.println(TAG + "waiting for location and team of player with id: " + i);
				receivedPlayerInfoJson = gmCommunicator.getMessage();
				if (!Messenger.getActionFromJson(receivedPlayerInfoJson).equals("player-info")) {
					System.err.println(TAG + "received message with wrong action");
					System.exit(-1);
				}
				plCommunicator.setPlayerTeamFromJson(receivedPlayerInfoJson);
				System.out.println(TAG + "message received");

			} catch (ReadMessageErrorException e) {
				printExceptionAndEnd(e);
			}

			//send info to player
			try {
				System.out.println(TAG + "sending location and team to player with id: " + i);
				plCommunicator.sendMsgToPlayerWithId(i, receivedPlayerInfoJson);
				System.out.println(TAG + "message sent");

			} catch (SendMessageErrorException e) {
				printExceptionAndEnd(e);
			}

			//wait for ready message from player
			try {
				System.out.println(TAG + "waiting for ready message from player with id: " + i);
				JSONObject receivedMsg = plCommunicator.getMessageFromPlayerWithId(i);
				if (!Messenger.getActionFromJson(receivedMsg).equals("ready")) {
					System.err.println(TAG + "received message with wrong action");
					System.exit(-1);
				}

			} catch (ReadMessageErrorException e) {
				printExceptionAndEnd(e);
			}
		}
	}

	private void sendStartGameMessage() {
		try {
			System.out.println(TAG + "sending start messages to gm and players");

			JSONObject msg = Messenger.createMsgWithAction("start");

			gmCommunicator.sendMessage(msg);
			plCommunicator.sendToAll(msg);

			System.out.println(TAG + "messages sent");

		} catch (SendMessageErrorException e) {
			printExceptionAndEnd(e);
		}
	}

	private void reactToMsgFromGm(JSONObject json) {
		if (Messenger.getActionFromJson(json).equals("end")) {
			shouldWork = false;

			try {
				plCommunicator.sendToAll(json);
			} catch (SendMessageErrorException e) {
				printExceptionAndEnd(e);
			}
		}
	}

	private void reactToMsgFromPlayerWithId(int id, JSONObject json) {

	}

	private void printExceptionAndEnd(Exception e) {
		System.err.println(TAG + e.getMessage());
		e.printStackTrace();
		System.exit(-1);
	}

}

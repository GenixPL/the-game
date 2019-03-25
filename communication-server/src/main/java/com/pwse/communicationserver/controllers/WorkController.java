package com.pwse.communicationserver.controllers;

import com.pwse.communicationserver.controllers.helpers.Messenger;
import com.pwse.communicationserver.models.exceptions.GmConnectionFailedException;
import com.pwse.communicationserver.models.exceptions.PlConnectionFailedException;
import com.pwse.communicationserver.models.exceptions.ReadMessageErrorException;
import com.pwse.communicationserver.models.exceptions.SendMessageErrorException;
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
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		try {
			System.out.println(TAG + "connecting with pl");
			plCommunicator.openSockets();
			plCommunicator.connect();

		} catch (PlConnectionFailedException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		doWork();
	}

	private void stop() {
		try {
			System.out.println(TAG + "disconnecting with gm");
			gmCommunicator.disconnect();
			gmCommunicator.closeSocket();

		} catch (GmConnectionFailedException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		try {
			System.out.println(TAG + "disconnecting with pl");
			plCommunicator.disconnect();
			plCommunicator.closeSockets();

		} catch (PlConnectionFailedException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}

	private void doWork() {
		System.out.println(TAG + "starting work");

		//send message to gm informing that everyone has connected and game can be started
		try {
			gmCommunicator.sendMessage(Messenger.createMsgWithAction("start"));
		} catch (SendMessageErrorException e) {
			e.printStackTrace();
		}

		while (shouldWork) {
			if (gmCommunicator.isMessageWaiting()) {
				try {
					String msg = gmCommunicator.getMessage();
					System.out.println(TAG + "new message from gm: " + msg);
					reactToMsgFromGm(msg);

				} catch (ReadMessageErrorException e) {
					System.err.println(e.getMessage());
				}
			}

			for (int i = 0; i < numOfPlayers; i++) {
				if (plCommunicator.isMessageWaitingFromPlayerWithId(i)) {
					try {
						String msg = plCommunicator.getMessageFromPlayerWithId(i);
						System.out.println(TAG + "new message from pl with id: " + i + " msg: " + msg);
						reactToMsgFromPlayerWithId(i);

					} catch (ReadMessageErrorException e) {
						e.printStackTrace();
					}
				}
			}
		}

		stop();
	}

	private void reactToMsgFromGm(String msg) {
		JSONObject json = new JSONObject(msg);

		if (Messenger.getActionFromJson(json).equals("end")) {
			shouldWork = false;

			try {
				plCommunicator.sendToAll(json);
			} catch (SendMessageErrorException e) {
				System.err.println(TAG + "sending to all pl " + e.getMessage());
			}
		}
	}

	private void reactToMsgFromPlayerWithId(int id) {
	
	}

}

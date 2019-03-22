package com.pwse.communicationserver.controllers;

import com.pwse.communicationserver.models.exceptions.GmConnectionFailedException;
import com.pwse.communicationserver.models.exceptions.PlConnectionFailedException;
import com.pwse.communicationserver.models.exceptions.ReadMessageErrorException;
import com.pwse.communicationserver.models.exceptions.SendMessageErrorException;
import org.json.JSONObject;


public class WorkController {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private GmCommunicator gmCommunicator;
	private PlCommunicator plCommunicator;



	public WorkController(int gmPort, int[] plPorts) {
		this.gmCommunicator = new GmCommunicator(gmPort);
		this.plCommunicator = new PlCommunicator(plPorts);
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
		System.out.println(TAG + "starting message passing");

		while (true) { //TODO: exit when proper message from gm comes

			if (gmCommunicator.isMessageWaiting()) {
				try {
					String msg = gmCommunicator.getMessage();
					System.out.println(TAG + "new message from gm: " + msg);

					JSONObject json = new JSONObject(msg);
					if (json.getString("action").equals("end")) {

						try {
							plCommunicator.sendToAll(json);
						} catch (SendMessageErrorException e) {
							System.err.println(TAG + "sending to all pl " + e.getMessage());
						}

						stop();
						break;
					}

				} catch (ReadMessageErrorException e) {
					System.err.println(e.getMessage());
				}
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

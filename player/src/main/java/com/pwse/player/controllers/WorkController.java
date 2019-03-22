package com.pwse.player.controllers;

import com.pwse.player.controllers.helpers.InfoSingleton;
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

		System.out.println(TAG + "starting work");
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
		while (true) {
			if (cController.isMessageWaiting()) {
				try {
					String msg = cController.getMessage();
					System.out.println(TAG + "new message from gm: " + msg);

					JSONObject json = new JSONObject(msg);
					if (json.getString("action").equals("end")) {
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

		stop();
	}

	private void exchangeInfo() {
		//TODO: it may be good idea to create some kind of "DecisionMaker" class
	}

	private void askForSurroundings() {
		//TODO: it may be good idea to create some kind of "DecisionMaker" class
	}
}

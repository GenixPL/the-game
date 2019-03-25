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



	public WorkController(ConnectionData connectionData, GameData gameData) {
		this.cController = new ConnectionController(connectionData);
		this.bController = new BoardController(gameData);
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
		System.out.println(TAG + "waiting for starting message...");
		bController.printBoard();

		//read until "start" message appears
		String msg = "";
		while (!msg.equals("start")) {
			try {
				msg = Messenger.getActionFromJson(cController.getMessage());
			} catch (ReadMessageErrorException e) {
				e.printStackTrace();
			}
		}

		//work until game has ended
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

		stop();
	}

	private void reactToMsg(JSONObject json) {

	}

}

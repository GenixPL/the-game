package com.pwse.gamemaster.controllers;

import com.pwse.gamemaster.controllers.helpers.BoardChecker;
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
		//TODO
		bController.printBoard();
		String msg = "";
		while (!msg.equals("start")) {
			try {
				JSONObject object = new JSONObject(cController.getMessage());
				msg = object.getString("action");
			} catch (ReadMessageErrorException e) {
				e.printStackTrace();
			}
		}


		int i = 0;
		while (!bController.isGameEnded()) {
			if (i < 12) {
				bController.movePlayerTo(1, 0, i++); //move down

				JSONObject json = new JSONObject();
				json.put("action", "move-down");
				try {
					cController.sendMessage(json);
				} catch (SendMessageErrorException e) {
					System.err.println(e.getMessage());
				}

			} else {
				bController.movePlayerTo(1, 0, (24 - i++)); //move up

				JSONObject json = new JSONObject();
				json.put("action", "move-up");
				try {
					cController.sendMessage(json);
				} catch (SendMessageErrorException e) {
					System.err.println(e.getMessage());
				}
			}
			bController.dropPiece(1);
			bController.pickUpPiece(1);

			bController.printBoard();

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		JSONObject json = new JSONObject();
		json.put("action", "end");
		try {
			cController.sendMessage(json);
		} catch (SendMessageErrorException e) {
			System.err.println(TAG + e.getMessage());
		}

		stop();
	}

}
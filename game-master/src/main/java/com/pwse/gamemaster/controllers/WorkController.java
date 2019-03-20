package com.pwse.gamemaster.controllers;

import com.pwse.gamemaster.models.ConnectionData;
import com.pwse.gamemaster.models.GameData;
import com.pwse.gamemaster.models.exceptions.CloseConnectionFailException;



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
		//TODO: uncomment
//		try {
//			cController.connect();
//		} catch (OpenConnectionFailException e) {
//			System.exit(-1);
//		}

		System.out.println(TAG + "starting work");
		doWork();
	}

	private void stop() {
		System.out.println(TAG + "stopping work");

		try {
			cController.close();
		} catch (CloseConnectionFailException e) {
			System.exit(-1);
		}

		System.exit(0);
	}

	private void doWork() {
		//TODO
		int i = 0;
		while (!bController.isGameEnded()) {
			if (i < 12) {
				bController.movePlayerTo(1, 0, i++); //move down
			} else {
				bController.movePlayerTo(1, 0, (30 - i++)); //move up
			}
			bController.dropPiece(1);
			bController.pickUpPiece(1);

			bController.printBoard();

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

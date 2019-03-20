package com.pwse.gamemaster;

import com.pwse.gamemaster.controllers.BoardController;
import com.pwse.gamemaster.models.board.BoardDimensions;
import com.pwse.gamemaster.models.board.BoardField;
import com.pwse.gamemaster.models.piece.Piece;

import java.io.IOException;
import java.net.Socket;

public class Work {

	private int csPort;
	private String csAddress;

	private boolean isWorking = false;
	private BoardController boardController;

	//TODO: connection should be moved to separate ConnectionController
	private Socket csSocket;



	Work(int csPort,
	     int numOfPlayers,
	     String csAddress,
	     BoardDimensions boardDimensions,
	     BoardField[] goals,
	     int numOfPieces,
	     double shamProbability,
	     int pieceSpawnFrequency
	) {
		this.csPort = csPort;
		this.csAddress = csAddress;

		this.boardController = new BoardController(boardDimensions, goals, numOfPieces, shamProbability, pieceSpawnFrequency, numOfPlayers);
	}

	public void run() {
		start();
	}



	private void start() {
//		connectWithCs(); commented for board debugging

		System.out.println("work starts");
		isWorking = true;
		doWork();
	}

	private void stop() {
		System.out.println("work stops");
		isWorking = false;

		closeSocket();
	}

	private void doWork() {
		int posY = 0;
		while (true) {
			boardController.printBoard();
			if (!boardController.movePlayerTo(1, 0, posY++)) {
				System.err.println("wrong move");
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean connectWithCs() {
		System.out.println("connecting with cs");

		try {
			csSocket = new Socket(csAddress, csPort);

		} catch (IOException e) {
			System.err.println("failed to connect with cs");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private void closeSocket() {
		System.out.println("closing socket");

		try {
			csSocket.close();

		} catch (IOException e) {
			System.err.println("failed to close cs socket");
			e.printStackTrace();

		} catch (NullPointerException e) {
			System.out.println("no cs socket to close");
		}
	}
}

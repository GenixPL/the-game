package com.pwse.communicationserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Work {

	public boolean isWorking() {
		return isWorking;
	}

	private boolean isWorking = false;
	private int port;
	private int numOfPlayers;

	private ServerSocket server;
	private Socket game;
	private ArrayList<Socket> players;



	Work(int port, int numberOfPlayers) {
		this.port = port;
		this.numOfPlayers = numberOfPlayers;
		players = new ArrayList<>(numberOfPlayers);

		System.out.println("num:" + numberOfPlayers);
	}

	public void run() {
		start();
	}



	private void start() {
		boolean isServerOpened = openServerSocket();
		if (!isServerOpened) {
			return;
		}

		boolean isGameConnected = connectGameSocket();
		if (!isGameConnected) {
			return;
		}

		boolean isEveryPlayerConnected = connectPlayers();
		if (!isEveryPlayerConnected) {
			return;
		}

		System.out.println("work starts...");
		isWorking = true;
		doWork();
	}

	private void stop() {
		System.out.println("work stops...");
		isWorking = false;

		closeSockets();
	}

	private boolean openServerSocket() {
		System.out.println("opening server socket...");
		try {
			server = new ServerSocket(port);

		} catch (IOException e) {
			System.err.println("error occurred during opening of server socket");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean connectGameSocket() {
		System.out.println("connecting game...");
		try {
			game = server.accept();
		} catch (IOException e) {
			System.err.println("failed to connect game");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean connectPlayers() {
		System.out.println("connecting players...");
		for (int i = 0; i < numOfPlayers; i++) {
			System.out.println("\tconnecting player num " + i + " of " + numOfPlayers);

			try {
				Socket player = server.accept();
				players.set(i, player);

			} catch (IOException e) {
				System.err.println("failed to connect players");
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	private void closeSockets() {
		System.out.println("closing sockets...");
//		try {
//			game.close();
//		} catch (IOException e) {
//			System.err.println("failed to close game socket");
//			e.printStackTrace();
//		}

//		players.forEach((p) -> {
//			try {
//				p.close();
//			} catch (IOException e) {
//				System.err.println("failed to close player socket");
//				e.printStackTrace();
//			}
//		});

		try {
			server.close();
		} catch (IOException e) {
			System.err.println("failed to close server socket");
			e.printStackTrace();
		}
	}

	private void doWork() {
		TimerTask stopTask = new TimerTask() {
			@Override
			public void run() {
				stop();
			}
		};
		new Timer().schedule(stopTask, 5000);

		while (isWorking) {
			System.out.println("working...");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

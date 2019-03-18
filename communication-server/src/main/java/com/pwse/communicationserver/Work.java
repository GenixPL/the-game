package com.pwse.communicationserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Work {

	private boolean isWorking = false;
	private int port;
	private int numOfPlayers;

	private ServerSocket gmServer;
	private Socket gmSocket;
	private ArrayList<ServerSocket> plServers;
	private ArrayList<Socket> plSockets;



	Work(int port, int numberOfPlayers) {
		this.port = port;
		this.numOfPlayers = numberOfPlayers;
		plServers = new ArrayList<>(numberOfPlayers);
		plSockets = new ArrayList<>(numberOfPlayers);
	}

	public void run() {
		start();
	}



	private void start() {
		boolean isServerOpened = openServerSockets();
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

		System.out.println("work starts");
		isWorking = true;
		doWork();
	}

	private void stop() {
		System.out.println("work stops");
		isWorking = false;

		closeSockets();
	}

	private boolean openServerSockets() {
		System.out.println("opening sockets");

		for (int i = 0; i < numOfPlayers; i++) {
			try {
				if (i == 0) {
					gmServer = new ServerSocket(port);

				} else {
					plServers.add(new ServerSocket(port + i));
				}

			} catch (IOException e) {
				System.err.println("error occurred during opening of csSocket socket");
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	private boolean connectGameSocket() {
		System.out.println("connecting gm socket at port: " + port);
		try {
			gmSocket = gmServer.accept();
		} catch (IOException e) {
			System.err.println("failed to connect gm socket");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean connectPlayers() {
		System.out.println("connecting pl sockets");
		for (int i = 1; i < numOfPlayers + 1; i++) {
			System.out.println("\tconnecting player num " + i + " of " + numOfPlayers + " at port: " + (port + i));

			try {
				Socket player = plServers.get(i - 1).accept();
				plSockets.add(player);

			} catch (IOException e) {
				System.err.println("failed to connect pl sockets");
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	private void closeSockets() {
		System.out.println("closing sockets");

		try {
			gmSocket.close();

		} catch (IOException e) {
			System.err.println("failed to close cs socket");
			e.printStackTrace();

		} catch (NullPointerException e) {
			System.out.println("no gm socket to close");
		}


		plSockets.forEach((p) -> {
			try {
				p.close();

			} catch (IOException e) {
				System.err.println("failed to close player socket");
				e.printStackTrace();

			} catch (NullPointerException e) {
				System.out.println("no pl socket to close");
			}
		});


		try {
			gmSocket.close();

		} catch (IOException e) {
			System.err.println("failed to close csSocket socket");
			e.printStackTrace();

		} catch (NullPointerException e) {
			System.out.println("no cs socket to close");
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
			System.out.println("working");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

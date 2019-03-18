package com.pwse.gamemaster;

import java.io.IOException;
import java.net.Socket;

public class Work {

	private int csPort;
	private int numOfPlayers;
	private String csAddress;
	private boolean isWorking = false;

	private Socket csSocket;



	Work(int csPort, int numOfPlayers, String csAddress) {
		this.csPort = csPort;
		this.numOfPlayers = numOfPlayers;
		this.csAddress = csAddress;
	}

	public void run() {
		start();
	}


	private void start() {
		connectWithCs();
	}

	private void stop() {

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
}

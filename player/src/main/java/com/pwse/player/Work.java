package com.pwse.player;

import java.io.IOException;
import java.net.Socket;

public class Work {

	private int csPort;
	private String csAddress;
	private boolean isWorking = false;

	private Socket csSocket;



	Work(int csPort, String csAddress) {
		this.csPort = csPort;
		this.csAddress = csAddress;
	}

	public void run() {
		start();
	}



	private void start() {
		connectWithCs();

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
		while (true) {
			System.out.println("working");

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
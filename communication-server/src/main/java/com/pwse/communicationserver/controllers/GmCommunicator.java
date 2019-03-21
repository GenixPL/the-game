package com.pwse.communicationserver.controllers;


import com.pwse.communicationserver.models.exceptions.GmConnectionFailedException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class GmCommunicator {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private int port;
	private ServerSocket serverSocket;
	private Socket gmSocket;



	public GmCommunicator(int port) {
		this.port = port;
	}

	public void openSocket() throws GmConnectionFailedException {
		try {
			System.out.println(TAG + "opening gm socket at port: " + port);
			serverSocket = new ServerSocket(port);

		} catch (IOException e) {
			throw new GmConnectionFailedException();
		}
	}

	public void closeSocket() throws GmConnectionFailedException {
		try {
			System.out.println(TAG + "closing gm socket at port: " + port);
			serverSocket.close();

		} catch (IOException e) {
			throw new GmConnectionFailedException();
		}
	}

	public void connect() throws GmConnectionFailedException {
		try {
			System.out.println(TAG + "connecting gm");
			gmSocket = serverSocket.accept();

		} catch (IOException e) {
			throw new GmConnectionFailedException();
		}
	}

	public void disconnect() throws GmConnectionFailedException {
		try {
			System.out.println(TAG + "disconnecting gm");
			gmSocket.close();

		} catch (IOException e) {
			throw new GmConnectionFailedException();
		}
	}

}

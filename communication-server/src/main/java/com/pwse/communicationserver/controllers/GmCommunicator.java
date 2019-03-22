package com.pwse.communicationserver.controllers;


import com.pwse.communicationserver.models.exceptions.GmConnectionFailedException;
import com.pwse.communicationserver.models.exceptions.ReadMessageErrorException;
import com.pwse.communicationserver.models.exceptions.SendMessageErrorException;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class GmCommunicator {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private int port;

	private ServerSocket serverSocket;
	private Socket gmSocket;
	private DataInputStream reader;
	private DataOutputStream writer;



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
			reader = new DataInputStream(gmSocket.getInputStream());
			writer = new DataOutputStream(gmSocket.getOutputStream());

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

	public String getMessage() throws ReadMessageErrorException {
		System.out.println(TAG + "getting message");
		String msg = null;

		try {
			msg = reader.readUTF();

		} catch (IOException e) {
			System.err.println(TAG + e.getMessage());
			throw new ReadMessageErrorException();
		}

		System.out.println(TAG + "got the message");
		return msg;
	}

	public boolean isMessageWaiting() {
		boolean isReady = false;

		try {
			isReady = (reader.available() != 0);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		return isReady;
	}

	public void sendMessage(JSONObject json) throws SendMessageErrorException {
		try {
			writer.writeUTF(json.toString());
		} catch (IOException e) {
			throw new SendMessageErrorException();
		}
	}
}

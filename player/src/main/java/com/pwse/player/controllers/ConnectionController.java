package com.pwse.player.controllers;

import com.pwse.player.models.ConnectionData;
import com.pwse.player.models.Exceptions.CloseConnectionFailException;
import com.pwse.player.models.Exceptions.OpenConnectionFailException;

import java.io.IOException;
import java.net.Socket;


/**
 * This class handles all connection between CS and Player (this module, not class)
 */
public class ConnectionController {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private ConnectionData cData;

	private Socket socket;



	public ConnectionController(ConnectionData cData) {
		this.cData = cData;
	}

	public void connect() throws OpenConnectionFailException {
		System.out.println(TAG + "opening connection with: " + cData.getAddress() + " at port: " + cData.getPort());

		try {
			socket = new Socket(cData.getAddress(), cData.getPort());

		} catch (IOException e) {
			System.err.println(TAG + "failed to open connection with: " + cData.getAddress() + " at port: " + cData.getPort());
			e.printStackTrace();
			throw new OpenConnectionFailException();
		}
	}

	public void close() throws CloseConnectionFailException {
		System.out.println(TAG + "closing connection with: " + cData.getAddress() + " at port: " + cData.getPort());

		try {
			socket.close();

		} catch (Exception e) {
			System.err.println(TAG + "failed to close connection with: " + cData.getAddress() + " at port: " + cData.getPort());
			e.printStackTrace();
			throw new CloseConnectionFailException();
		}
	}

	public void sendMessage(String msg) {
		//TODO: messages
		//TODO: throw different errors if occur
		System.out.println(TAG + "sending message: " + msg);
	}
}

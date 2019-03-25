package com.pwse.gamemaster.controllers;

import com.pwse.gamemaster.models.ConnectionData;
import com.pwse.gamemaster.models.exceptions.CloseConnectionFailException;
import com.pwse.gamemaster.models.exceptions.OpenConnectionFailException;
import com.pwse.gamemaster.models.exceptions.ReadMessageErrorException;
import com.pwse.gamemaster.models.exceptions.SendMessageErrorException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * This class handles all connection between CS and Player (this module, not class)
 */
public class ConnectionController {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private ConnectionData cData;

	private Socket socket;
	private DataInputStream reader;
	private DataOutputStream writer;



	public ConnectionController(ConnectionData cData) {
		this.cData = cData;
	}

	public void connect() throws OpenConnectionFailException {
		System.out.println(TAG + "opening connection with: " + cData.getAddress() + " at port: " + cData.getPort());

		try {
			socket = new Socket(cData.getAddress(), cData.getPort());
			reader = new DataInputStream(socket.getInputStream());
			writer = new DataOutputStream(socket.getOutputStream());

		} catch (IOException e) {
			System.err.println(TAG + "failed to open connection with: " + cData.getAddress() + " at port: " + cData.getPort());
			e.printStackTrace();
			throw new OpenConnectionFailException();
		}

		System.out.println(TAG + "connection opened");
	}

	public void disconnect() throws CloseConnectionFailException{
		System.out.println(TAG + "closing connection with: " + cData.getAddress() + " at port: " + cData.getPort());

		try {
			socket.close();

		} catch (Exception e) {
			System.err.println(TAG + "failed to disconnect connection with: " + cData.getAddress() + " at port: " + cData.getPort());
			e.printStackTrace();
			throw new CloseConnectionFailException();
		}
	}

	public JSONObject getMessage() throws ReadMessageErrorException {
		String msg = null;

		try {
			msg = reader.readUTF();
		} catch (IOException e) {
			throw new ReadMessageErrorException();
		}

		return new JSONObject(msg);
	}

	public void sendMessage(JSONObject json) throws SendMessageErrorException {
		System.out.println(TAG + "sending message");

		try {
			writer.writeUTF(json.toString());

		} catch (IOException e) {
			System.err.println(TAG + e.getMessage());
			throw new SendMessageErrorException();
		}

		System.out.println(TAG + "message sent");
	}
}

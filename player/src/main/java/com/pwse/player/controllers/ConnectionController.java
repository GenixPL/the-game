package com.pwse.player.controllers;

import com.pwse.player.models.ConnectionData;
import com.pwse.player.models.Exceptions.CloseConnectionFailException;
import com.pwse.player.models.Exceptions.OpenConnectionFailException;
import com.pwse.player.models.Exceptions.ReadMessageErrorException;
import com.pwse.player.models.Exceptions.SendMessageErrorException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * This class handles all connection between CS and Player (this module, not class)
 */
public class ConnectionController {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private ConnectionData cData;

	private Socket socket;
	private DataInputStream reader;
	private DataOutputStream writer;
	private String lastAction = "lack";
	private boolean isPieceInCurrentPlace;
	private boolean isGoalInCurrentPlace;



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

	public void disconnect() throws CloseConnectionFailException {
		System.out.println(TAG + "closing connection with: " + cData.getAddress() + " at port: " + cData.getPort());

		try {
			socket.close();
			reader.close();
			writer.close();

		} catch (Exception e) {
			System.err.println(TAG + "failed to disconnect connection with: " + cData.getAddress() + " at port: " + cData.getPort());
			e.printStackTrace();
			throw new CloseConnectionFailException();
		}
	}

	public JSONObject getMessage() throws ReadMessageErrorException {
		System.out.println(TAG + "receiving message");
		String msg = null;
		JSONObject json = null;

		while (json == null) {
			try {
				msg = reader.readUTF();
			} catch (IOException e) {
				throw new ReadMessageErrorException();
			}

			json = new JSONObject(msg);
		}

		System.out.println(TAG + "message received: " + json.toString());
		markPieceAndGoal(json);

		return json;
	}

	public void sendMessage(JSONObject json) {
		System.out.println(TAG + "sending message: " + json.toString());

		try {
			writer.writeUTF(json.toString());
			lastAction = json.getString("action");

		} catch (IOException e) {
			System.err.println(TAG + e.getMessage());
		}

		System.out.println(TAG + "message sent");
	}

	public String getLastAction() {
		return lastAction;
	}

	public boolean isPieceInCurrentPlace() {
		return isPieceInCurrentPlace;
	}

	public boolean isGoalInCurrentPlace() {
		return isGoalInCurrentPlace;
	}



	private void markPieceAndGoal(JSONObject json) {
		isGoalInCurrentPlace = false;
		isPieceInCurrentPlace = false;

		if (!json.getString("action").equals("move")) {
			return;
		}

		if (!json.getBoolean("approved")) {
			//otherwise field array is empty and program crashes
			return;
		}

		JSONObject fieldInfo = json.getJSONObject("field");
		JSONArray array = fieldInfo.getJSONArray("field");
		for (int i = 0; i < array.length(); i++) {
			if (array.getString(i).equals("goal")) {
				isGoalInCurrentPlace = true;
			}

			if (array.getString(i).equals("piece")) {
				isPieceInCurrentPlace = true;
			}
		}
	}
}

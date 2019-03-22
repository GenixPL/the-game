package com.pwse.communicationserver.controllers;

import com.pwse.communicationserver.models.PlayerContact;
import com.pwse.communicationserver.models.exceptions.PlConnectionFailedException;
import com.pwse.communicationserver.models.exceptions.SendMessageErrorException;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;



public class PlCommunicator {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private ArrayList<PlayerContact> playersContacts;


	public PlCommunicator(int[] portsToConnect) {
		this.playersContacts = new ArrayList<>(portsToConnect.length);

		for (int i = 0; i < portsToConnect.length; i++) {
			playersContacts.add(new PlayerContact(i, portsToConnect[i]));
		}
	}



	public void openSockets() throws PlConnectionFailedException {
		for (PlayerContact plC : playersContacts) {
			try {
				System.out.println(TAG + "opening port: " + plC.getPort() + " for player with id: " + plC.getId());
				plC.setServerSocket(new ServerSocket(plC.getPort()));

			} catch (IOException e) {
				throw new PlConnectionFailedException();
			}
		}
	}

	public void closeSockets() throws PlConnectionFailedException {
		for (PlayerContact plC : playersContacts) {
			try {
				System.out.println(TAG + "closing port: " + plC.getPort() + " for player with id: " + plC.getId());
				plC.getServerSocket().close();

			} catch (IOException e) {
				throw new PlConnectionFailedException();
			}
		}
	}

	public void connect() throws PlConnectionFailedException {
		for (PlayerContact plC : playersContacts) {
			try {
				System.out.println(TAG + "connecting player with id: " + plC.getId());
				plC.setPlayerSocket(plC.getServerSocket().accept());
				plC.setReader(new DataInputStream(plC.getPlayerSocket().getInputStream()));
				plC.setWriter(new DataOutputStream(plC.getPlayerSocket().getOutputStream()));

			} catch (IOException e) {
				throw new PlConnectionFailedException();
			}
		}
	}

	public void disconnect() throws PlConnectionFailedException {
		for (PlayerContact plC : playersContacts) {
			try {
				System.out.println(TAG + "disconnecting player with id: " + plC.getId());
				plC.getPlayerSocket().close();
				plC.getReader().close();
				plC.getWriter().close();

			} catch (IOException e) {
				throw new PlConnectionFailedException();

			}
		}
	}

	public void sendToAll(JSONObject json) throws SendMessageErrorException {
		System.out.println(TAG + "sending message to all pl");

		try {
			for (PlayerContact plC : playersContacts) {
			plC.getWriter().writeUTF(json.toString());
		}

		} catch (IOException e) {
			System.err.println(TAG + e.getMessage());
			throw new SendMessageErrorException();
		}

		System.out.println(TAG + "messages sent");
	}
}

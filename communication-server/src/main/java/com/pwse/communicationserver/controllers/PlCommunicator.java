package com.pwse.communicationserver.controllers;

import com.pwse.communicationserver.models.PlayerContact;
import com.pwse.communicationserver.models.exceptions.PlConnectionFailedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.util.ArrayList;



public class PlCommunicator {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private ArrayList<PlayerContact> playersContacts;
	private ArrayList<BufferedReader> readers; //TODO
	private ArrayList<OutputStreamWriter> writers; //TODO


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

			} catch (IOException e) {
				throw new PlConnectionFailedException();
			}
		}
	}

}

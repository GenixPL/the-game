package com.pwse.communicationserver.models;

import java.net.ServerSocket;
import java.net.Socket;

public class PlayerContact {

	private int id;
	private int port;
	private ServerSocket serverSocket;
	private Socket playerSocket;



	public PlayerContact(int id, int port) {
		this.id = id;
		this.port = port;
	}

	public int getId() {
		return id;
	}

	public int getPort() {
		return port;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void setPlayerSocket(Socket playerSocket) {
		this.playerSocket = playerSocket;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public Socket getPlayerSocket() {
		return playerSocket;
	}
}

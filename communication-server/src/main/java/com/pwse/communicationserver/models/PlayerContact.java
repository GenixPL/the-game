package com.pwse.communicationserver.models;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PlayerContact {

	private int id;
	private String teamColor; //TODO
	private int port;
	private ServerSocket serverSocket;
	private Socket playerSocket;
	private DataInputStream reader;
	private DataOutputStream writer;



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

	public void setReader(DataInputStream reader) {
		this.reader = reader;
	}

	public void setWriter(DataOutputStream writer) {
		this.writer = writer;
	}

	public DataInputStream getReader() {
		return reader;
	}

	public DataOutputStream getWriter() {
		return writer;
	}
}

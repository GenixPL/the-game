package com.pwse.gamemaster.models;


/**
 * This class encapsulates connection data (no fckn way)
 */
public class ConnectionData {
	private int port;
	private String address;



	public ConnectionData(int port, String address) {
		this.port = port;
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public String getAddress() {
		return address;
	}
}

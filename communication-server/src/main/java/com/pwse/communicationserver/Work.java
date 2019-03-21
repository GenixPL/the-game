package com.pwse.communicationserver;

import com.pwse.communicationserver.controllers.GmCommunicator;
import com.pwse.communicationserver.controllers.PlCommunicator;
import com.pwse.communicationserver.models.exceptions.GmConnectionFailedException;
import com.pwse.communicationserver.models.exceptions.PlConnectionFailedException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Work {

	private final String TAG = this.getClass().getSimpleName() + ": ";

	private GmCommunicator gmCommunicator;
	private PlCommunicator plCommunicator;



	Work(int gmPort, int[] plPorts) {
		this.gmCommunicator = new GmCommunicator(gmPort);
		this.plCommunicator = new PlCommunicator(plPorts);
	}

	public void run() {
		start();
	}



	private void start() {
		try {
			System.out.println(TAG + "connecting with gm");
			gmCommunicator.openSocket();
			gmCommunicator.connect();

		} catch (GmConnectionFailedException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		try {
			System.out.println(TAG + "connecting with pl");
			plCommunicator.openSockets();
			plCommunicator.connect();

		} catch (PlConnectionFailedException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		doWork();
	}

	private void stop() {
		try {
			System.out.println(TAG + "disconnecting with gm");
			gmCommunicator.disconnect();
			gmCommunicator.closeSocket();

		} catch (GmConnectionFailedException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		try {
			System.out.println(TAG + "disconnecting with pl");
			plCommunicator.disconnect();
			plCommunicator.closeSockets();

		} catch (PlConnectionFailedException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}

	private void doWork() {
		System.out.println(TAG + "starting message passing");

		while (true) { //TODO: exit when proper message from gm comes

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

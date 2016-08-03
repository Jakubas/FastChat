package client;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientInputListener extends Thread {

	private BufferedReader in;
	public volatile boolean listening = true;
	
	public ClientInputListener(BufferedReader in) {
		this.in = in;
	}

	public void run() {
		String fromServer;
		while (listening) {
			try {
				if ((fromServer = in.readLine()) != null) {
					System.out.println("Server: " + fromServer);
				}
			} catch (IOException e) {
	        	System.err.println("I/O exception for Client Input Listener, Contact Developer");
	            System.exit(1);
			}
		}
	}
}

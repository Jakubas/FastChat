package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServerLauncher {

	public static Hashtable<Integer,Client> clientTable;
	
	public void main(String[] args) throws IOException {
		if (args[0] == null)
			throw new NullPointerException();
		if (!Utils.isNumeric(args[0]))
			throw new IllegalArgumentException();
		
		int portNumber = Integer.parseInt(args[0]);
		
		ServerSocket serverSocket = null;
		boolean listeningSocket = true;
		
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			listeningSocket = false;
			System.err.println("Could not listen to port " + portNumber);
		}
		
		while (listeningSocket) {
			Socket clientSocket = serverSocket.accept();
			Client client = new Client(clientSocket);
			clientTable.put(client.getClientId(), client);
			client.start();
		}
	}
	
	public static Client getClientById(int id) {
		return clientTable.get(id);
	}
}

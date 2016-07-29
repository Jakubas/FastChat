package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServerLauncher {

	public static Hashtable<Integer,Client> clientTable = new Hashtable<Integer,Client>();
	
	public static void main(String[] args) throws IOException {
		
		if (args.length == 0) {
			System.err.println("Please enter a port number as an argument");
			System.exit(1);
		} else if (!Utils.isNumeric(args[0])) {
			System.err.println("Please use a number for the port");
			System.exit(1);
		}
		
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
			System.out.println("Awaiting connection on: " + InetAddress.getLocalHost().getHostAddress());
			Socket clientSocket = serverSocket.accept();
			
			Client client = new Client(clientSocket);
			System.out.println(clientSocket.getInetAddress().toString() + " has connected");
			clientTable.put(client.getClientId(), client);
			client.start();
		}
	}
	
	public static Client getClientById(int id) {
		return clientTable.get(id);
	}
}

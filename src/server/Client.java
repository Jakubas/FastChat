package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
public class Client extends Thread {

	
	private int id;
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	public Client(Socket clientSocket) {
		id = generateId();
		try (
		    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		){
			this.clientSocket = clientSocket;
			this.out = out;
			this.in = in;
		} catch (IOException e) {
			e.printStackTrace();
		}
		processInput();
	}
	
	public int generateId() {
	    Random random = new Random();
	    int minVal = 1000000;
	    //generates number between 1,000,000 and 9,999,999 (commas are seperators not decimal points)
	    return random.nextInt(10000000 - minVal) + minVal;
	}
	
	public int getClientId() {
		return id;
	}
	
	public Socket getSocket() {
		return clientSocket;
	}
	
	public PrintWriter getWriter() {
		return out;
	}
	
	public BufferedReader getReader() {
		return in;
	}
	
	public void writeToClient(String s) {
		out.write(s);
	}
	
	public String readFromClient() {
		try {
			return in.readLine();
		} catch (IOException e) {
			System.err.println("Couldn't read line");
		}
		return null;
	}
	
	private void processInput() {
		String inputLine;
		writeToClient("What would you like to do? Type /help for information about available commands");
		if ((inputLine = readFromClient()) != null) {
			if (inputLine.equals("/connect")) {
				connectClients();
			} else if (inputLine.equals("/help")) {
				provideInfo();
			}
		}
	}
	
	private void connectClients() {
		ClientConnector clientConnector = new ClientConnector(this);
	}
	
	private void provideInfo() {
		writeToClient("The available commands are:");
		writeToClient("/connect - starts the process for connecting with another user");
		writeToClient("/help - displays help about available commands");
	}
}

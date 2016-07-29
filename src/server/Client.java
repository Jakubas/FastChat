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
	private ClientConnection clientConnection;
	private ChatProtocol chatProtocol;
	
	public Client(Socket clientSocket) {
		this.id = generateId();
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
		
	}
	
	public void run() {
		//add check to see if client is still alive
		chatProtocol = new ChatProtocol(this);
		chatProtocol.processInput();
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
	
	public ClientConnection getClientConnection() {
		return clientConnection;
	}
	
	public ChatProtocol getChatProtocol() {
		return chatProtocol;
	}
	
	public void setClientConnection(ClientConnection clientConnection) {
		this.clientConnection = clientConnection;
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
}

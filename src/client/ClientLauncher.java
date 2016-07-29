package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientLauncher {
	
	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.err.println("Usage: java FastChatClient <host address> <port number> ");
			System.exit(1); 
		}
		
		String hostAddress = args[0];
		int portNumber = Integer.parseInt(args[1]);
        try (
    		Socket socket = new Socket(hostAddress, portNumber);
        	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ){
        	System.out.println("2");
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;
            System.out.println("3");
            while ((fromServer = in.readLine()) != null) {
            	System.out.println("4");
                System.out.println("Server: " + fromServer);
//                if (fromServer.equals("Bye."))
//                    break;
                 
                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("unknown host: " + hostAddress);
            System.exit(1);
        } catch (IOException e) {
        	System.err.println("I/O exception for the connection to " + hostAddress);
            System.exit(1);		
        }
	}
}

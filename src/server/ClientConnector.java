package server;

public class ClientConnector {

	private Client hostClient;
	private Client targetClient;
	
	public ClientConnector(Client hostClient) {
		this.hostClient = hostClient;
		this.targetClient = connectClients();
	}
	
	public Client getHostClient() {
		return hostClient;
	}
	
	public Client getTargetClient() {
		return targetClient;
	}
	
	private Client connectClients() {
		String inputLine;
		int targetId = recieveTargetId();
		
		Client targetClient = ServerLauncher.getClientById(targetId);
		targetClient.writeToClient(hostClient.getId() + " wishes to chat with you, "
				                   + "do you accept the chat request? (Y/N)");
		if ((inputLine = targetClient.readFromClient()) != null) {
			if (inputLine.matches("[Yy+](es)?")) {
				targetClient.writeToClient("Chat accepted");
				hostClient.writeToClient(targetClient.getId() + " accepted chat request");
				return targetClient;
			} else {
				targetClient.writeToClient("Chat declined");
				hostClient.writeToClient(targetClient.getId() + " declined chat request");
				return null;
			}
		}
		return null;
	}
	
	private int recieveTargetId() {
		String inputLine;
		int targetId = 0;
		
		hostClient.writeToClient("Please enter the id you wish to connect to.");
		
		if ((inputLine = hostClient.readFromClient()) != null) {
			if (Utils.isNumeric(inputLine)) {
				targetId = Integer.parseInt(inputLine);
			} else {
				hostClient.writeToClient("The id has to be a number, please re-enter the id you wish to connect to.");
				return recieveTargetId();
			}
		}
		return targetId;
	}
}

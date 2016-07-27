package server;

public class ClientConnection {

	private Client hostClient;
	private Client targetClient;
	
	public ClientConnection(Client hostClient) {
		this.hostClient = hostClient;
		this.targetClient = connectClients();
	}
	
	public ClientConnection(Client hostClient, Client targetClient) {
		this.hostClient = hostClient;
		this.targetClient = targetClient;
	}
	
	public Client getHostClient() {
		return hostClient;
	}
	
	public Client getTargetClient() {
		return targetClient;
	}
	
	private Client connectClients() {
		int targetId = recieveTargetId();
		Client targetClient = ServerLauncher.getClientById(targetId);
		if (targetClient == null) return null;
		if (establishConnection(targetClient)) {
			hostClient.setClientConnection(this);
			targetClient.setClientConnection(new ClientConnection(targetClient, hostClient));
			return targetClient;
		} else {
			return null;
		}
	}
	
	private Boolean establishConnection(Client targetClient) {
		String inputLine;
		targetClient.writeToClient(hostClient.getId() + " wishes to chat with you, "
								   + "do you accept the chat request? (Y/N)");
		if ((inputLine = targetClient.readFromClient()) != null) {
			if (inputLine.matches("[Yy+](es)?")) {
				targetClient.writeToClient("Chat accepted");
				hostClient.writeToClient(targetClient.getId() + " accepted chat request");
				return true;
			} else {
				targetClient.writeToClient("Chat declined");
				hostClient.writeToClient(targetClient.getId() + " declined chat request");
				return false;
			}
		}
		return false;
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

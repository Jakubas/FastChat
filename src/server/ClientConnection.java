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
		if (targetClient == null) {
			hostClient.writeToClient("Could not find client with id: " + targetId);
			return null;
		}
		
		targetClient.setState(ClientState.REPLYING);
		if (establishConnection(targetClient)) {
			hostClient.setClientConnection(this);
			targetClient.setClientConnection(new ClientConnection(targetClient, hostClient));
			targetClient.setState(ClientState.PROCESSING);
			return targetClient;
		} else {
			targetClient.setState(ClientState.PROCESSING);
			return null;
		}
	}
	
	private Boolean establishConnection(Client targetClient) {
		String inputLine;
		
		hostClient.writeToClient("Waiting for reply from " + targetClient.getClientId());
		targetClient.writeToClient(hostClient.getClientId() + " wishes to chat with you, "
								   + "do you accept the chat request? (Y/N)");

		while ((inputLine = targetClient.readFromClient()) != null) {
			if (inputLine.matches("[Yy+](es)?")) {
				targetClient.writeToClient("Chat accepted");
				hostClient.writeToClient(targetClient.getClientId() + " accepted chat request");
				return true;
			} else if (!inputLine.isEmpty()) {
				targetClient.writeToClient("Chat declined");
				hostClient.writeToClient(targetClient.getClientId() + " declined chat request");
				return false;
			}
		}
		return false;
	}
	
	private int recieveTargetId() {
		String inputLine;
		int targetId = 0;
		hostClient.writeToClient("Please enter the id of the client you wish to connect to.");
		
		while ((inputLine = hostClient.readFromClient()) != null) {
			if (Utils.isNumeric(inputLine)) {
				targetId = Integer.parseInt(inputLine);
				break;
			} else if (!inputLine.isEmpty()) {
				hostClient.writeToClient("The id has to be a number, please enter a correct id.");
			}
		}
		return targetId;
	}
}

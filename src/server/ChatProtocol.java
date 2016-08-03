package server;

public class ChatProtocol {
	
	public ClientState state = ClientState.PROCESSING;
	
	private Client client;
	
	public ChatProtocol(Client client) {
		this.client = client;
	}
	
	public void processInput() {
		String inputLine;
		client.writeToClient("What would you like to do? Type /help for information about available commands");
		
		while ((inputLine = client.readFromClient()) != null) {
			
			if (inputLine.equals("/connect")) {
				connectClients();
			} else if (inputLine.equals("/help")) {
				provideInfo();
			} else if (inputLine.equals("/chat") || state == ClientState.CHATTING) {
//				state = ClientState.CHATTING;
				openChatSession();
			} else if (inputLine.equals("/id")) {
				client.writeToClient(Integer.toString(client.getClientId()));
			} else if (state == ClientState.REPLYING) {
				while(state == ClientState.REPLYING) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						client.writeToClient("ChatProtocol: Interrupted exception in REPLYING state");
						System.err.println("ChatProtocol: Interrupted exception in REPLYING state");
					}
				}
				client.writeToClient(state.toString());
			} else if (!inputLine.isEmpty()) {
				client.writeToClient(state.toString());
			}
		}
	}
	
	private void connectClients() {
		new ClientConnection(client);
	}
	
	private void provideInfo() {
		client.writeToClient("The available commands are:");
		client.writeToClient("/connect - starts the process for connecting with another user");
		client.writeToClient("/chat - opens a chat session with the user you have connected to");
		client.writeToClient("/id - displays your id");
		client.writeToClient("/help - displays help about available commands");
	}
	
	private void openChatSession() {
		ClientConnection clientConnection = client.getClientConnection();
		if (clientConnection == null || clientConnection.getTargetClient() == null) {
			client.writeToClient("Chat session could not be opened. Please establish a connection "
					+ "with another user by using the /connect command.");
		} else {
			Client targetClient = clientConnection.getTargetClient();
			client.writeToClient("Chat session has been opened with " + targetClient.getClientId());
			//targetClient.writeToClient("Chat session has been opened with " + targetClient.getClientId());
			targetClient.setState(ClientState.CHATTING);
			manageChatSession();
			//targetClient.getChatProtocol().manageChatSession();
		}
	}
	
	public void manageChatSession() {
		Client targetClient = client.getClientConnection().getTargetClient();
		String inputLine;
		
		client.writeToClient("You are in a chat session? Type /help for information about available commands");
		while ((inputLine = client.readFromClient()) != null) {
//			client.writeToClient("Chatting sessions management");
			if (inputLine.equals("/close")) {
				targetClient.writeToClient("Other user has ended the chat session");
				client.setState(ClientState.PROCESSING);
				targetClient.setState(ClientState.PROCESSING);
				return;
			} else if (inputLine.equals("/help")) {
				client.writeToClient("You are in a chat session, the available commands are:");
				client.writeToClient("/close - close chat session");
				client.writeToClient("/help - show available commands");
			} else if (!inputLine.isEmpty()) {
				//write your message to target client
				targetClient.writeToClient(client.getClientId() + ": " + inputLine);
			}
		}
	}
}

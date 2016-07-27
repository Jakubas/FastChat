package server;

public class ChatProtocol {

	private Client client;
	
	public ChatProtocol(Client client) {
		this.client = client;
	}
	
	public void processInput() {
		String inputLine;
		client.writeToClient("What would you like to do? Type /help for information about available commands");
		if ((inputLine = client.readFromClient()) != null) {
			if (inputLine.equals("/connect")) {
				connectClients();
			} else if (inputLine.equals("/help")) {
				provideInfo();
			} else if (inputLine.equals("/chat")) {
				openChatSession();
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
			targetClient.writeToClient("Chat session has been opened with " + targetClient.getClientId());
			targetClient.getChatProtocol().manageChatSession();
			manageChatSession();
		}
	}
	
	public void manageChatSession() {
		Client targetClient = client.getClientConnection().getTargetClient();
		String inputLine;
		
		client.writeToClient("What would you like to do? Type /help for information about available commands");
		if ((inputLine = client.readFromClient()) != null) {
			if (inputLine.equals("/close")) {
				targetClient.writeToClient("Other user has ended the chat session");
				return;
			} else if (inputLine.equals("/help")) {
				client.writeToClient("You are in a chat session, the available commands are:");
				client.writeToClient("/close - close chat session");
				client.writeToClient("/help - show available commands");
			} else {
				targetClient.writeToClient(client.getClientId() + ": " + inputLine);
			}
		}
	}
}

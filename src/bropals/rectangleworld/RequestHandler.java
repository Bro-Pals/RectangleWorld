package bropals.rectangleworld;

import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;

/**
	Object that will hold a list of ClientConnection objects and listens
	for input from them, broadcasting any messages. Also handles the 
	special request of new clients.
*/
public class RequestHandler {
	
	private ArrayList<ClientConnection> clients;
	private ServerSocket server;
	
	public void RequestHandler(ServerSocket s) {
		this.clients = new ArrayList<>();
		this.server = s;
		nextId = 0;
	}
	
	public void handleRequest(String msg, int id) {
		broadcastToClients(msg, id);
	}
	
	/**
		Send msg to every client except the one with the given id number
	*/
	private void broadcastToClients(String msg, int idOfSender) {
		for (int i=0; i<clients.size(); i++) {
			if (clients.get(i).getId() != idOfSender) {
				clients.get(i).getOut().println(msg);
			}
		}
	}
	
	public void addClient(ClientConnection client) {
		clients.add(client);
		
		// send a copy of the world?
	}
}
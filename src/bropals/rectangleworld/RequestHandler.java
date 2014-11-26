package bropals.rectangleworld;
package bropals.rectangleworld.event.*;

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
	private GameWorld world;
	private GameEventParser parser;
	
	public RequestHandler(ServerSocket s, GameWorld w) {
		this.clients = new ArrayList<>();
		this.server = s;
		this.world = w;
		parser = new GameEventParser();
	}
	
	
	public void handleRequest(String msg, int id) {
		GameEvent event = parser.parseMessage(msg);
		// do whatever the message said to do

		// tell the clients to do the same thing
		broadcastToClients(msg, id);
	}
	
	/**
		Send msg to every client except the one with the given id number
		if the idOfSender is -1, then it's a message the server sent to itself
	*/
	private void broadcastToClients(String msg, int idOfSender) {
		for (int i=0; i<clients.size(); i++) {
			if (idOfSender == -1 || clients.get(i).getId() != idOfSender) {
				clients.get(i).getOut().println(msg);
			}
		}
	}
	
	public void addClient(ClientConnection client) {
		clients.add(client);
		
		// send a copy of the world?
		ArrayList<GameEntity> entities = world.getEntities();
		for (int i=0; i<entities.size(); i++) {
			if (entities.get(i) instanceof PlayerEntity) {
				
			}
		}
		
	}
}
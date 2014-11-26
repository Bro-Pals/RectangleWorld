package bropals.rectangleworld;

import bropals.rectangleworld.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.net.ServerSocket;
import java.net.Socket;

/**
	Object that will hold a list of ClientConnection objects and listens
	for input from them, broadcasting any messages. Also handles the 
	special request of new connections.
*/
public class RequestHandler {
	
	private ArrayList<ClientConnection> connections;
	private GameWorld world;
	
	public RequestHandler(GameWorld w) {
		this.connections = new ArrayList<ClientConnection();
		this.world = w;
	}
	
	
	public void handleRequest(String msg, int id) {
		GameEvent event = GameEventParser.parseMessage(msg);
		
		if (!(event instanceof IdAssignmentEvent)) {
			world.addEvent(event); // add it's own event
		}
		
		// tell the connections to do the same thing
		broadcastToClients(msg, id);
	}
	
	/**
		Send msg to every client except the one with the given id number
		if the idOfSender is -1, then it's a message the server sent to itself
	*/
	private void broadcastToClients(String msg, int idOfSender) {
		Iterator iterator = connections.iterator();
		while (iterator.hasNext()) {
			ClientConnection cc = (ClientConnection)iterator.next();
			if (idOfSender == -1 || cc.getID() != idOfSender) {
				cc.getOut().println(msg);
			}
		}
	}
	
	public void addClient(ClientConnection client) {
		connections.add(client);
		
		// send a copy of the world?
		
		ArrayList<GameEntity> entities = world.getEntities();
		System.out.println("Sending information on " + entities.size() + " entities");
		Iterator i = entities.iterator();
		while (i.hasNext()) {
			System.out.println("Sending information about " + i.toString());
			GameEntity ent = (GameEntity)i.next();
			if (ent instanceof PlayerEntity) {
				PlayerAddEvent pae = new PlayerAddEvent(System.currentTimeMillis(), ent.getID(), 
					ent.getX(), ent.getY(), ent.getWidth(), ent.getHeight(), ent.getColor(), 
					((PlayerEntity)ent).getName());
				client.getOut().println(GameEventParser.translateEvent(pae)); // send the message of the event
			} else {
				GameEvent eve = new EntityAddEvent(System.currentTimeMillis(), ent.getID(), 
					ent.getX(), ent.getY(), ent.getWidth(), ent.getHeight(), ent.getColor());
				client.getOut().println(GameEventParser.translateEvent(eve)); // send the message of the event
			}
		}
		
		String sendMePls = GameEventParser.translateEvent(new IdAssignmentEvent(System.currentTimeMillis(), client.getID()));
		client.getOut().println(sendMePls);
		System.out.println("I have sent output: " + sendMePls);
	}
	
	public void removeClient(int idNum) {
		ClientConnection connection = null;
		Iterator i = connections.iterator();
		while (i.hasNext()) {
			ClientConnection cc = (ClientConnection)i.next();
			if (cc.getID() == idNum) {
				connection = cc;
				break;
			}
		}
		if (connection != null) {
			connections.remove(connection);
			connection.stopSelf();
		} else {
			System.out.println("Could not find the client with the id number " + idNum + ": Failed removal");
		}
	}
	
	public int getNumberOfClients() {
		return connections.size();
	}
}
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
	special request of new clients.
*/
public class RequestHandler {
	
	private List<ClientConnection> clients;
	private ServerSocket server;
	private GameWorld world;
	
	public RequestHandler(ServerSocket s, GameWorld w) {
		this.clients = Collections.synchronizedList(new ArrayList<ClientConnection>());
		this.server = s;
		this.world = w;
	}
	
	
	public void handleRequest(String msg, int id) {
		GameEvent event = GameEventParser.parseMessage(msg);
		
		if (!(event instanceof IdAssignmentEvent)) {
			world.addEvent(event); // add it's own event
		}
		
		// tell the clients to do the same thing
		broadcastToClients(msg, id);
	}
	
	/**
		Send msg to every client except the one with the given id number
		if the idOfSender is -1, then it's a message the server sent to itself
	*/
	private void broadcastToClients(String msg, int idOfSender) {
		synchronized (clients) {
			Iterator iterator = clients.iterator();
			while (iterator.hasNext()) {
				ClientConnection cc = (ClientConnection)iterator.next();
				if (idOfSender == -1 || cc.getID() != idOfSender) {
					cc.getOut().println(msg);
				}
			}
		}
	}
	
	public void addClient(ClientConnection client) {
		clients.add(client);
		
		// send a copy of the world?
		List<GameEntity> entities = world.getEntities();
		synchronized (entities) {
			Iterator i = entities.iterator();
			while (i.hasNext()) {
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
		}
		
		client.getOut().println(GameEventParser.translateEvent(new IdAssignmentEvent(System.currentTimeMillis(), client.getID())));
	}
}
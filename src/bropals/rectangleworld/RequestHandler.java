package bropals.rectangleworld;

import bropals.rectangleworld.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
	Object that will hold a list of ClientConnection objects and listens
	for input from them, broadcasting any messages. Also handles the 
	special request of new connections.
*/
public class RequestHandler extends Thread {
	
	private ArrayList<ClientConnection> connections;
	private GameWorld world;
	private ServerDialog serverDialog;
	private ServerSocket server;
	
	public RequestHandler(GameWorld w, ServerDialog sd, ServerSocket ss) {
		this.connections = new ArrayList<ClientConnection>();
		this.world = w;
		this.serverDialog = sd;
		this.server = ss;
	}
	
	@Override
	public void run() {
		//Socket nextSocket;
		while (serverDialog.isRunning()) {
			try {
				serverDialog.print("Waiting for another client");
				Socket nextSocket = server.accept();
				ClientConnection connection = new ClientConnection(
					nextSocket, 
					new BufferedReader(new InputStreamReader(nextSocket.getInputStream())),
					new PrintWriter(nextSocket.getOutputStream(), true),
					RectangleWorldServer.getNewId(), 
					this
				);	
				addClient(connection);
				connection.start();
				serverDialog.print("Accepted connection from " + nextSocket.getInetAddress().toString() + 
					"(now have " + this.getNumberOfClients() + " clients)");
			} catch(SocketException socketException) {
				System.out.println("Server got shutdown: " + socketException.toString());
				/* Need to shutdown all clients here */
				
			} catch(Exception e) {
				System.out.println("Error in main class: " + e.toString());
			}
		}
		serverDialog.dispose();
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
		System.out.println("Broadcasted to " + connections.size() + " clients");
	}

	/**
		Sends information about the world to the client and also gives it
		it's ID number
	*/
	public void addClient(ClientConnection client) {
		connections.add(client);

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
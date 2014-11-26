package bropals.rectangleworld;

import java.io.BufferedReader;
import java.io.PrintWriter;
import bropals.rectangleworld.event.*;

/**
	Takes in GameEvents from the server and sends it to the GameWorld.
	Also sets the Client's player entity class
*/
public class ClientEventWatcher implements Runnable {
	
	private BufferedReader in; // the reader it's listening for events from
	private GameWorld world; // the world it's sending events to
	private PrintWriter out;
	private RectangleWorldClient daMainClass; // so it can tell it to set it's special player
	
	public ClientEventWatcher(GameWorld world, BufferedReader in, PrintWriter out, RectangleWorldClient rwc) {
		this.in=in;
		this.out=out;
		this.daMainClass = rwc;
		this.world=world;
	}
	
	@Override
	public void run() {
		try {
			String input = null;
			System.out.println("Waiting for server input...");
			while((input = in.readLine()) != null) {
				System.out.println("Input: " + input);
				GameEvent event = GameEventParser.parseMessage(input);
				if (event != null) {
					if (event instanceof IdAssignmentEvent) {
						System.out.println("I have gotten an IdAssignmentEvent");
						daMainClass.makePlayerWithId(((IdAssignmentEvent)event).getID()); // we now have a player!
					} else {
						System.out.println("I have gotten an event");
						world.addEvent(event); // the world will handle other events
					}
				}
			}
			System.out.println("Stopped recieving input from server, it probably closed out.");
		} catch(Exception e) {
			System.out.println("Error in ClientEventWatcher: " + e.toString());
			e.printStackTrace();
		}
	}
}
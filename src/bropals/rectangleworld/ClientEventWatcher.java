package bropals.rectangleworld;

import java.io.BufferedReader;
import java.io.PrintWriter;
import bropals.rectangleworld.event.*;

/**
	Takes in GameEvents from the server and sends it to the GameWorld.
	Also sets the Client's player entity class
*/
public class ClientEventWatcher extends Thread {
	
	private BufferedReader in; // the reader it's listening for events from
	private GameWorld world; // the world it's sending events to
	private PrintWriter out;
	
	public ClientEventWatcher(GameWorld w, BufferedReader i, PrintWriter o) {
		this.in=i;
		this.out=o;
		this.world=w;
	}
	
	@Override
	public void run() {
		try {
			String input = null;
			System.out.println("Waiting for server input...");
			while((input = in.readLine()) != null) {
				System.out.println("LOOP : ClientEventWatcher:31");
				System.out.println("Input: " + input);
				GameEvent event = GameEventParser.parseMessage(input);
				if (event != null) {
					if (event instanceof IdAssignmentEvent) {
						System.out.println("I have gotten an IdAssignmentEvent!!");
						RectangleWorldClient.makePlayerWithId(((IdAssignmentEvent)event).getID()); // we now have a player!
					} else {
						System.out.println("I have gotten an event");
						world.addEvent(event); // the world will handle other events it got
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
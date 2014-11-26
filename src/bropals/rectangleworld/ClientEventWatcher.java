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
	private PlayerEntity player; // the player it will tell to change it's Id number when it gets a JoinEvent
	private PrintWriter out;
	
	public ClientEventWatcher(GameWorld world, BufferedReader in, PrintWriter out) {
		this.in=in;
		this.world=world;
		this.ourPlayer=null;
		this.out=out;
	}
	
	@Override
	public void run() {
		String input;
		while((input = in.readLine()) != null) {
			GameEvent event = GameEventParser.parseMessage(input);
			if (event != null) {
				if (event instanceof JoinEvent) {
					
				}
			}
		}
	}
}
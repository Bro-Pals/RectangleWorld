package bropals.rectangleworld;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import horsentp.simpledrawing.DrawWindow;
import bropals.rectangleworld.event.*;
import java.awt.Color;

public class RectangleWorldClient {

	public static final int SERVER_PORT = 18002;

	public static void main(String[] args) {
		final ClientConnectDialog dialog = new ClientConnectDialog();
		dialog.addGoButtonActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
					When the go button is pressed, connect the client to the server
					with the information provided.
				*/
				String playerName = dialog.getName();
				InetAddress address = null;
				try {
					address = dialog.getIPAddress();
 				} catch(UnknownHostException uhe) {
					JOptionPane.showMessageDialog(dialog, "Error with server address: " + uhe.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				dialog.setVisible(false);
				if (address!=null) {
					/*
						Continue creating the world. Error box popped up if the 
						address location is unknown (UnknownHostException)
					*/
					RectangleWorldClient client = null;
					try {
						client = new RectangleWorldClient(playerName, address);
					} catch(IOException ioe) {
						JOptionPane.showMessageDialog(dialog, "Error making client: " + ioe.toString(), "Error", JOptionPane.ERROR_MESSAGE);
						client = null;
					}
					if (client!=null) {
						System.out.println("Successfully established a connection with the server at " + address.toString());
						System.out.println();
						dialog.dispose(); //Don't need the dialog anymore.
						client.loop();
					} else {
						//Could not connect, open the dialog again
						dialog.setVisible(true);
					}
				}
			}
		});
		
		
	}
	
	private String playerName;
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private static GameWorld world;
	private float cameraX, cameraY;
	private int idOfPlayer;
	
	public RectangleWorldClient(String playerName, InetAddress address) throws IOException {
		this.playerName = playerName;
		socket = new Socket(address, SERVER_PORT);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream());
		cameraX = 0;
		cameraY = 0;
		idOfPlayer = -1; // -1 is when it's not set yet
	}
	
	public void loop() {
		DrawWindow window = new DrawWindow("RectangleWorld", 800, 600, false);
		Graphics g = window.getDrawGraphics();
		g.drawString("Waiting for world..", 100, 100);
		window.showBuffer(g);
		world = new GameWorld();
		
		ClientEventWatcher eventWatcher = new ClientEventWatcher(world, input, output, this);
		Thread eventWatcherThread = new Thread(eventWatcher);
		eventWatcherThread.run(); // start listening for events
		
		long before, delta;
		final long mpf = 20; //Milliseconds per frame
		
		/*
			The loop in question
		*/
		while (window.exists()) {
			before = System.currentTimeMillis();
			g = window.getDrawGraphics();
			List<GameEntity> entities = world.getEntities();
			synchronized (entities) { //What is a synchronized block? (need to do research)
				Iterator iterator;
				GameEntity next;
				//First update all the entities
				world.update();
				//Then draw all the entities
				iterator = entities.iterator();
				while (iterator.hasNext()) {
					next = (GameEntity)iterator.next();
					drawGameEntity(g, next);
				}
			}
			window.showBuffer(g);
			delta = System.currentTimeMillis();
			if (delta < mpf) {
				try { Thread.sleep(mpf - delta); } catch(Exception threade) {} // sleep
			}
		}
	}
	
	public void makePlayerWithId(int id) {
		if (idOfPlayer == -1) { // if the player id is NOT set yet
			idOfPlayer = id;
			PlayerAddEvent pae = new PlayerAddEvent(System.currentTimeMillis(), idOfPlayer, 
				150, 150, 50, 50, Color.RED, playerName);
			world.addEvent(pae);
			output.println(GameEventParser.translateEvent(pae)); // send it over to the serve
			System.out.println("I have created a player and told the server all about it");
		}
	}
	
	public void setCameraPosition(float x, float y) {
		cameraX = x;
		cameraY = y;
	}
	
	public void translateCamera(float x, float y) {
		cameraX += x;
		cameraY += y;
	}
	
	public void drawGameEntity(Graphics g, GameEntity ge) {
		g.setColor(ge.getColor());
		g.fillRect((int)(ge.getX()-cameraX),
			(int)(ge.getY()-cameraY),
			(int)ge.getWidth(),
			(int)ge.getHeight()
		);
	}
}
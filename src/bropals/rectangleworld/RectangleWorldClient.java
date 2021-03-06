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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class RectangleWorldClient {

	public static final int SERVER_PORT = 18002;

	public static void main(String[] args) {
		final ClientConnectDialog dialog = new ClientConnectDialog();
		while(dialog.waiting()) {
			try {
				Thread.sleep(5);
			} catch(Exception e) {
				
			}
		}
		initClient(dialog);
	}
	
	
	public static void initClient(final ClientConnectDialog dialog) {
		dialog.setVisible(false);
		if (dialog.validInputs()) {
			/*
				When the go button is pressed, connect the client to the server
				with the information provided.
			*/
			playerName = dialog.getName();
			
			InetAddress address = null;
			try {
				address = dialog.getIPAddress();
			} catch(UnknownHostException uhe) {
				JOptionPane.showMessageDialog(dialog, "Error with server address: " + uhe.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				dialog.dispose();
			}
			if (address!=null) {
				/*
					Continue creating the world. Error box popped up if the 
					address location is unknown (UnknownHostException)
				*/
				System.out.println("Attempting connection through address " + address.toString());
				eventWatcher = null;
				try {
					socket = new Socket(address, SERVER_PORT);
					input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					output = new PrintWriter(socket.getOutputStream(), true);
					world = new GameWorld();
					eventWatcher = new ClientEventWatcher(world, input, output);
					eventWatcher.start(); // start listening for events
				} catch(IOException ioe) {
					JOptionPane.showMessageDialog(dialog, "Error making client: " + ioe.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					eventWatcher = null;
				}
				if (eventWatcher!=null) {
					System.out.println("Successfully established a connection with the server at " + address.toString());
					System.out.println();
					// initial values
					cameraX = 0;
					cameraY = 0;
					idOfPlayer = -1; // -1 is when it's not set yet
					dialog.dispose(); //Don't need the dialog anymore.
					loop(); // start the game loop
				} else {
					dialog.dispose();
				}
			}
		} else {
			//Invalid input, tell them the error
			JOptionPane.showMessageDialog(dialog, "Invalid name: name must be 25 characters or less and it must not contain \"" + GameEventParser.SEPARATOR + "\"", "Sorry! Invalid name", JOptionPane.ERROR_MESSAGE);
			dialog.dispose();
		}
	}
	
	
	// pro-core object orientated programming
	private static Socket socket;
	private static BufferedReader input;
	private static PrintWriter output;
	
	private static String playerName;
	private static GameWorld world;
	private static float cameraX, cameraY;
	private static int idOfPlayer;
	private static DrawWindow window;
	private static ClientEventWatcher eventWatcher;
	
	public static void loop() {
		window = new DrawWindow("RectangleWorld - " + playerName, 800, 600, false);
		window.getRawFrame().setFocusable(true);
		window.getRawFrame().requestFocus();
		/* Have something happen if the user wants to close the window */
		window.getRawFrame().addWindowListener(new WindowAdapter() { 
			@Override
			public void windowClosing(WindowEvent we) {
				window.destroy();
				onWindowCloseRequest();
			}
		});
		Graphics g = window.getDrawGraphics();
		g.drawString("Waiting for world..", 100, 100);
		window.showBuffer(g);
		
		long before, delta;
		final long mpf = 20; //Milliseconds per frame
		
		/*
			The loop in question.
			This is an infinite loop at the client side of the client/server pair.
			
		*/
		while (window.exists()) {
			handleWindowInput();
			before = System.currentTimeMillis();
			g = window.getDrawGraphics();
			List<GameEntity> entities = world.getEntities();
			Iterator iterator;
			GameEntity next;
			//First update all the entities
			world.updateEvents();
			/*
				Update this copy of the world's entities, not allowing self generated events to come in
			*/
			GameEntity current;
			Iterator i = entities.iterator();
			while(i.hasNext()) {
				current = (GameEntity)i.next();
				current.update();
				if (current.getID() == idOfPlayer) {
					//Center this client's camera on the player
					setCameraPosition(current.getX()-400, current.getY()-300);
				}
			}
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 800, 600);
			//Then draw all the entities
			drawWorldBoundries(g, world);
			iterator = entities.iterator();
			while (iterator.hasNext()) {
				next = (GameEntity)iterator.next();
				drawGameEntity(g, next);
			}
			
			window.showBuffer(g);
			delta = System.currentTimeMillis()-before;
			if (delta < mpf) {
				try { Thread.sleep(mpf - delta); } catch(Exception threade) {} // sleep
			}
			
		}
	}
	
	public static void onWindowCloseRequest() {
		//Need to close connection here
		System.out.println("Quitting");
		try {
			System.out.println("Closing client I/O streams...");
			input.close();
			output.close();
			System.out.println("Closed the client I/O streams");
		} catch(IOException e) {
			System.out.println("Exception while closing the client I/O streams: " + e.toString());
		}
		try {
			System.out.println("Closing client socket...");
			socket.close();
			System.out.println("Closed the client socket");
		} catch(Exception e2) {
			System.out.println("Exception while closing the client socket : " + e2.toString());
		}
		window.destroy();
	}
	
	public static void makePlayerWithId(int id) {
		if (idOfPlayer == -1) { // if the player id is NOT set yet
			idOfPlayer = id;
			PlayerAddEvent pae = new PlayerAddEvent(System.currentTimeMillis(), idOfPlayer, 
				150, 150, 50, 50, Color.RED, playerName);
			localAddAndSendEvent(pae);
		}
	}
	
	public static void setCameraPosition(float x, float y) {
		cameraX = x;
		cameraY = y;
	}
	
	public static void translateCamera(float x, float y) {
		cameraX += x;
		cameraY += y;
	}
	
	private static void handleWindowInput() {
		//System.out.println("handling window input");
		KeyEvent k = null;
		MouseEvent m = null;;
		GameEntity plyEnt = world.getEntity(idOfPlayer); // at least it should be
		int playerSpeed = 2;
		
		while ((k = window.nextKeyPressedEvent()) != null) {
			//System.out.println(k.toString());
			if (k.getKeyCode() == KeyEvent.VK_ESCAPE) {
				onWindowCloseRequest();
			}
			// handle player starting to move
			if (plyEnt != null) {
				System.out.println("We found da player and are about to make him do things");
				switch (k.getKeyCode()) {
					case KeyEvent.VK_W:
						localAddAndSendEvent(new StartMoveEvent(System.currentTimeMillis(), plyEnt.getID(), 
								Direction.NORTH_SOUTH, -playerSpeed));
						break;
					case KeyEvent.VK_S:
						localAddAndSendEvent(new StartMoveEvent(System.currentTimeMillis(), plyEnt.getID(), 
								Direction.NORTH_SOUTH, playerSpeed));
						break;
					case KeyEvent.VK_A:
						localAddAndSendEvent(new StartMoveEvent(System.currentTimeMillis(), plyEnt.getID(), 
								Direction.EAST_WEST, -playerSpeed));
						break;
					case KeyEvent.VK_D:
						localAddAndSendEvent(new StartMoveEvent(System.currentTimeMillis(), plyEnt.getID(), 
								Direction.EAST_WEST, playerSpeed));
						break;
				}
			} else {
				System.out.println("Could not find the player");
			}
		}
		k = null;
		while ((k = window.nextKeyReleasedEvent()) != null) {
			// handle player stop moving
			if (plyEnt != null) {
				switch (k.getKeyCode()) {
					case KeyEvent.VK_W:
					case KeyEvent.VK_S:
						localAddAndSendEvent(new StopMoveEvent(System.currentTimeMillis(), plyEnt.getID(), 
								Direction.NORTH_SOUTH, plyEnt.getY()));
						break;
					case KeyEvent.VK_A:
					case KeyEvent.VK_D:
						localAddAndSendEvent(new StopMoveEvent(System.currentTimeMillis(), plyEnt.getID(), 
								Direction.EAST_WEST, plyEnt.getX()));
						break;
				}
			}
		}
		while ((m = window.nextMousePressedEvent()) != null) {
			System.out.println("Handled mouse pressed event");
		}
		m = null;
		while ((m = window.nextMouseReleasedEvent()) != null) {
			System.out.println("Handled mouse released event");

		}
		//System.out.println("All done with window events");
	}
	
	private static void localAddAndSendEvent(GameEvent e) {
		System.out.println("Sending event");
		world.addEvent(e);
		output.println(GameEventParser.translateEvent(e));
	}
	
	public static void drawWorldBoundries(Graphics g, GameWorld world) {
		g.setColor(Color.BLACK);
		g.drawLine( //Right boundry line
			(int)(world.getBoundryRight()-cameraX), 
			(int)(world.getBoundryTop()-cameraY), 
			(int)(world.getBoundryRight()-cameraX), 
			(int)(world.getBoundryBottom()-cameraY)
		);
		g.drawLine( //Left  boundry line
			(int)(world.getBoundryLeft()-cameraX), 
			(int)(world.getBoundryTop()-cameraY), 
			(int)(world.getBoundryLeft()-cameraX), 
			(int)(world.getBoundryBottom()-cameraY)
		);
		g.drawLine( //Top boundry line
			(int)(world.getBoundryLeft()-cameraX), 
			(int)(world.getBoundryTop()-cameraY), 
			(int)(world.getBoundryRight()-cameraX), 
			(int)(world.getBoundryTop()-cameraY)
		);
		g.drawLine( //Bottom boundry line
			(int)(world.getBoundryLeft()-cameraX), 
			(int)(world.getBoundryBottom()-cameraY), 
			(int)(world.getBoundryRight()-cameraX), 
			(int)(world.getBoundryBottom()-cameraY)
		);
	}
	
	public static void drawGameEntity(Graphics g, GameEntity ge) {
		g.setColor(ge.getColor());
		g.fillRect((int)(ge.getX()-cameraX),
			(int)(ge.getY()-cameraY),
			(int)ge.getWidth(),
			(int)ge.getHeight()
		);
		if (ge instanceof PlayerEntity) {
			PlayerEntity pe = (PlayerEntity)ge;
			g.setColor(Color.BLACK);
			g.drawString(
				pe.getName(),
				(int)(ge.getX()-cameraX),
				(int)(ge.getY()-cameraY)-10
			);
		}
	}
}
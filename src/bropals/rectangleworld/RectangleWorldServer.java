package bropals.rectangleworld;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.io.IOException;

public class RectangleWorldServer {

	public static final int SERVER_PORT = 18002;
	private static int lastId = 0; // keep track of the last ID number so it's always unique
	
	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			server = new ServerSocket(SERVER_PORT);
		} catch(IOException fsfs) {
			System.err.println("Unable to create server at socket " + SERVER_PORT + ": " + fsfs.toString());
			//Just stop the program
			return;
		}
		GameWorld world = new GameWorld(); // the server's copy of game world
		initWorld(world);
		RequestHandler requestHandler = new RequestHandler(world);
		ArrayList<Thread> threads = new ArrayList<>();
		
		boolean running = true;
		Socket nextSocket;
		while (running) {
			try {
				System.out.println("Waiting for another client");
				nextSocket = server.accept();
				ClientConnection connection = new ClientConnection(nextSocket, getNewId(), requestHandler);	
				Thread t = new Thread(connection);
				threads.add(t);
				t.start();
				requestHandler.addClient(connection);
				System.out.println("Accepted connection from " + nextSocket.getInetAddress().toString() + 
							"(now have " + requestHandler.getNumberOfClients() + " clients)");
			} catch(Exception e) {
				System.out.println("Error in main class: " + e.toString());
				running = false;
			}
		}
		try {
			server.close();
		} catch(IOException fsfs2) {
			System.err.println("Unable to close the server at socket " + SERVER_PORT + ": " + fsfs2.toString());
		}
	}
	
	public static int getNewId() {
		lastId++;
		return lastId;
	}
	
	public static void initWorld(GameWorld world) {
		//Initial entity placement
	}
}
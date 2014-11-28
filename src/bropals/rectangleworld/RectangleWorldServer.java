package bropals.rectangleworld;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.io.IOException;

public class RectangleWorldServer {

	public static final int SERVER_PORT = 18002;
	private static int lastId = 0; // keep track of the last ID number so it's always unique
	
	public static void main(String[] args) {
		ServerSocket server = null;
		ServerDialog serverDialog = null;
		try {
			server = new ServerSocket(SERVER_PORT);
			server.setReuseAddress(true);
			serverDialog = new ServerDialog(server);
		} catch(IOException fsfs) {
			System.err.println("Unable to create server at socket " + SERVER_PORT + ": " + fsfs.toString());
			//Just stop the program
			return;
		}
		GameWorld world = new GameWorld(); // the server's copy of game world
		initWorld(world);
		RequestHandler requestHandler = new RequestHandler(world, serverDialog, server);
		requestHandler.start();
		
	}
	
	public static int getNewId() {
		lastId++;
		return lastId;
	}
	
	public static void initWorld(GameWorld world) {
		//Initial entity placement
	}
}
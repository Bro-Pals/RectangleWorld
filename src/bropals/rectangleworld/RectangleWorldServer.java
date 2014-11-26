package bropals.rectangleworld;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

public class RectangleWorldServer {

	public static final int SERVER_PORT = 18002;

	public static void main(String[] args) {
		
		ServerSocket server = new ServerSocket(SERVER_PORT);
		RequestHandler requestHandler = new RequestHandler(server);
		ArrayList<Thread> threads = new ArrayList<>();
		
		int nextId = 0;
		
		boolean running = true;
		while (running) {
			try {
				Socket nextSocket = server.accept();
				ClientConnection connection = new ClientConnection(nextSocket, nextId, requestHandler)
				requestHandler.addClient(connection);
				Thread newThread = new Thread(connection);
				newThread.run();
				threads.add(newThread);
			} catch(Exception e) {
				System.out.println("Error in main class: " + e.toString());
				running = false;
			}
		}
		
		server.close();
	}
}
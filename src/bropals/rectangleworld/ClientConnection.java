package bropals.rectangleworld;;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
/**
	An object that represents a connected client. The server
	will hold an ArrayList of these to keep track of things.
*/
public class ClientConnection implements Runnable {

	private RequestHandler handler;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private int id;
	
	public ClientConnection(Socket s, int idNum, handler) {
		this.id = idNum;
		try {
			this.socket = s;
			out = new PrintWriter(s.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch(Exception e) {
			System.out.println("Error construction a ClientConnection object: " + e.toString());
		}
	}
	
	@Override
	public void run() {
		String input;
		while (!socket.isClosed() && (input = in.readLine()) != null) {
			handler.handleRequest(input, id);
		}
	}
	
	public PrintWriter getOut() {
		return out;
	}
	
	public BufferedReader getIn() {
		return in;
	}
	
	public int getId() {
		return id;
	}
}
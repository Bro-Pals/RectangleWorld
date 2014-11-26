package bropals.rectangleworld;;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
/**
	An object that represents a connected client. The server
	will hold an ArrayList of these to keep track of things.
*/
public class ClientConnection implements Runnable {

	private RequestHandler handler;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private Thread myThread;
	private int id;
	
	public ClientConnection(Socket s, int idNum, RequestHandler handler) {
		this.id = idNum;
		this.myThread = null;
		this.handler = handler;
		try {
			this.socket = s;
			out = new PrintWriter(s.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch(Exception e) {
			System.out.println("Error construction a ClientConnection object: " + e.toString());
		}
	}
	
	public void startListening(Thread onThis) {
		myThread = onThis;
		myThread.start();
	}
	
	@Override
	public void run() {
		System.out.println("I'm running!");
		String input = null;
		try {
			System.out.println("Waiting for client input...");
			while (!socket.isClosed() && (input = in.readLine()) != null) {
				handler.handleRequest(
				input, 
				id);
			}
			
		} catch(IOException e) {
			System.out.println("IO Error: " + e.toString());
			//Need to have the server disconnect this connection
		}
	}
	
	public void stopSelf() {
		try {
			out.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			System.out.println("Exception while client was stopping itself: " + e.toString());
		}
	}
	
	public PrintWriter getOut() {
		return out;
	}
	
	public BufferedReader getIn() {
		return in;
	}
	
	public int getID() {
		return id;
	}
}
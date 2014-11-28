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
public class ClientConnection extends Thread {

	private RequestHandler handler;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private int id;
	
	public ClientConnection(Socket s, BufferedReader in, PrintWriter out, int idNum, RequestHandler handler) throws IOException {
		this.id = idNum;
		this.handler = handler;
		this.socket = s;
		this.in = in;
		this.out = out;
	}
	
	@Override
	public void run() {
		try {
			String input = null;
			System.out.println("Server is waiting for client input...");
			while ((input = in.readLine()) != null) {
				System.out.println("LOOP : ClientConnection:36");
				System.out.println("Got input: "+input);
				handler.handleRequest(input, id);
			}
			
		} catch(IOException e) {
			System.out.println("IO Error: " + e.toString());
			//Need to have the server disconnect this connection
			stopSelf();
		}
		System.out.println("Client " + id + " has finished its run method");
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
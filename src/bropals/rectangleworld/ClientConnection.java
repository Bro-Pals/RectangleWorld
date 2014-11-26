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
	private int id;
	private Thread myThread;
	
	public ClientConnection(Socket s, int idNum, RequestHandler handler) throws IOException {
		this.id = idNum;
		this.handler = handler;
		this.socket = s;
		this.myThread = new Thread(this);
		out = new PrintWriter(s.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public void startThread() {
		myThread.start();
	}
	
	@Override
	public void run() {
		try {
			String input = null;
			System.out.println("Server is waiting for client input...");
			while ((input = in.readLine()) != null) {
				System.out.println("Got input: "+input);
				handler.handleRequest(input, id);
			}
			
		} catch(IOException e) {
			System.out.println("IO Error: " + e.toString());
			//Need to have the server disconnect this connection
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
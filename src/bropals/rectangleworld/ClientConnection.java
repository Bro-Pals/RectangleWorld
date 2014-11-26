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
		this.myThread = new Thread(this);
		try {
			this.socket = s;
			out = new PrintWriter(s.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch(Exception e) {
			System.out.println("Error construction a ClientConnection object: " + e.toString());
		}
	}
	
	public void startListening() {
		myThread.run();
	}
	
	@Override
	public void run() {
		String input;
		try {
			while (!socket.isClosed() && (input = in.readLine()) != null) {
				handler.handleRequest(input, id);
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
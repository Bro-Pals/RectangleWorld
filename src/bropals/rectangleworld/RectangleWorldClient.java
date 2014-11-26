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
				dialog.dispose();
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
					}
					if (client!=null) {
						System.out.println("Successfully established a connection with the server at " + address.toString());
						System.out.println();
						//Client established the connection properly
						
					}
				}
			}
		});
	}
	
	private String playerName;
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private GameWorld world;
	
	public RectangleWorldClient(String playerName, InetAddress address) throws IOException {
		this.playerName = playerName;
		socket = new Socket(address, SERVER_PORT);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream());
	}
	
	public void getWorldFromServer() {
		world = new GameWorld();
	}
}
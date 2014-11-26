package bropals.rectangleworld;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class RectangleWorldClient {

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
					JOptionPane.showMessageDialog(dialog, "Unable to connect to server: " + uhe.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				if (address!=null) {
					dialog.dispose();
					/*
						Continue creating the world. Error box popped up if the 
						address location is unknown (UnknownHostException)
					*/
					
				}
			}
		});
	}
	
	private String playerName;
	
	public RectangleWorldClient(String playerName, InetAddress address) {
		this.playerName = playerName;
	}
}
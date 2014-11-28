package bropals.rectangleworld;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
	Stores the information about a client that is about to send a request to the server.
	This is mainly so 
*/
public class ClientConnectDialog extends JFrame {
	
	/**
		The text field that will store the name of the client's player.
	*/
	private JTextField name;
	/**
		The text field that will store the IP of the server's router that
		the client will try to connect to.
	*/
	private JTextField ip;
	/**
		The button that will launch the application.
	*/
	private JButton go;
	
	private boolean waiting;
	
	public ClientConnectDialog() {
		setTitle("Rectangle World: Join a server");
		setLayout(new GridLayout(3, 2, 20, 20));
		name = new JTextField(20);
		ip = new JTextField(20);
		go = new JButton("Enter Server");
		add(new JLabel("Player Name"));
		add(name);
		add(new JLabel("IP Address"));
		add(ip);
		add(go);
		setSize(350, 180);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		waiting = true;
		go.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				waiting = false;
			}
		});
	}
	
	/**
		Gets the name that is in the text box.
	*/
	public String getName() {
		return name.getText();
	}
	
	/**
		Get the address that is in the text box.
	*/
	public InetAddress getIPAddress() throws UnknownHostException {
		return InetAddress.getByName(ip.getText());
	}
	
	public boolean validInputs() {
		boolean valid = true;
		char[] array = name.getText().toCharArray();
		for (int i=0; i<array.length; i++) {
			if (array[i] == GameEventParser.SEPARATOR) {
				valid = false;
				break;
			}
		}
		return valid && array.length <= 25;
	}
	
	public boolean waiting() {
		return waiting;
	}
}
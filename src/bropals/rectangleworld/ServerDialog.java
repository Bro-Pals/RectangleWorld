package bropals.rectangleworld;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.net.ServerSocket;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.io.IOException;
import javax.swing.ScrollPaneConstants;

public class ServerDialog extends JFrame {
	
	private boolean running;
	private JTextArea area;
	private ServerSocket server;
	
	public ServerDialog(ServerSocket server) {
		this.server = server;
		setTitle("Rectangle World server");
		running = true;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				closeServer();
			}
		});
		area = new JTextArea();
		JScrollPane pane = new JScrollPane(area, 
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		area.setLineWrap(false);
		area.setEditable(false);
		add(pane);
		setSize(400, 300);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	public void closeServer() {
		try {
			server.close();
		} catch(IOException e) {
			System.err.println("ServerDialog: Unable to shutdown server");
		}
		running = false;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void print(String line) {
		area.append(line);
		area.append("\n");
	}
}
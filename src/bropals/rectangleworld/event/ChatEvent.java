package bropals.rectangleworld.event;

import bropals.rectangleworld.*;
import java.awt.Color;

public class ChatEvent extends GameEvent {
	
	private int id;
	private String text;
	
	public ChatEvent(long timeStamp, int id, String text) {
		setTimeStamp(timeStamp);
		this.id = id;
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public int getID() {
		return id;
	}
}
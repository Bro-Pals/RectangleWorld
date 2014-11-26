package bropals.rectangleworld.event;

import bropals.rectangleworld.*;
import java.awt.Color;

public class ChatEvent extends GameEvent {
	
	private String text;
	
	public ChatEvent(long timeStamp, int id, String text) {
		super(id);
		setTimeStamp(timeStamp);
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

}
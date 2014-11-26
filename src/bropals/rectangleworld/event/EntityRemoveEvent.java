package bropals.rectangleworld.event;

import bropals.rectangleworld.*;
import java.awt.Color;

public class EntityRemoveEvent extends GameEvent {
	
	private int id;
	
	public EntityRemoveEvent(long timeStamp, int id) {
		setTimeStamp(timeStamp);
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
}
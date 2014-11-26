package bropals.rectangleworld.event;

import bropals.rectangleworld.*;
import java.awt.Color;

public class EntityRemoveEvent extends GameEvent {
	
	public EntityRemoveEvent(long timeStamp, int id) {
		super(id);
		setTimeStamp(timeStamp);
	}

}
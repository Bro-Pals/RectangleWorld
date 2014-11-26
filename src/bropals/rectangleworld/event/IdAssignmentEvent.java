package bropals.rectangleworld.event;

import bropals.rectangleworld.*;

public class IdAssignmentEvent extends GameEvent {

	public IdAssignmentEvent(long timeStamp, int id) {
		super(id);
		setTimeStamp(timeStamp);
	}

}
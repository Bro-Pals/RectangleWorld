package bropals.rectangleworld.event;

import bropals.rectangleworld.*;

public class IdAssignmentEvent extends GameEvent {

	private int id; // the id that the client will change its player's id number to

	public IdAssignmentEvent(long timeStamp, int id) {
		setTimeStamp(timeStamp);
		this.id=id;
	}
	
	public int getId() {
		return id;
	}
}
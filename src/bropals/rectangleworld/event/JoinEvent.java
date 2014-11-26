package bropals.rectangleworld.event;

import bropals.rectangleworld.*;

public class JoinEvent extends GameEvent {

	private int id; // the id that the client will change its player's id number to

	public JoinEvent(long timeStamp, int id) {
		setTimeStamp(timeStamp);
		this.id=id;
	}
	
	public int getId() {
		return id;
	}
}
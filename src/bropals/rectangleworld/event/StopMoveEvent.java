package bropals.rectangleworld.event;

import bropals.rectangleworld.*;

public class StopMoveEvent extends GameEvent {
	
	private int id;
	private Direction direction;
	private float posDir;
	
	public StopMoveEvent(long timeStamp, int id, Direction direction, float posDir) {
		setTimeStamp(timeStamp);
		this.id = id;
		this.posDir = posDir;
		this.direction = direction;
	}
	
	public float getDirectionalPosition() {
		return this.posDir;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public int getID() {
		return id;
	}
}
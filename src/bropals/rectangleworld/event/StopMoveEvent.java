package bropals.rectangleworld.event;

import bropals.rectangleworld.*;

public class StopMoveEvent extends GameEvent {
	
	private Direction direction;
	private float posDir;
	
	public StopMoveEvent(long timeStamp, int id, Direction direction, float posDir) {
		super(id);
		setTimeStamp(timeStamp);
		this.posDir = posDir;
		this.direction = direction;
	}
	
	public float getDirectionalPosition() {
		return this.posDir;
	}
	
	public Direction getDirection() {
		return direction;
	}

}
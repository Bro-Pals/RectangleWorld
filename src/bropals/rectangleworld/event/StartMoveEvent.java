package bropals.rectangleworld.event;

import bropals.rectangleworld.*;

public class StartMoveEvent extends GameEvent {
	
	private int id;
	private Direction direction;
	private int velocity;
	
	public StartMoveEvent(long timeStamp, int id, Direction direction, int velocity) {
		setTimeStamp(timeStamp);
		this.id = id;
		this.direction = direction;
		this.velocity = velocity;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public int getVelocity() {
		return velocity;
	}
	
	public int getID() {
		return id;
	}
}
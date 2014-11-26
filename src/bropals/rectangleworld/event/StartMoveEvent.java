package bropals.rectangleworld.event;

import bropals.rectangleworld.*;

public class StartMoveEvent extends GameEvent {
	
	private int id;
	private Direction direction;
	private float velocity;
	
	public StartMoveEvent(long timeStamp, int id, Direction direction, float velocity) {
		setTimeStamp(timeStamp);
		this.id = id;
		this.direction = direction;
		this.velocity = velocity;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public float getVelocity() {
		return velocity;
	}
	
	public int getID() {
		return id;
	}
}
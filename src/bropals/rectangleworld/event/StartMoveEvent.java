package bropals.rectangleworld.event;

import bropals.rectangleworld.*;

public class StartMoveEvent extends GameEvent {
	
	private Direction direction;
	private float velocity;
	
	public StartMoveEvent(long timeStamp, int id, Direction direction, float velocity) {
		super(id);
		setTimeStamp(timeStamp);
		this.direction = direction;
		this.velocity = velocity;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public float getVelocity() {
		return velocity;
	}

}
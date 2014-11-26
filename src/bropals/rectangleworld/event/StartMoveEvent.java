package bropals.rectangleworld.event;

import bropals.rectangleworld.*;

public class StartMoveEvent extends GameEvent {
	
	private int id;
	private Direction direction;
	private int velocity;
	
	public StartMoveEvent(int id, Direction direction, int velocity) {
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
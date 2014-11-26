package bropals.rectangleworld.event;

import bropals.rectangleworld.*;

public class StopMoveEvent extends GameEvent {
	
	private int id;
	private Direction direction;
	
	public StopMoveEvent(int id, Direction direction) {
		this.id = id;
		this.direction = direction;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public int getID() {
		return id;
	}
}
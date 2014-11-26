package bropals.rectangleworld.event;

import bropals.rectangleworld.*;
import java.awt.Color;

public class EntityAddEvent extends GameEvent {
	
	private int id;
	private Color color;
	private float posX, posY;
	private float width, height;
	
	public EntityAddEvent(long timeStamp, int id, float posX, float posY, float width, float height, Color color) {
		setTimeStamp(timeStamp);
		this.id = id;
		this.color = color;
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public Color getColor() {
		return color;
	}
	
	public float getPositionX() {
		return posX;
	}
	
	public float getPositionY() {
		return posY;
	}

	public int getID() {
		return id;
	}
}
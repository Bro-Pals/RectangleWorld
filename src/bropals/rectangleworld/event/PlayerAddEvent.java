package bropals.rectangleworld.event;

import bropals.rectangleworld.*;
import java.awt.Color;

public class PlayerAddEvent extends GameEvent {
	
	private Color color;
	private float posX, posY, width, height;
	private String name;
	
	public PlayerAddEvent(long timeStamp, int id, float posX, float posY, float width, float height, Color color, String name) {
		super(id);
		setTimeStamp(timeStamp);
		this.color = color;
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.name = name;
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
	
	public String getName() {
		return name;
	}
}
package bropals.rectangleworld.event;

import bropals.rectangleworld.*;
import java.awt.Color;

public class PlayerAddEvent extends GameEvent {
	
	private int id;
	private Color color;
	private float posX, posY;
	private String name;
	
	public PlayerAddEvent(int id, float posX, float posY, Color color, String name) {
		this.id = id;
		this.color = color;
		this.posX = posX;
		this.posY = posY;
		this.name = name;
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
	
	public String getName() {
		return name;
	}
}
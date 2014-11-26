package bropals.rectangleworld.event;

import bropals.rectangleworld.*;
import java.awt.Color;

public class ColorChangeEvent extends GameEvent {
	
	private int id;
	private Color color;
	
	public ColorChangeEvent(int id, Color color) {
		this.id = id;
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

	public int getID() {
		return id;
	}
}
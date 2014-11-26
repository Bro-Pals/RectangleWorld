package bropals.rectangleworld.event;

import bropals.rectangleworld.*;
import java.awt.Color;

public class ColorChangeEvent extends GameEvent {
	
	private Color color;
	
	public ColorChangeEvent(long timeStamp, int id, Color color) {
		super(id);
		setTimeStamp(timeStamp);
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

}
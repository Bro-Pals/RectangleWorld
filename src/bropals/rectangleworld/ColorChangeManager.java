package bropals.rectangleworld;

import java.awt.Color;

/**
	Manages a list of possible colors, can rotate between the possible colors.
*/
public class ColorChangeManager {
	
	private Color[] colors;
	private int current;
	
	public ColorChangeManager() {
		current = 0;
		colors = new Color[] {
			Color.BLACK,
			Color.BLUE,
			Color.CYAN,
			Color.DARK_GRAY,
			Color.GRAY,
			Color.LIGHT_GRAY,
			Color.MAGENTA,
			Color.ORANGE,
			Color.PINK,
			Color.RED,
			Color.WHITE,
			Color.YELLOW
		};
	}
	
	public Color nextColor() {
		Color color = colors[current];
		current++;
		if (current>=colors.length) {
			current = 0;
		}
		return color;
	}
}
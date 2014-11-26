package bropals.rectangleworld;

import bropals.rectangleworld.event.*;
import java.awt.Color;

public class GameEventParser {

	private static final int 
		START_MOVE_EVENT = 0,
		STOP_MOVE_EVENT = 1,
		ENTITY_ADD_EVENT = 2,
		ENTITY_REMOVE_EVENT = 3,
		COLOR_CHANGE_EVENT = 4,
		CHAT_EVENT = 5,
		JOIN_EVENT = 6,
		PLAYER_ADD_EVENT = 7;
	private static final String SEPARATOR = " ";

	public static GameEvent parseMessage(String msg) {
		String[] paramList = msg.split(SEPARATOR);
		long timeStamp = Long.parseLong(paramList[0]);
		int eventId = Integer.parseInt(paramList[1]);
		int id;
		Direction direction;
		Color color;
		float posX, posY, width, height;
		switch(eventId) {
			case START_MOVE_EVENT:
				id = Integer.parseInt(paramList[2]);
				direction = Direction.getDirectionByID(Ingeger.parseInt(paramList[3]));
				float vel = Float.parseFloat(paramList[4]);
				return new StartMoveEvent(timeStamp, id, direction, vel);
			case STOP_MOVE_EVENT:
				id = Integer.parseInt(paramList[2]);
				direction = Direction.getDirectionByID(Ingeger.parseInt(paramList[3]));
				float posDir = Integer.parseInt(paramList[4]);
				return new StopMoveEvent(timeStamp, id, direction, posDir);
			case ENTITY_ADD_EVENT:
				id = Integer.parseInt(paramList[2]);
				posX = Float.parseFloat(paramList[3]);
				posY = Float.parseFloat(paramList[4]);
				color = getColorFromID(Integer.parseInt(paramList[5]));
				width = Float.parseFloat(paramList[6]);
				height = Float.parseFloat(paramList[7]);
				return new EntityAddEvent(timeStamp, id, posX, posY, width, height, color);
			case  ENTITY_REMOVE_EVENT:
				id = Integer.parseInt(paramList[2]);
				return new EntityRemoveEvent(timeStamp, id);
			case COLOR_CHANGE_EVENT:
				id = Integer.parseInt(paramList[2]);
				color = getColorFromID(Integer.parseInt(paramList[3]));
				return new ColorChangeEvent(timeStamp, id, color);
			case CHAT_EVENT:
				id = Integer.parseInt(paramList[2]);
				String text = paramList[3];
				return new ChatEvent(timeStamp, id, text);
				break;
			case PLAYER_ADD_EVENT:
				id = Integer.parseInt(paramList[2]);
				posX = Float.parseFloat(paramList[3]);
				posY = Float.parseFloat(paramList[4]);
				Color color = getColorFromID(Integer.parseInt(paramList[5]));
				String name = paramList[6];
				width = Float.parseFloat(paramList[7]);
				height = Float.parseFloat(paramList[8]);
				return new PlayerAddEvent(timeStamp, id, posX, posY, width, height, color, name);
			case JOIN_EVENT:
				id = Integer.parseInt(paramList[2]);
				return new JoinEvent(timeStamp, id);
		}
		return null;
	}
	
	public static String translateEvent(GameEvent e) {
		if (e instanceof ChatEvent) {
			ChatEvent ce = (ChatEvent)e;
			return "" + ce.getTimeStamp()  + SEPARATOR + CHAT_EVENT + SEPARATOR + ce.getID() + SEPARATOR + ce.getText();
		} else if (e instanceof ColorChangeEvent) {
			ColorChangeEvent cce = (ColorChangeEvent)e;
			return "" + cce.getTimeStamp()  + SEPARATOR + COLOR_CHANGE_EVENT + SEPARATOR + cce.getID() + SEPARATOR + getColorID(cce.getColor());
		} else if (e instanceof EntityAddEvent) {
			EntityAddEvent eae = (EntityAddEvent)e;
			return "" + eae.getTimeStamp()  + SEPARATOR + ENTITY_ADD_EVENT + SEPARATOR + eae.getID() + SEPARATOR + eae.getPositionX() + SEPARATOR + eae.getPositionY() + SEPARATOR + getColorID(eae.getColor()) + SEPARATOR + eae.getWidth() + SEPARATOR + eae.getHeight();
		} else if (e instanceof EntityRemoveEvent) {
			EntityRemoveEvent ere = (EntityRemoveEvent)e;
			return "" + ere.getTimeStamp()  + SEPARATOR + ENTITY_REMOVE_EVENT + SEPARATOR + ere.getID();
		} else if (e instanceof PlayerAddEvent) {
			PlayerAddEvent eae = (PlayerAddEvent)e;
			return "" + eae.getTimeStamp()  + SEPARATOR + ENTITY_ADD_EVENT + SEPARATOR + eae.getID() + SEPARATOR + eae.getPositionX() + SEPARATOR + eae.getPositionY() + SEPARATOR + getColorID(eae.getColor()) + SEPARATOR + eae.getName() + SEPARATOR + eae.getWidth() + SEPARATOR + eae.getHeight();
		} else if (e instanceof StartMoveEvent) {
			StartMoveEvent sme = (StartMoveEvent)e;
			return "" + sme.getTimeStamp()  + SEPARATOR + START_MOVE_EVENT + SEPARATOR + sme.getID() + SEPARATOR + Direction.getDirectionID(sme.getDirection()) + SEPARATOR + sme.getVelocity();
		} else if (e instanceof StopMoveEvent) {
			StopMoveEvent sme = (StopMoveEvent)e;
			return "" + sme.getTimeStamp()  + SEPARATOR + STOP_MOVE_EVENT + SEPARATOR + sme.getID() + SEPARATOR + Direction.getDirectionID(sme.getDirection()) + SEPARATOR + sme.getDirectionalPosition();
		} else if (e instanceof JoinEvent) {
			JoinEvent je = (JoinEvent)e;
			return "" + je.getTimeStamp() + SEPARATOR + JOIN_EVENT;
		}
		return null;
	}
	
	private static int getColorID(Color color) {
		if (color == Color.BLACK) {
			return 0;
		} else if (color == Color.BLUE) {
			return 1;
		} else if (color == Color.CYAN) {
			return 2;
		} else if (color == Color.DARK_GRAY) {
			return 3;
		} else if (color == Color.GRAY) {
			return 4;
		} else if (color == Color.LIGHT_GRAY) {
			return 5;
		} else if (color == Color.MAGENTA) {
			return 6;
		} else if (color == Color.ORANGE) {
			return 7;
		} else if (color == Color.PINK) {
			return 8;
		} else if (color == Color.RED) {
			return 9;
		} else if (color == Color.WHITE) {
			return 10;
		} else if (color == Color.YELLOW) {
			return 11;
		} else {
			return -1;
		}
	}
	
	private static Color getColorFromID(int color) {
		if (color == 0) {
			return Color.BLACK;
		} else if (color == 1) {
			return Color.BLUE;
		} else if (color == 2) {
			return Color.CYAN;
		} else if (color == 3) {
			return Color.DARK_GRAY;
		} else if (color == 4) {
			return Color.GRAY;
		} else if (color == 5) {
			return Color.LIGHT_GRAY;
		} else if (color == 6) {
			return Color.MAGENTA;
		} else if (color == 7) {
			return Color.ORANGE;
		} else if (color == 8) {
			return Color.PINK;
		} else if (color == 9) {
			return Color.RED;
		} else if (color == 10) {
			return Color.WHITE;
		} else if (color == 11) {
			return Color.YELLOW;
		} else {
			return null;
		}
	}
}
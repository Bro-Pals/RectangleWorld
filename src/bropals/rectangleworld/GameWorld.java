package bropals.rectangleworld;

import java.util.ArrayList;

public class GameWorld {
	
	private ArrayList<GameEvent> eventQueue;
	private ArrayList<GameEntity> entities;
	
	public GameWorld() {
		eventQueue = new ArrayList<>();
		entities = new ArrayList<>();
	}
	
	public ArrayList<GameEntity> getEntities() {
		return entities;
	}
}
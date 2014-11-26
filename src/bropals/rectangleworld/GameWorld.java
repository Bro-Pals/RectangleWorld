package bropals.rectangleworld;

public class GameWorld {
	
	private ArrayList<GameEvent> eventQueue;
	private ArrayList<GameEntity> entities;
	
	public GameWorld() {
		eventQueue = new ArrayList<>();
		entities = new ArrayList<>();
	}
}
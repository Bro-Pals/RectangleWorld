package bropals.rectangleworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;

public class GameWorld {
	
	private List<GameEvent> eventQueue;
	private List<GameEntity> entities;
	private static final int 
		boundryLeft = 0, 
		boundryRight = 1500, 
		boundryTop = 0, 
		boundryBottom = 1500
	;
	
	public GameWorld() {
		eventQueue = Collections.synchronizedList(new ArrayList<GameEvent>());
		entities = Collections.synchronizedList(new ArrayList<GameEntity>());
	}
	
	public List<GameEntity> getEntities() {
		return entities;
	}
	
	public void addEvent(GameEvent e) {
		eventQueue.add(e);
	}
	
	public void updateEvents() {
		/*
			Handle all received and self-generated game events on this frame; don't let new guys come in
		*/
		synchronized (eventQueue) {
			GameEvent current;
			Iterator iterator = eventQueue.iterator();
			while(iterator.hasNext()) {
				current = (GameEvent)iterator.next();
				handleEvent(current);
			}
		}
	}
	
	public void handleEvent(GameEvent event) {
		System.out.println("Handling an event! \n msg: " + GameEventParser.translateEvent(event) + 
			"\n (delay: " +(System.currentTimeMillis() - event.getTimeStamp()) +")\n");
	}
	
	public GameEntity getEntity(int id) {
		synchronized (entities) {
			GameEntity current;
			Iterator iterator = entities.iterator();
			while(iterator.hasNext()) {
				current = (GameEntity)iterator.next();
				if (current.getID() == id) {
					return current;
				}
			}
		}
		System.out.println("Could not find Entity with id " + id);
		return null;
	}
	
	public int getBoundryRight() { return boundryRight; }
	public int getBoundryLeft() { return boundryLeft; }
	public int getBoundryTop() { return boundryTop; }
	public int getBoundryBottom() { return boundryBottom; }
}
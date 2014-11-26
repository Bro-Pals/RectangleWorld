package bropals.rectangleworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;

public class GameWorld {
	
	private List<GameEvent> eventQueue;
	private List<GameEntity> entities;
	
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
	
	public void update() {
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
		/*
			Update this copy of the world, not allowing self generated events to come in
		*/
		synchronized (entities) {
			GameEntity current;
			Iterator i = entities.iterator();
			while(i.hasNext()) {
				current = (GameEntity)i.next();
				current.update();
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
}
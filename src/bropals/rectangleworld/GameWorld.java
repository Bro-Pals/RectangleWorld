package bropals.rectangleworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import bropals.rectangleworld.event.*;

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
		if (event instanceof ChatEvent) {
			PlayerEntity pent = (PlayerEntity)getEntity(event.getID()); // it should be a player that talked
			ChatEvent chate = (ChatEvent)event;
			if (pent != null) {
				System.out.println(pent.getName() + " : \"" + chate.getText() + "\"");
			}
		} else if (event instanceof ColorChangeEvent) {
			GameEntity ent = getEntity(event.getID());
			ColorChangeEvent cce = (ColorChangeEvent)event;
			if (ent != null) {
				ent.setColor(cce.getColor());
			}
		} else if (event instanceof EntityAddEvent) {
			EntityAddEvent eae = (EntityAddEvent)event;
			entities.add(new GameEntity(eae.getID(), this, eae.getColor(), eae.getPositionX(), 
				 eae.getPositionY(), eae.getWidth(), eae.getHeight()));
		} else if (event instanceof EntityRemoveEvent) {
			GameEntity ent = getEntity(event.getID());
			if (ent != null) {
				entities.remove(ent);
			}
		} else if (event instanceof PlayerAddEvent) {
			PlayerAddEvent pae = (PlayerAddEvent)event;
			entities.add(new PlayerEntity(pae.getID(), this, pae.getColor(), pae.getPositionX(), 
				 pae.getPositionY(), pae.getWidth(), pae.getHeight(), pae.getName()));
		} else if (event instanceof StartMoveEvent) {
			GameEntity ent = getEntity(event.getID());
			StartMoveEvent sme = (StartMoveEvent)event;
			if (ent != null) {
				if (sme.getDirection() == Direction.NORTH_SOUTH) {
					ent.setYVelocity(sme.getVelocity());
				} else {
					ent.setXVelocity(sme.getVelocity());
				}
			}
		} else if (event instanceof StopMoveEvent) {
			GameEntity ent = getEntity(event.getID());
			StopMoveEvent sme = (StopMoveEvent)event;
			if (ent != null) {
				if (sme.getDirection() == Direction.NORTH_SOUTH) {
					ent.setYVelocity(0);
					ent.setY(sme.getDirectionalPosition());
				} else {
					ent.setXVelocity(0);
					ent.setX(sme.getDirectionalPosition());
				}
			}
		}
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
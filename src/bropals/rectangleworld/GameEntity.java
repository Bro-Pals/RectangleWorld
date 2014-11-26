package bropals.rectangleworld;

import java.awt.Color;

/**
	The GameEntity class made a bazillion times before
*/
public class GameEntity {
	
	private GameWorld world; // reference to the world it's in
	private Color color;
	private int id; // it's unique ID number given to it by the server
	private float x,y,w,h,xV,yV;
	
	public GameEntity(int idNum, GameWorld world, Color c, float x, float y, float width, float height) {
		this.x=x;
		this.y=y;
		this.w=width;
		this.h=height;
		this.xV=0;
		this.yV=0;
		this.id=idNum;
		this.world=world;
		this.color=c;
	}
	
	public void update() {
		this.x += this.xV;
		this.y += this.yV;
	}
	
	public void setGameWorld(GameWorld world) { this.world = world; }
	public void setColor(Color c) { this.color = c; }
	public void setX(float x) { this.x=x; }
	public void setY(float y) { this.y=y; }
	public void setWidth(float width) { this.w=width; }
	public void setHeight(float height) { this.h=height; }
	public void setXVelocity(float xVel) { this.xV=xVel; }
	public void setYVelocity(float yVel) { this.yV=yVel; }
	public void getID(int id) { this.id=id; }
	
	public GameWorld getGameWorld() { return world; }
	public Color getColor() { return color; }
	public float getX() { return x; }
	public float getY() { return y; }
	public float getWidth() { return w; }
	public float getHeight() { return h; }
	public float getXVelocity() { return xV; }
	public float getYVelocity() { return yV; }
	public int getID() { return id; }
	
}
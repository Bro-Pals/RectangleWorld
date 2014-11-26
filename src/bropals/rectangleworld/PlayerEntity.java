package bropals.rectangleworld;

/**
	Just like the GameEntity only has a name too
*/
public class PlayerEntity extends GameEntity {
	
	private String name;
	
	public PlayerEntity(int idNum, GameWorld world, Color c, float x, float y, float width, float height, String name) {
		super(idNum, world, c, x, y, width, height);
		this.name=name;
	}
	
	public String getName() { return name; }
	
	public void setName(String n) { this.name = n; }
}
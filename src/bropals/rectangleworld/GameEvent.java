package bropals.rectangleworld;

public class GameEvent {
	
	private long timeStamp;
	private int id;
	
	public GameEvent(int id) {
		this.id=id;
	}
	
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}	
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public int getID() {
		return id;
	}
}
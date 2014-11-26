package bropals.rectangleworld;

public enum Direction {
	NORTH_SOUTH, EAST_WEST;
	
	public static Direction getByID(int id) {
		if (id == 0) {
			return NORTH_SOUTH;
		} else {
			return EAST_WEST;
		}
	}
	
	public static int getDirectionID(Direction d) {
		if (d == Direction.NORTH_SOUTH) {
			return 0;
		} else {
			return 1;
		}
	}
}
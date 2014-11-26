package horsentp.rectangleworld;

public enum Direction {
	NORTH_SOUTH, EAST_WEST;
	
	public static Direction getById(int id) {
		if (id == 0) {
			return NORTH_SOUTH;
		} else {
			return EAST_WEST;
		}
	}
	
	public static int getDirectionId(Direction d) {
		if (d == Direction.NORTH_SOUTH) {
			return 0;
		} else {
			return 1;
		}
	}
}
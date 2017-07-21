package core;

public class Ice extends AllWall {

	@Override
	public String toString() {
		return " @@ ";
	}

	public Ice(int x, int y, World world) {
		super(x, y, world, true, true);
	}
}

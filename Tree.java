package core;

public class Tree extends AllWall {

	@Override
	public String toString() {
		return " TT ";
	}          

	public Tree(int x, int y, World world) {
		super(x, y, world, true, true);
	}
}

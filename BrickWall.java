package progettoIGPE.davide.giovanni.unical2016;

public class BrickWall extends Wall {

	public BrickWall(int x, int y, World world, int health) {
		super(x, y, world, 4, false, false);
	}

	@Override
	public String toString() {
		return "[||]";
	}

}

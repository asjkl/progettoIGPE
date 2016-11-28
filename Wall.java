package progettoIGPE.davide.giovanni.unical2016;

public class Wall extends AllWall {
	private int health;
	
	public Wall(int x, int y, World world, int health, boolean shot, boolean tank) {
		super(x, y, world, shot, tank);
		this.health = health;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public void damage() {
		
	}
}

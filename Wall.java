package progettoIGPE.davide.giovanni.unical2016;

public class Wall extends AllWall {
	
	private AbstractStaticObject before; //salvo powerUp che cade
	private int health;
	
	public Wall(int x, int y, World world, int health, boolean shot, boolean tank) {
		super(x, y, world, shot, tank);
		this.health = health;
		before=null;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public void damage() {
		
	}

	public AbstractStaticObject getBefore() {
		return before;
	}

	public void setBefore(AbstractStaticObject before) {
		this.before = before;
	}
}

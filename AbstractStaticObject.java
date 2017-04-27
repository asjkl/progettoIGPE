package progettoIGPE.davide.giovanni.unical2016;

public abstract class AbstractStaticObject implements StaticObject {
	private int x;
	private int y;
	protected World world;
	private int inc; // serve per gli effetti di powerUp/Tank

	public AbstractStaticObject(int x, int y, World world) {
		this.x = x;
		this.y = y;
		this.world = world;
		this.inc=0;
	}

	@Override
	public int getX() {
		return x;
	}

	// eccezzione viene gestito nell' update
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getY() {
		return y;
	}

	// eccezzione viene gestito nell' update
	public void setY(int y) {
		this.y = y;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	@Override
	public String toString() {
		return "AbstractStaticObject [x=" + x + ", y=" + y + ", mondo=" + world + "]";
	}

	public int getInc() {
		return inc;
	}

	public void setInc(int inc) {
		this.inc = inc;
	}
}
package progettoIGPE.davide.giovanni.unical2016;

public class BrickWall extends Wall {

	private AbstractStaticObject before; //salvo powerUp che cade
	
	public BrickWall(int x, int y, World world, int health) {
		super(x, y, world, 4, false, false);
		before=null;
	}

	@Override
	public String toString() {
		return "[  ]";
	}

	public AbstractStaticObject getBefore() {
		return before;
	}

	public void setBefore(AbstractStaticObject before) {
		this.before = before;
	}
}

package progettoIGPE.davide.giovanni.unical2016;

public class BrickWall extends Wall {

	private PowerUp before; //salvo powerUp che cade
	
	public BrickWall(int x, int y, World world, int health) {
		super(x, y, world, 4, false, false);
		setBefore(null);
	}

	@Override
	public String toString() {
		return "[  ]";
	}

	public PowerUp getBefore() {
		return before;
	}

	public void setBefore(PowerUp before) {
		this.before = before;
	}

}

package progettoIGPE.davide.giovanni.unical2016;

public class AllWall extends AbstractStaticObject {

	private boolean shot;
	private boolean tank;

	public AllWall(int x, int y, World mondo, boolean shot, boolean tank) {
		super(x, y, mondo);
		this.shot = shot;
		this.tank = tank;
	}
	
	public boolean isShot() {
		return shot;
	}

	public void setShot(boolean shot) {
		this.shot = shot;
	}

	public boolean isTank() {
		return tank;
	}

	public void setTank(boolean tank) {
		this.tank = tank;
	}

}

package progettoIGPE.davide.giovanni.unical2016;

public abstract class Tank extends AbstractDynamicObject{

	public Tank(int x, int y, World mondo, Speed speed, Speed speedShot, Direction direction,int health) {
		super(x, y, mondo, speed, speedShot, direction, health);
	}
	
	@Override
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;
	}
}

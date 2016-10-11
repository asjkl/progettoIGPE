package progettoIGPE.davide.giovanni.unical2016;

public class PowerUp extends AbstractStaticObject {

	@Override
	public String toString() {
		return " $$ ";
	}

	private Power powerUp;

	public PowerUp(int x, int y, World world, Power powerUp) {
		super(x, y, world);
		this.powerUp = powerUp;
	}

	public Power getPowerUp() {
		return powerUp;
	}

	public void setPowerUp(Power powerUp) {
		this.powerUp = powerUp;
	}
}

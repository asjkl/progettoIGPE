package progettoIGPE.davide.giovanni.unical2016;

public class PowerUp extends AbstractStaticObject {

	private AbstractStaticObject before;
	private Power powerUp;

	public PowerUp(int x, int y, World world, Power powerUp) {
		super(x, y, world);
		this.powerUp = powerUp;
	}

	public AbstractStaticObject getBefore() {
		return before;
	}

	public void setBefore(AbstractStaticObject before) {
		this.before = before;
	}

	public Power getPowerUp() {
		return powerUp;
	}

	public void setPowerUp(Power powerUp) {
		this.powerUp = powerUp;
	}

	@Override
	public String toString() {
		switch (powerUp) {
		case GRANADE:
			return " $1 ";
		case HELMET:
			return " $2 ";
		case SHOVEL:
			return " $3 ";
		case STAR:
			return " $4 ";
		case TANK:
			return " $5 ";
		case TIMER:
			return " $6 ";
		default:
			return null;
		}
	}

}

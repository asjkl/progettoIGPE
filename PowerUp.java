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
			return " $G ";
		case HELMET:
			return " $H ";
		case SHOVEL:
			return " $SH ";
		case STAR:
			return " $ST ";
		case TANK:
			return " $T ";
		case TIMER:
			return " $TI ";
		default:
			return null;
		}
	}

}

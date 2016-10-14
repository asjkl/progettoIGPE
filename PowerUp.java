package progettoIGPE.davide.giovanni.unical2016;

public class PowerUp extends AbstractStaticObject {

	private AbstractStaticObject before;
	private Power powerUp;
	private AbstractDynamicObject tank; //powerup appartenenza

	public PowerUp(int x, int y, World world, Power powerUp) {
		super(x, y, world);
		this.powerUp = powerUp;
	}
	
	public PowerUp(int x, int y, World world, Power powerUp, AbstractDynamicObject  tank) {
		super(x, y, world);
		this.powerUp = powerUp;
		this.tank=tank;
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
	

	public AbstractDynamicObject getTank() {
		return tank;
	}

	public void setTank(AbstractDynamicObject tank) {
		this.tank = tank;
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

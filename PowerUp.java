package progettoIGPE.davide.giovanni.unical2016;

public class PowerUp extends AbstractStaticObject {

	private AbstractStaticObject before;
	private Power powerUp;
	private AbstractDynamicObject tank; //powerup appartenenza
	private long timer;
	private boolean activate;
	private long duration;

	public PowerUp(int x, int y, World world, Power powerUp) {
		super(x, y, world);
		this.powerUp = powerUp;
		timer=0;
		activate=false;
		duration=5;
	}
	
	public PowerUp(int x, int y, World world, Power powerUp, AbstractDynamicObject  tank) {
		super(x, y, world);
		this.powerUp = powerUp;
		this.tank=tank;
		timer=0;
		activate=false;
		duration=5;
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

	public long getTimer() {
		return timer;
	}

	public void setTimer(long timer) {
		this.timer = timer;
	}

	public boolean isActivate() {
		return activate;
	}

	public void setActivate(boolean activate) {
		this.activate = activate;
	}
	
	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
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

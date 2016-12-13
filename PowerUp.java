package progettoIGPE.davide.giovanni.unical2016;

public class PowerUp extends AbstractStaticObject {

	private AbstractStaticObject before; //salva oggetto su cui cade
	private Power powerUp;
	private AbstractDynamicObject tank; //powerup appartenenza
	private long timer;
	private boolean activate;
	private long duration;
	private boolean drop;
	private long dropTime; //salva il tempo quando cade
	private boolean dropOnBorder;
	private String dropDirection;

	public PowerUp(int x, int y, World world, Power powerUp, long duration, boolean drop) {
		super(x, y, world);
		this.powerUp = powerUp;
		timer=0;
		activate=false;
		this.duration=duration;
		this.drop=drop;
		this.setDropTime(0);
		before=null;
		dropOnBorder = false;
		dropDirection=null;
	}

	@Override
	public String toString() {
		switch (powerUp) {
		case GRENADE:
			return "GRENADE";
		case HELMET:
			return "HELMET";
		case SHOVEL:
			return "SHOVEL";
		case STAR:
			return "STAR";
		case TANK:
			return "TANK";
		case TIMER:
			return "TIMER";
		default:
			return null;
		}
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

	public boolean isDrop() {
		return drop;
	}

	public void setDrop(boolean drop) {
		this.drop = drop;
	}

	public long getDropTime() {
		return dropTime;
	}

	public void setDropTime(long dropTime) {
		this.dropTime = dropTime;
	}

	public AbstractStaticObject getBefore() {
		return before;
	}

	public void setBefore(AbstractStaticObject before) {
		this.before = before;
	}

	public boolean isDropOnBorder() {
		return dropOnBorder;
	}

	public void setDropOnBorder(boolean dropOnBorder) {
		this.dropOnBorder = dropOnBorder;
	}

	public String getDropDirection() {
		return dropDirection;
	}

	public void setDropDirection(String dropDirection) {
		this.dropDirection = dropDirection;
	}
	
}

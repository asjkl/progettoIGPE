package progettoIGPE.davide.giovanni.unical2016;

public class PowerUp extends AbstractStaticObject {

	private AbstractStaticObject before; //salvo oggetto su cui cade powerUp
	private Power powerUp;
	private AbstractDynamicObject tank; //powerup appartenenza
	private long timer;
	private boolean activate;
	private long duration;
	private long count;
	private long time; //serve per geestire il cont ovvero durata powerUp prima di distruggersi
	private boolean drop;

	public PowerUp(int x, int y, World world, Power powerUp, long duration, boolean drop) {
		super(x, y, world);
		this.powerUp = powerUp;
		timer=0;
		activate=false;
		this.duration=duration;
		count=0;
		time=0;
		this.drop=drop;
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

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isDrop() {
		return drop;
	}

	public void setDrop(boolean drop) {
		this.drop = drop;
	}

}

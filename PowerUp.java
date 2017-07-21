package core;

public class PowerUp extends AbstractStaticObject {

	private Power powerUp;
	private boolean activate;
	private boolean drop;
	private boolean dropOnBorder;
	private Direction dropDirection;
	private long duration;
	private long dropTime;                    //dropped
	private boolean blink;
	private int inc;                          // serve per gli effetti di powerUp
	private long time;                        //timeout
	private boolean blinkShovel;
	private AbstractDynamicObject tank;       //powerup appartenenza
	private AbstractStaticObject before;      //salva oggetto su cui cade
	private AbstractStaticObject beforeWater; // importante non delete

	public PowerUp(int x, int y, World world, Power powerUp) {
		super(x, y, world);
		this.powerUp = powerUp;
		this.beforeWater=null;
		this.activate=false;
		this.duration=duration();
		this.drop=true;
		this.setDropTime(0);
		this.before=null;
		this.dropOnBorder = false;
		this.setDropDirection(null);
		this.blink=false;
		this.inc=0;
		this.setBlinkShovel(false);
		time=0;
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
	
	public int duration(){
		if(powerUp == Power.HELMET || powerUp == Power.TIMER)
			return 10;
		if(powerUp == Power.SHOVEL)
			return 12;
		return 0;
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

	public Direction getDropDirection() {
		return dropDirection;
	}

	public void setDropDirection(Direction dropDirection) {
		this.dropDirection = dropDirection;
	}

	public boolean isBlink() {
		return blink;
	}

	public void setBlink(boolean blink) {
		this.blink = blink;
	}

	public int getInc() {
		return inc;
	}

	public void setInc(int inc) {
		this.inc = inc;
	}

	public AbstractStaticObject getBeforeWater() {
		return beforeWater;
	}

	public void setBeforeWater(AbstractStaticObject beforeWater) {
		this.beforeWater = beforeWater;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isBlinkShovel() {
		return blinkShovel;
	}

	public void setBlinkShovel(boolean blinkShovel) {
		this.blinkShovel = blinkShovel;
	}

}

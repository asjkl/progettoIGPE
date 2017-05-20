package progettoIGPE.davide.giovanni.unical2016;

public abstract class Tank extends AbstractDynamicObject{

	private boolean readyToSpawn;
	private int countdown;
	private long spawnTime; //salva il tempo che ci mette per spawnare
	private boolean protection;
	private Direction  oldD; 
	private boolean oldDirection;
	private Direction tmpDirection;
	private long timerEffect;
	
	public Tank(int x, int y, World mondo, Speed speed, Speed speedShot, Direction direction,int health) {
		super(x, y, mondo, speed, speedShot, direction, health);
		this.setCountdown(0);
		protection = false;
		oldD = Direction.STOP;
		tmpDirection = Direction.STOP;
		timerEffect = -1;
	}
	
	@Override
	public void update(){
		super.update();
	}

	public boolean isReadyToSpawn() {
		return readyToSpawn;
	}

	public void setReadyToSpawn(boolean readyToSpawn) {
		this.readyToSpawn = readyToSpawn;
	}

	public int getCountdown() {
		return countdown;
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}

	public long getSpawnTime() {
		return spawnTime;
	}

	public void setSpawnTime(long spawnTime) {
		this.spawnTime = spawnTime;
	}

	public Direction getOldD() {
		return oldD;
	}

	public void setOld(Direction old) {
		this.oldD = old;
	}

	public boolean isOldDirection() {
		return oldDirection;
	}

	public void setOldDirection(boolean oldDirection) {
		this.oldDirection = oldDirection;
	}
		
	public Direction getTmpDirection() {
		return tmpDirection;
	}

	public void setTmpDirection(Direction tmpDirection) {
		this.tmpDirection = tmpDirection;
	}

	public long getTimerEffect() {
		return timerEffect;
	}

	public void setTimerEffect(long timerEffect) {
		this.timerEffect = timerEffect;
	}

	public boolean isProtection() {
		return protection;
	}

	public void setProtection(boolean protection) {
		this.protection = protection;
	}
}

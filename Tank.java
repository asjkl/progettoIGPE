package progettoIGPE.davide.giovanni.unical2016;

public abstract class Tank extends AbstractDynamicObject{

	//utilizzato per l effetto nel gamecomponent
	private int tmpCont;
	private boolean readyToSpawn;
	private int countdown;
	private long spawnTime; //salva il tempo che ci mette per spawnare
	
	private Direction  oldD; 
	private boolean oldDirection;
	private Direction tmpDirection; 
	
	private long timerEffect;
	
	public Tank(int x, int y, World mondo, Speed speed, Speed speedShot, Direction direction,int health) {
		super(x, y, mondo, speed, speedShot, direction, health);
		setTmpCont(0);
		this.setCountdown(0);
		oldD = Direction.STOP;
		tmpDirection = Direction.STOP;
		timerEffect = -1;
	}
	
	@Override
	public void update(){
		super.update();
		getWorld().world[getX()][getY()] = this;
	}

	public int getTmpCont() {
		return tmpCont;
	}

	public void setTmpCont(int tmpCont) {
		this.tmpCont = tmpCont;
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

}

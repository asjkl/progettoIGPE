package progettoIGPE.davide.giovanni.unical2016;

import java.util.ArrayList;

public class PlayerTank extends Tank {

	private int level;
	private int resume;
	private int point;
	private boolean died;
	private boolean first;
	private int bornX;
	private int bornY;
	private boolean shot;
	
	private long keyPressedMillis=0;
	private long keyPressLength=0;
	private ArrayList<Integer> keys;
	private boolean pressed;
	private boolean releaseKeyRocket;
	private boolean enter;
	
	public PlayerTank(int x, int y, World world) {
		super(x, y, world, Speed.NORMAL, Speed.FASTROCKET, Direction.STOP, 1);
		this.resume = 3;
		this.point = 0;
		this.level = 1;
		this.died = false;
		this.setReadyToSpawn(true);
		this.setCountdown(0);
		this.setInc(0);
		this.setRotateDegrees(0);
		this.setTmpDirection(Direction.UP);
		this.first=true;
		this.setBornX(x);
		this.setBornY(y);
		setShot(false);
		this.setKeys(new ArrayList<>());
		this.setPressed(false);
		this.setReleaseKeyRocket(false);
		this.setEnter(false);
	} 

	@Override
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;
		setDirection(Direction.STOP); 
	}

	@Override
	public boolean sameObject() {
		
		// DA CONTROLLARE CAMBIATO THIS PRIMA ERA PLAYERTANK
		if(next instanceof Rocket && ((Rocket)next).getTank() == this ){
			next = ((Rocket)next).getCurr();
		}
		
		if (!(next instanceof Wall) && !(next instanceof Tank) && !(next instanceof Water) && !(next instanceof Rocket) && !(next instanceof Flag)) {

			if (next instanceof PowerUp ){
				if (!(((PowerUp)next).getBefore() instanceof Tree) && !(((PowerUp)next).getBefore() instanceof Ice) && !(((PowerUp)next).getBefore() instanceof Water)) 
					curr = null;
				else if(((PowerUp)next).getBefore() instanceof Water)
					curr =((PowerUp)next).getBeforeWater();
				else if((((PowerUp)next).getBefore() instanceof Tree || ((PowerUp)next).getBefore() instanceof Ice) )
					curr = ((PowerUp)next).getBefore();
			}
			else
				curr = next;
				
			return true;
		}
		return false;
	}

	@Override
	public Direction getDirection() {
		return super.getDirection();
	}

	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getResume() {
		return resume;
	}

	public void setResume(int resume) {
		this.resume = resume;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	@Override
	public String toString() {
		return " ** ";
	}

	public boolean isDied() {
		return died;
	}

	public void setDied(boolean died) {
		this.died = died;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public int getBornX() {
		return bornX;
	}

	public void setBornX(int bornX) {
		this.bornX = bornX;
	}

	public int getBornY() {
		return bornY;
	}

	public void setBornY(int bornY) {
		this.bornY = bornY;
	}

	public boolean isShot() {
		return shot;
	}

	public void setShot(boolean shot) {
		this.shot = shot;
	}

	public long getKeyPressedMillis() {
		return keyPressedMillis;
	}

	public void setKeyPressedMillis(long keyPressedMillis) {
		this.keyPressedMillis = keyPressedMillis;
	}

	public long getKeyPressLength() {
		return keyPressLength;
	}

	public void setKeyPressLength(long keyPressLength) {
		this.keyPressLength = keyPressLength;
	}

	public ArrayList<Integer> getKeys() {
		return keys;
	}

	public void setKeys(ArrayList<Integer> keys) {
		this.keys = keys;
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public boolean isReleaseKeyRocket() {
		return releaseKeyRocket;
	}

	public void setReleaseKeyRocket(boolean releaseKeyRocket) {
		this.releaseKeyRocket = releaseKeyRocket;
	}

	public boolean isEnter() {
		return enter;
	}

	public void setEnter(boolean enter) {
		this.enter = enter;
	}
}

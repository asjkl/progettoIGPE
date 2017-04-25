package progettoIGPE.davide.giovanni.unical2016;

import java.awt.Point;

public abstract class AbstractDynamicObject extends AbstractStaticObject implements DynamicObject {
	
	private Speed speed;
	private Speed speedShot;
	private Direction direction;
	private int health;
	protected AbstractStaticObject curr;
	protected AbstractStaticObject next;
	private int contRocket;
	public boolean canGo;
	private int contP; //conta pixel
	private boolean updateObject; //switcha dalla logica alla grafica
	private Point pixelPosition;
	private int sizePixel;
	private int rotateDegrees; // rotazione oggetto
	private boolean firstTime; // booleana per entrare una sola volta
	
	//distruzione object + time (effect explosion)
	private boolean toDestroy;
	private long time;
	private int inc;
	
	public AbstractDynamicObject(int x, int y, World mondo, Speed speed, Speed speedShot, Direction direction,int health) {
		super(x, y, mondo);
		this.sizePixel=35;
		this.speed = speed;
		this.direction = direction;
		this.speedShot = speedShot;
		this.health = health;
		this.contRocket=0;
		this.curr = null;
		this.next = null;
		this.pixelPosition=new Point(x*sizePixel, y*sizePixel);
		this.rotateDegrees=0;
		this.toDestroy=false;
		this.time=-1;
		this.inc=0;
		this.firstTime=true;
		FPS();
	}

	//COSTRUTTORE SOLO PER IL ROCKET
	public AbstractDynamicObject(int x, int y, World mondo, Direction direction) {		
		super(x, y, mondo);
		this.sizePixel=35;
		this.direction = direction;
		pixelPosition=new Point(x*sizePixel, y*sizePixel);
		FPS();	
	}

	public void FPS(){
		setCont(1);
	}
	
	public void update() {
		
		canGo=true;
		
		// rimette l oggetto di prima
		if (!(curr instanceof Tank)) {
			getWorld().world[getX()][getY()] = curr;
		}
		
		switch (getDirection()) {
		case UP:
			if (getX() - 1 >= 0) {
				next = getWorld().world[getX() - 1][getY()];
				if (sameObject()) {
					setX(getX() - 1);
				}
				else
					canGo=false;
			}
			else 
				canGo=false;
			break;
		case DOWN:
			if (getX() + 1 < getWorld().getRow()) {
				next = getWorld().world[getX() + 1][getY()];
				if (sameObject()) {
					setX(getX() + 1);
				}
				else
					canGo=false;
			}	
			else 
				canGo=false;
			break;
		case LEFT:
			if (getY() - 1 >= 0) {
				next = getWorld().world[getX()][getY() - 1];
				if (sameObject()) {
					setY(getY() - 1);
				}
				else
					canGo=false;
			}
			else 
				canGo=false;
			break;
		case RIGHT:
			if (getY() + 1 < getWorld().getColumn()) {
				next = getWorld().world[getX()][getY() + 1];
				if (sameObject()) {
					setY(getY() + 1);
				}
				else
					canGo=false;
			}
			else 
				canGo=false;
			break;
		default:
			break;
		}
	}

	public abstract boolean sameObject();

	@Override
	public Speed getSpeed() {
		return speed;
	}

	public void setSpeed(Speed speed) {
		this.speed = speed;
	}

	@Override
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public Speed getSpeedShot() {
		return speedShot;
	}

	public void setSpeedShot(Speed speedShot) {
		this.speedShot = speedShot;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public AbstractStaticObject getCurr() {
		return curr;
	}

	public void setCurr(AbstractStaticObject curr) {
		this.curr = curr;
	}

	public AbstractStaticObject getNext() {
		return next;
	}

	public void setNext(AbstractStaticObject next) {
		this.next = next;
	}

	public int getContRocket() {
		return contRocket;
	}

	public void setContRocket(int contRocket) {
		this.contRocket = contRocket;
	}

	public boolean isUpdateObject() {
		return updateObject;
	}

	public void setUpdateObject(boolean updateObject) {
		this.updateObject = updateObject;
	}

	public int getCont() {
		return contP;
	}

	public void setCont(int contP) {
		this.contP = contP;
	}

	public Point getPixelPosition() {
		return pixelPosition;
	}

	public void setPixelPosition(Point pixelPosition) {
		this.pixelPosition = pixelPosition;
	}

	public int getSizePixel() {
		return sizePixel;
	}

	public void setSizePixel(int sizePixel) {
		this.sizePixel = sizePixel;
	}

	public int getRotateDegrees() {
		return rotateDegrees;
	}

	public void setRotateDegrees(int rotateDegrees) {
		this.rotateDegrees = rotateDegrees;
	}

	public boolean isToDestroy() {
		return toDestroy;
	}

	public void setToDestroy(boolean toDestroy) {
		this.toDestroy = toDestroy;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getInc() {
		return inc;
	}

	public void setInc(int inc) {
		this.inc = inc;
	}

	public boolean isFirstTime() {
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}
	
}
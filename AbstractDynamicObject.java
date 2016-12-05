package progettoIGPE.davide.giovanni.unical2016;

public abstract class AbstractDynamicObject extends AbstractStaticObject implements DynamicObject {
	private Speed speed;
	private Speed speedShot;
	private Direction direction;
	private int health;
	protected AbstractStaticObject curr;
	protected AbstractStaticObject next;
	private int contRocket = 0;
	
	public boolean bordi;

	public AbstractDynamicObject(int x, int y, World mondo, Speed speed, Speed speedShot, Direction direction,
			int health) {
		super(x, y, mondo);
		this.speed = speed;
		this.direction = direction;
		this.speedShot = speedShot;
		this.health = health;
		this.setContRocket(0);
		curr = null;
		next = null;
	}

	public AbstractDynamicObject(int x, int y, World mondo, Direction direction) {
		super(x, y, mondo);
		this.direction = direction;
	}

	public void update() {
		bordi=false;
		// rimette l oggetto di prima
		if (!(curr instanceof PlayerTank) && !(curr instanceof EnemyTank)) {
			getWorld().world[getX()][getY()] = curr;
		}
		switch (getDirection()) {
		case UP:
			if (getX() - 1 >= 0) {
				next = getWorld().world[getX() - 1][getY()];
				if (sameObject()) {
					setX(getX() - 1);
				}else{
					bordi=true;
				}
			}else{
				bordi=true;
			}
			break;
		case DOWN:
			if (getX() + 1 < getWorld().getRow()) {
				next = getWorld().world[getX() + 1][getY()];
				if (sameObject()) {
					setX(getX() + 1);
				}else{
					bordi=true;
				}
			}else{
				bordi=true;
			}
			break;
		case LEFT:
			if (getY() - 1 >= 0) {
				next = getWorld().world[getX()][getY() - 1];
				if (sameObject()) {
					setY(getY() - 1);
				}else{
					bordi=true;
				}
			}else{
				bordi=true;
			}
			break;
		case RIGHT:
			if (getY() + 1 < getWorld().getColumn()) {
				next = getWorld().world[getX()][getY() + 1];
				if (sameObject()) {
					setY(getY() + 1);
				}else{
					bordi=true;
				}
			}else{
				bordi=true;
			}
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
}
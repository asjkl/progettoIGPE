package progettoIGPE.davide.giovanni.unical2016;

public abstract class AbstractDynamicObject extends AbstractStaticObject implements DynamicObject {
	private Speed speed;
	private Speed speedShot;
	private Direction direction;
	private int health;
	private AbstractStaticObject curr;
	private AbstractStaticObject next;

	public AbstractDynamicObject(int x, int y, World mondo, Speed speed, Speed speedShot, Direction direction,
			int health) {
		super(x, y, mondo);
		this.speed = speed;
		this.direction = direction;
		this.speedShot = speedShot;
		this.health = health;
	}

	public AbstractDynamicObject(int x, int y, World mondo, Direction direction) {
		super(x, y, mondo);
		this.direction = direction;
	}

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

	public void update() {
		switch (getDirection()) {
		case UP:
			if (getX() - 1 >= 0) {
				next = getWorld().world[getX() - 1][getY()];
				if (sameObject()) {
					setX(getX() - 1);
				}
			}
			break;
		case DOWN:
			if (getX() + 1 < getWorld().getRow()) {
				next = getWorld().world[getX() + 1][getY()];
				if (sameObject()) {
					setX(getX() + 1);
				}
			}
			break;
		case LEFT:
			if (getY() - 1 >= 0) {
				next = getWorld().world[getX()][getY() - 1];
				if (sameObject()) {
					setY(getY() - 1);
				}
			}
			break;
		case RIGHT:
			if (getY() + 1 < getWorld().getColumn()) {
				next = getWorld().world[getX()][getY() + 1];
				if (sameObject()) {
					setY(getY() + 1);
				}
			}
			break;
		default:
			break;
		}
	}

	public boolean sameObject() {
		if (!(next instanceof BrickWall) && !(next instanceof SteelWall) && !(next instanceof EnemyTank)
				&& !(next instanceof PlayerTank) && !(next instanceof Water) && !(next instanceof Rocket)) {
			if (next == curr) {
				getWorld().world[getX()][getY()] = next;
			} else {
				getWorld().world[getX()][getY()] = curr;
				curr = next;
			}
			return true;
		}
		return false;
	}
}
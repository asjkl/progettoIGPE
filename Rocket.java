package progettoIGPE.davide.giovanni.unical2016;

public class Rocket extends AbstractDynamicObject {

	private boolean bordo; // se trovo bordo
	private boolean shot; // se � stato sparato un colpo

	public Rocket(int x, int y, World world, Direction direction, boolean shot) {
		super(x, y, world, direction);
		this.bordo = false;
		this.shot = shot;

	}

	public boolean isShot() {
		return shot;
	}

	public void setShot(boolean shot) {
		this.shot = shot;
	}

	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}

	@Override
	public String toString() {
		return " -- ";
	}

	@Override
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;
	}

	@Override
	public boolean sameObject() {
		// a differenza di quello Dynamic questo object passa sull acqua
		if (!(getNext() instanceof Wall) && !(getNext() instanceof PlayerTank) && !(getNext() instanceof Rocket)
				&& !(getNext() instanceof EnemyTank)) {
			if (getNext() == getCurr()) {
				getWorld().world[getX()][getY()] = getNext();
			} else {
				getWorld().world[getX()][getY()] = getCurr();
				setCurr(getNext());
			}
			return true;
		}
		return false;
	}

	public boolean isBordo() {
		return bordo;
	}

	public void setBordo(boolean bordo) {
		this.bordo = bordo;
	}
}
package progettoIGPE.davide.giovanni.unical2016;

public class Rocket extends AbstractDynamicObject {

	private boolean bordo; // se trovo bordo
	private boolean shot; // se � stato sparato un colpo
	private AbstractDynamicObject tank; //rocket appartenenza

	public Rocket(int x, int y, World world, Direction direction, boolean shot, AbstractDynamicObject tank) {
		super(x, y, world, direction);
		this.bordo = false;
		this.shot = shot;
		this.tank = tank;
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
		if (!(next instanceof Wall) && !(next instanceof PlayerTank) && !(next instanceof Rocket)
				&& !(next instanceof EnemyTank)  && !(next instanceof Flag)) {
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

	public boolean isBordo() {
		return bordo;
	}

	public void setBordo(boolean bordo) {
		this.bordo = bordo;
	}

	public AbstractDynamicObject getTank() {
		return tank;
	}

	public void setTank(AbstractDynamicObject tank) {
		this.tank = tank;
	}
}
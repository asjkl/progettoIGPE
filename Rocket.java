package progettoIGPE.davide.giovanni.unical2016;

public class Rocket extends AbstractDynamicObject {

	private boolean bordo; // se trovo bordo
	private boolean shot;

	public Rocket(int x, int y, World world, Direction direction) {
		super(x, y, world, direction);
		bordo = false;
		shot = false;
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

	// TODO SPOSTARE FUNZIONI IN CLASSI E FARE EVENTUALI OVERRIDE

	@Override
	public void update() {

		super.update();
		getWorld().world[getX()][getY()] = this;

		if (destroyRocket()) { // distruzione Rocket
			getWorld().world[getX()][getY()] = curr;
			setRocket(null);

			if (next instanceof Wall) // viene settato a null nell matrice ma
										// non distrutto del tutto
				if (((Wall) next).getHealth() == 0)
					destroyWall();

			if (next instanceof EnemyTank)
				if (((EnemyTank) next).getHealth() == 0)
					destroyTank();
		}
	}

	@Override
	public boolean sameObject(AbstractStaticObject tmp) {
		// a differenza di quello Dynamic questo object passa sull acqua
		if (!(tmp instanceof Wall) && !(tmp instanceof PlayerTank) && !(tmp instanceof Rocket)
				&& !(tmp instanceof EnemyTank)) {
			if (tmp == curr) {
				getWorld().world[getX()][getY()] = tmp;
			} else {
				getWorld().world[getX()][getY()] = curr;
				curr = tmp;
			}
			return true;
		}
		return false;
	}

	public boolean destroyRocket() {

		if (bordo || next instanceof Rocket)
			return true;

		if (next instanceof Wall) {
			damageWall();
			shot = false;
			return true;
		}

		if (next instanceof EnemyTank) {
			damageTank();
			shot = false;
			return true;
		}

		// se Rocket tocca bordo
		if ((getX() == 0 && this.getDirection() == Direction.UP)
				|| (getX() == getWorld().getRow() - 1 && this.getDirection() == Direction.DOWN)
				|| (getY() == 0 && this.getDirection() == Direction.LEFT)
				|| (getY() == getWorld().getColumn() - 1 && this.getDirection() == Direction.RIGHT)) {
			bordo = true;
		}
		return false;
	}

	// TODO spostare in Wall e fare override
	private void damageWall() {
		if (PlayerTank.getLevel() == 3) {
			((Wall) next).setHealth(((Wall) next).getHealth() - 2);
		} else if (!(next instanceof SteelWall)) // e non è steelwall
		{
			((Wall) next).setHealth(((Wall) next).getHealth() - 1);
		}
	}

	// TODO spostare e fare override in EnemyTank
	private void damageTank() {
		((EnemyTank) next).setHealth(((EnemyTank) next).getHealth() - 1);
	}

	// TODO spostare in WALL
	public void destroyWall() {
		switch (getDirection()) {
		case UP:
			getWorld().world[getX() - 1][getY()] = null;
			break;
		case DOWN:
			getWorld().world[getX() + 1][getY()] = null;
			break;
		case RIGHT:
			getWorld().world[getX()][getY() + 1] = null;
			break;
		case LEFT:
			getWorld().world[getX()][getY() - 1] = null;
			break;
		default:
			break;
		}
	}

	// TODO spostare in EnemyTank
	public void destroyTank() {
		switch (getDirection()) {
		case UP:
			getWorld().world[getX() - 1][getY()] = curr;
			break;
		case DOWN:
			getWorld().world[getX() + 1][getY()] = curr;
			break;
		case RIGHT:
			getWorld().world[getX()][getY() + 1] = curr;
			break;
		case LEFT:
			getWorld().world[getX()][getY() - 1] = curr;
			break;
		default:
			break;
		}
	}
}

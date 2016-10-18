package progettoIGPE.davide.giovanni.unical2016;

public class Rocket extends AbstractDynamicObject {

	private boolean bordo; // se trovo bordo
	private AbstractDynamicObject tank; // rocket appartenenza
	private boolean destroyEnemy; // SERVE PER AGGIORNARE IL ROCKET ANCHE QUANDO
									// IL SUO TANK � STATO DISTRUTTO

	public Rocket(int x, int y, World world, Direction direction, AbstractDynamicObject tank) {
		super(x, y, world, direction);
		this.bordo = false;
		this.tank = tank;
		this.destroyEnemy = false;
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
				&& !(next instanceof EnemyTank) && !(next instanceof Flag)) {
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

	public boolean isDestroyEnemy() {
		return destroyEnemy;
	}

	public void setDestroyEnemy(boolean destroyEnemy) {
		this.destroyEnemy = destroyEnemy;
	}
}
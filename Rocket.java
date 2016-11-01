package progettoIGPE.davide.giovanni.unical2016;

public class Rocket extends AbstractDynamicObject {

	private boolean bordo; // se trovo bordo
	private AbstractDynamicObject tank; // rocket appartenenza

	public Rocket(int x, int y, World world, Direction direction, AbstractDynamicObject tank) {
		super(x, y, world, direction);
		this.bordo = false;
		this.tank = tank;
		curr = tank.getCurr();
	}

	@Override
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;
	}

	@Override
	public boolean sameObject() {
		if (!(next instanceof Wall) && !(next instanceof PlayerTank) && !(next instanceof Rocket)
				&& !(next instanceof EnemyTank) && !(next instanceof Flag)) {
			curr=next;
			return true;
		}
		return false;
	}
	
	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}

	@Override
	public String toString() {
		return " -- ";
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
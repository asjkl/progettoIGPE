package progettoIGPE.davide.giovanni.unical2016;

public class Rocket extends AbstractDynamicObject {

	private boolean bordo; // se trovo bordo
	private boolean shot; // se � stato sparato un colpo

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

	@Override
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;

		if (destroyRocket()) { // distruzione Rocket
			
			getWorld().world[getX()][getY()] = getCurr();
		
			setRocket(null);
			
			shot=false;

			if (getNext() instanceof Wall)
				if (((Wall) getNext()).getHealth() == 0)
					destroyWall();

			if (getNext() instanceof EnemyTank)
				if (((EnemyTank) getNext()).getHealth() == 0)
					destroyTank();
		}
	}

	@Override
	public boolean sameObject() {
		// a differenza di quello Dynamic questo object passa sull acqua
		if (!(getNext() instanceof Wall) && !(getNext() instanceof PlayerTank) && !(getNext() instanceof Rocket)
				&& !(getNext() instanceof EnemyTank)) {
			if (getNext() == getCurr()) {	
				getWorld().world[getX()][getY()] = getNext();
			} 
			else {
				getWorld().world[getX()][getY()] = getCurr();
				setCurr(getNext());
			}
				return true;
		}
		return false;
	}
	

	public boolean destroyRocket() {

		if (bordo || getNext() instanceof Rocket)
			return true;

		if (getNext() instanceof Wall) {
			damageWall();
			return true;
		}

		if (getNext() instanceof EnemyTank) {
			damageTank();
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

	
	public void destroyTank() {
		switch (getDirection()) {
		case UP:
			getWorld().world[getX() - 1][getY()] = null;		//SOSTITUIRE CON OGGETTO PRECEDENTE
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
		
 		for(int i=0;i < GameManager.getEnemy().size();i++)
 		{
 			if (GameManager.getEnemy().get(i).getHealth() == 0)
 				GameManager.getEnemy().remove(i);
 		}
	}
	
	private void damageWall() {
	 	if (PlayerTank.getLevel() == 3) {
	 		((Wall) getNext()).setHealth(((Wall) getNext()).getHealth()-2);
	 	} else if (!(getNext() instanceof SteelWall)) // e non � steelwall
	  		{
	 			((Wall) getNext()).setHealth(((Wall) getNext()).getHealth()-1);
	  		}
	  }

	
	private void damageTank() {
		((EnemyTank) getNext()).setHealth(((EnemyTank) getNext()).getHealth() - 1);
	}
}
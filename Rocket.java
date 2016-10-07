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

	@Override
	public void update() {
		
		super.update();
		getWorld().world[getX()][getY()] = this;
		
		if(destroyRocket()) {
			getWorld().world[getX()][getY()] = curr;
			setRocket(null);
			
			if(next instanceof BrickWall)
				if(((BrickWall) next).getHealth() == 0)
					destroyWall();
			
			if(next instanceof SteelWall)
				if(((SteelWall) next).getHealth() == 0)
					destroyWall();
		}
	}

	@Override
	public boolean sameObject(AbstractStaticObject tmp) {
		// a differenza di quello Dynamic questo object passa sull acqua
		if (!(tmp instanceof BrickWall) && !(tmp instanceof SteelWall) 
				&& !(tmp instanceof PlayerTank) && !(tmp instanceof Rocket)) {
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

		// se mi trovo al bordo oppure incontro questi oggetti...
		if (bordo || next instanceof Rocket)
			return true;
		
		if(next instanceof BrickWall || next instanceof SteelWall)
		{
			damage();
			shot=false;
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
	
	private void damage()
	{
		if(next instanceof BrickWall)     
		{	
			switch(PlayerTank.getLevel())
			{
				case 3:
				((BrickWall) next).setHealth(((BrickWall) next).getHealth()-2);
				break;
				default: // livello 1 o livello 2
				((BrickWall) next).setHealth(((BrickWall) next).getHealth()-1);
				break;
			}
		}
		else if(next instanceof SteelWall && PlayerTank.getLevel() == 3) //entro solo se player è di livello 3
		{
			((SteelWall) next).setHealth(((SteelWall) next).getHealth()-2);
		}
	}
	
	private void destroyWall(){
		
		switch(getDirection())
		{
		case UP:
			getWorld().world[getX()-1][getY()]=null;
			break;
		case DOWN:
			getWorld().world[getX()+1][getY()]=null;
			break;
		case RIGHT:
			getWorld().world[getX()][getY()+1]=null;
			break;
		case LEFT:
			getWorld().world[getX()][getY()-1]=null;
			break;
		default:
			break;
		}
	}
}
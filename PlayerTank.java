package progettoIGPE.davide.giovanni.unical2016;

public class PlayerTank extends Tank {

	private int level;
	private int resume;
	private int point;
	private boolean died;
	private boolean first;
	
	public PlayerTank(int x, int y, World world) {
		super(x, y, world, Speed.NORMAL, Speed.FAST, Direction.STOP, 1);
		this.resume = 3;
		this.point = 0;
		this.level = 2;
		this.died = false;
		setReadyToSpawn(true);
		first=true;
	} 

	@Override
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;
		setDirection(Direction.STOP); 
	}

	@Override
	public boolean sameObject() {
		if (!(next instanceof Wall) && !(next instanceof Tank)
				&& !(next instanceof Water) && !(next instanceof Rocket) && !(next instanceof Flag)) {
			// prendo tutti i powerUp
			if (next instanceof PowerUp && !(((PowerUp)next).getBefore() instanceof Trees) && !(((PowerUp)next).getBefore() instanceof Ice)) 
				curr = null;
			else if( next instanceof PowerUp && (((PowerUp)next).getBefore() instanceof Trees || ((PowerUp)next).getBefore() instanceof Ice) )
				curr = ((PowerUp)next).getBefore();
			else
				curr = next;
				
			return true;
		}
		return false;
	}

	@Override
	public Direction getDirection() {
		return super.getDirection();
	}

	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getResume() {
		return resume;
	}

	public void setResume(int resume) {
		this.resume = resume;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	@Override
	public String toString() {
		return " ** ";
	}

	public boolean isDied() {
		return died;
	}

	public void setDied(boolean died) {
		this.died = died;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}
}

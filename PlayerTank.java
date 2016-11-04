package progettoIGPE.davide.giovanni.unical2016;

public class PlayerTank extends AbstractDynamicObject {

	private static int level; // 1 stella...2 stella... 3 stella
	private int resume;
	private boolean protection;
	private int point;

	public PlayerTank(int x, int y, World world) {
		super(x, y, world, Speed.NORMAL, Speed.NORMAL, Direction.STOP, 1);
		protection=false;
		resume=3;
		point=0;
		level=1;
	}
	
	@Override
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;		
		setDirection(Direction.STOP); // serve altrimenti il giocatore non si ferma
	}

	@Override
	public boolean sameObject() {
		
		if (!(next instanceof Wall) && !(next instanceof EnemyTank) && !(next instanceof PlayerTank)
				&& !(next instanceof Water) && !(next instanceof Rocket) && !(next instanceof Flag)) {
			// prendo tutti i powerUp
			if (next instanceof PowerUp) {
				curr = ((PowerUp) next).getBefore();
			}
			else
				curr=next;
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
		PlayerTank.level = level;
	}

	public int getResume() {
		return resume;
	}

	public void setResume(int resume) {
		this.resume = resume;
	}

	public boolean isProtection() {
		return protection;
	}

	public void setProtection(boolean protection) {
		this.protection = protection;
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

}

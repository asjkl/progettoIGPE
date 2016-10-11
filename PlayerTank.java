package progettoIGPE.davide.giovanni.unical2016;

public class PlayerTank extends AbstractDynamicObject {
	private static int level = 3; // 1 stella...2 stella... 3 stella
	private int resume = 3;
	private boolean protection;
	private int point = 0;

	public PlayerTank(int x, int y, World world) {
		super(x, y, world, Speed.NORMAL, Speed.NORMAL, Direction.STOP, 1);
	}

	@Override
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;
		setDirection(Direction.STOP); // serve altrimenti giocatore non si ferma
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

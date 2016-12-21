package progettoIGPE.davide.giovanni.unical2016;

public class PlayerTank extends AbstractDynamicObject {

	private static int level; // 1 stella...2 stella... 3 stella
	private int resume;
	private boolean protection;
	private int point;
	private boolean died;
	
	//DOPPIO ROCKET
	private int xTmp;
	private int yTmp;
	private Direction directionTmp;

	public PlayerTank(int x, int y, World world) {
		super(x, y, world, Speed.NORMAL, Speed.NORMAL, Direction.STOP, 1);
		protection = false;
		resume = 3;
		point = 0;
		level = 2;
		died=false;
		xTmp=-1;
		yTmp=-1;
		directionTmp=null;
		
		if(this.getSpeed()==Speed.SLOW){
			this.setCont(1);
		}else if(this.getSpeed()==Speed.NORMAL){
			this.setCont(getSizePixel()/2);
		}else{			
			this.setCont((getSizePixel()/2)+5);
		}	
	} 

	@Override
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;
		setDirection(Direction.STOP); 
	}

	@Override
	public boolean sameObject() {

		if (!(next instanceof Wall) && !(next instanceof EnemyTank) && !(next instanceof PlayerTank)
				&& !(next instanceof Water) && !(next instanceof Rocket) && !(next instanceof Flag)) {
			// prendo tutti i powerUp
			if (next instanceof PowerUp && !(((PowerUp)next).getBefore() instanceof Trees) && !(((PowerUp)next).getBefore() instanceof Ice)) 
				curr = null;
			else
			if( next instanceof PowerUp && (((PowerUp)next).getBefore() instanceof Trees || ((PowerUp)next).getBefore() instanceof Ice) )
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

	public boolean isDied() {
		return died;
	}

	public void setDied(boolean died) {
		this.died = died;
	}

	public Direction getDirectionTmp() {
		return directionTmp;
	}

	public void setDirectionTmp(Direction directionTmp) {
		this.directionTmp = directionTmp;
	}

	public int getyTmp() {
		return yTmp;
	}

	public void setyTmp(int yTmp) {
		this.yTmp = yTmp;
	}

	public int getxTmp() {
		return xTmp;
	}

	public void setxTmp(int xTmp) {
		this.xTmp = xTmp;
	}
}

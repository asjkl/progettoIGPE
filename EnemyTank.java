package progettoIGPE.davide.giovanni.unical2016;

public class EnemyTank extends Tank {
	
	private int point;
	private boolean appearsInTheMap;
	private boolean powerUpOn;
	private boolean noUpdateG;
	private boolean stopEnemy; //powerUp TIMER
	private boolean stopEnemyGraphic; //powerUp TIMER
	private boolean nextShot;
	public boolean[] directions;
	public boolean stop;
	public boolean ok = false;
	public long nextDirTime = 0;

	
	public EnemyTank(int x, int y, World world, Speed speed, Speed speedShot, Direction direction, int health, int point) {
		super(x, y, world, speed, speedShot, direction, health);
		
		this.setPoint(point);
		this.appearsInTheMap = false;
		this.powerUpOn = false;
		this.noUpdateG=false;
		this.stopEnemy=false;
		this.stopEnemyGraphic=false;
		this.setReadyToSpawn(false);
		this.setUpdateObject(true);
		this.nextShot=false;
		this.stop=false;
		
		directions = new boolean[4];
		for(int i = 0; i < directions.length; i++) {
			directions[i] = false;
		}
	}

	@Override
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;
	}

	@Override
	public boolean sameObject() {

		if(next instanceof Rocket && ((Rocket)next).getTank() instanceof EnemyTank ){
			next = ((Rocket)next).getCurr();
		}
		
		if (!(next instanceof Wall) && !(next instanceof Tank)
				&& !(next instanceof Water) && !(next instanceof Rocket) && !(next instanceof Flag)) {
			
			// prende solo Helmet
			if (next instanceof PowerUp && ((PowerUp) next).getPowerUp() == Power.HELMET && !(((PowerUp)next).getBefore() instanceof Tree)) {
				curr = null;
				setProtection(true);
			} 
			else
				if( next instanceof PowerUp && ((PowerUp) next).getPowerUp() == Power.HELMET &&( ((PowerUp)next).getBefore() instanceof Tree || ((PowerUp)next).getBefore() instanceof Ice) )
					curr = ((PowerUp)next).getBefore();
			else
				curr = next;

			return true;
		}
		return false;
	}

	public void setDir(int dir){
		 switch(dir) {
		    case 0:
		      this.setDirection(Direction.UP);
		      break;
		    case 1:
		      this.setDirection(Direction.DOWN);
		      break;
		    case 2:
		      this.setDirection(Direction.RIGHT);
		      break;
		    case 3:
		      this.setDirection(Direction.LEFT);
		      break;
		    default:
		      break;
		    }
	}
	
	public boolean isAppearsInTheMap() {
		return appearsInTheMap;
	}

	public void setAppearsInTheMap(boolean appearsInTheMap) {
		this.appearsInTheMap = appearsInTheMap;
	}

	public boolean isPowerUpOn() {
		return powerUpOn;
	}

	public void setPowerUpOn(boolean powerUpOn) {
		this.powerUpOn = powerUpOn;
	}
	
	public boolean isNoUpdateG() {
		return noUpdateG;
	}

	public void setNoUpdateG(boolean noUpdateG) {
		this.noUpdateG = noUpdateG;
	}

	public boolean isStopEnemy() {
		return stopEnemy;
	}

	public void setStopEnemy(boolean stopEnemy) {
		this.stopEnemy = stopEnemy;
	}

	public boolean isStopEnemyGraphic() {
		return stopEnemyGraphic;
	}

	public void setStopEnemyGraphic(boolean stopEnemyGraphic) {
		this.stopEnemyGraphic = stopEnemyGraphic;
	}

	public boolean isNextShot() {
		return nextShot;
	}

	public void setNextShot(boolean nextShot) {
		this.nextShot = nextShot;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
}

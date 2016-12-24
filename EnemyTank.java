package progettoIGPE.davide.giovanni.unical2016;

import java.util.Random;

public class EnemyTank extends AbstractDynamicObject {
	
	private int point;
	private int step;
	private int countStep;
	private boolean recoverValue;
	private int tempX;
	private int tempY;
	private boolean directionUp;
	private boolean directionDown;
	private boolean directionLeft;
	private boolean directionRight;
	private boolean appearsInTheMap;
	private boolean protection;
	private boolean powerUpOn;
	private boolean noUpdateG;
	private boolean destroy;
	private boolean stopEnemy; //powerUp TIMER
	private boolean stopEnemyGraphic; //powerUp TIMER
	
	private boolean readyToSpawn;
	private int contT;

	public EnemyTank(int x, int y, World world, Speed speed, Speed speedShot, Direction direction, int health, int point) {
		super(x, y, world, speed, speedShot, direction, health);
		this.point = point;
		step = 0;
		countStep = 0;
		recoverValue = false;
		tempX = x;
		tempY = y;
		directionUp = false;
		directionDown = false;
		directionLeft = false;
		directionRight = false;
		appearsInTheMap = false;
		protection = false;
		powerUpOn = false;
		noUpdateG=false;
		destroy=false;
		stopEnemy=false;
		stopEnemyGraphic=false;
		readyToSpawn = false;
		contT=1;
		
		setUpdateObject(true);
		if(speed==Speed.SLOW){
			this.setCont(1);
		}else if(speed==Speed.NORMAL){
			this.setCont(getSizePixel()/2);
		}else{			
			this.setCont((getSizePixel()/2)+5);
		}
	}

	@Override
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;
	}

	@Override
	public boolean sameObject() {
		if (!(next instanceof Wall) && !(next instanceof EnemyTank) && !(next instanceof PlayerTank)
				&& !(next instanceof Water) && !(next instanceof Rocket) && !(next instanceof Flag)) {
			
			// prende solo Helmet
			if (next instanceof PowerUp && ((PowerUp) next).getPowerUp() == Power.HELMET && !(((PowerUp)next).getBefore() instanceof Trees)) {
				curr = null;
				protection=true;
			} 
			else
				if( next instanceof PowerUp && ((PowerUp) next).getPowerUp() == Power.HELMET &&( ((PowerUp)next).getBefore() instanceof Trees || ((PowerUp)next).getBefore() instanceof Ice) )
					curr = ((PowerUp)next).getBefore();
			else
				curr = next;

			return true;
		}
		return false;
	}

	public void setPositionXY() {
		this.tempX = getX();
		this.tempY = getY();
	}

	public void setPositionDirection() {
		directionRight = false;
		directionLeft = false;
		directionDown = false;
		directionUp = false;
		if (getDirection() == Direction.UP) {
			// System.out.println("ENTRATO1");
			directionUp = true;
		} else if (getDirection() == Direction.DOWN) {
			// System.out.println("ENTRATO2");
			directionDown = true;
		} else if (getDirection() == Direction.LEFT) {
			// System.out.println("ENTRATO3");
			directionLeft = true;
		} else if (getDirection() == Direction.RIGHT) {
			// System.out.println("ENTRATO4");
			directionRight = true;
		}

	}

	public boolean notSamePosition() {
		if (getDirection() == Direction.UP && isDirectionUp()) {
			return false;
		} else if (getDirection() == Direction.DOWN && isDirectionDown()) {
			return false;
		} else if (getDirection() == Direction.LEFT && isDirectionLeft()) {
			return false;
		} else if (getDirection() == Direction.RIGHT && isDirectionRight()) {
			return false;
		}
		return true;
	}

	public boolean positionCorrect() {
		int x = getX();
		int y = getY();
		AbstractStaticObject tmp;
		if (getDirection() == Direction.UP && x - 1 >= 0) {
			tmp = world.world[x - 1][y];
			if (tmp instanceof Wall || tmp instanceof EnemyTank || tmp instanceof PlayerTank || getX() == 0) {
				return false;
			}

		} else if (getDirection() == Direction.DOWN && x + 1 < world.getRow()) {
			tmp = world.world[x + 1][y];
			if (tmp instanceof Wall || tmp instanceof EnemyTank || tmp instanceof PlayerTank
					|| getX() == world.getRow() - 1) {
				return false;
			}

		} else if (getDirection() == Direction.LEFT && y - 1 >= 0) {
			tmp = world.world[x][y - 1];
			if (tmp instanceof Wall || tmp instanceof EnemyTank || tmp instanceof PlayerTank || getY() == 0) {
				return false;
			}
		} else if (getDirection() == Direction.RIGHT && y + 1 < world.getColumn()) {
			tmp = world.world[x][y + 1];
			if (tmp instanceof Wall || tmp instanceof EnemyTank || tmp instanceof PlayerTank
					|| getY() == world.getColumn() - 1) {
				return false;
			}
		}

		return true;
	}

	public void directionEnemyRandom() {
		Random random = new Random();
		switch (random.nextInt(4)) {
		case 0:
			if (!isDirectionUp()) {
				setDirection(Direction.UP);
				setDirectionUp(true);
			}
			break;
		case 1:
			if (!isDirectionDown()) {
				setDirection(Direction.DOWN);
				setDirectionDown(true);
			}
			break;
		case 2:
			if (!isDirectionLeft()) {
				setDirection(Direction.LEFT);
				setDirectionLeft(true);
			}
			break;
		case 3:
			if (!isDirectionRight()) {
				setDirection(Direction.RIGHT);
				setDirectionRight(true);
			}
			break;
		default:
			break;

		}
	}

	public boolean allTrue() {
		if (directionDown && directionRight && directionUp && directionDown) {
			directionRight = false;
			directionLeft = false;
			directionDown = false;
			directionUp = false;
			return true;
		}
		return false;
	}

	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}

	public boolean isDirectionUp() {
		return directionUp;
	}

	public void setDirectionUp(boolean directionUp) {
		this.directionUp = directionUp;
	}

	public boolean isDirectionDown() {
		return directionDown;
	}

	public void setDirectionDown(boolean directionDown) {
		this.directionDown = directionDown;
	}

	public boolean isDirectionLeft() {
		return directionLeft;
	}

	public void setDirectionLeft(boolean directionLeft) {
		this.directionLeft = directionLeft;
	}

	public boolean isDirectionRight() {
		return directionRight;
	}

	public void setDirectionRight(boolean directionRight) {
		this.directionRight = directionRight;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getCountStep() {
		return countStep;
	}

	public void setCountStep(int countStep) {
		this.countStep = countStep;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public boolean isRecoverValue() {
		return recoverValue;
	}

	public void setRecoverValue(boolean recoverValue) {
		this.recoverValue = recoverValue;
	}

	public int getTempX() {
		return tempX;
	}

	public void setTempX(int tempX) {
		this.tempX = tempX;
	}

	public int getTempY() {
		return tempY;
	}

	public void setTempY(int tempY) {
		this.tempY = tempY;
	}

	public boolean isAppearsInTheMap() {
		return appearsInTheMap;
	}

	public void setAppearsInTheMap(boolean appearsInTheMap) {
		this.appearsInTheMap = appearsInTheMap;
	}

	public boolean isProtection() {
		return protection;
	}

	public void setProtection(boolean protection) {
		this.protection = protection;
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

	public boolean isDestroy() {
		return destroy;
	}

	public void setDestroy(boolean destroy) {
		this.destroy = destroy;
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

	public boolean isReadyToSpawn() {
		return readyToSpawn;
	}

	public void setReadyToSpawn(boolean readyToSpawn) {
		this.readyToSpawn = readyToSpawn;
	}

	public int getContT() {
		return contT;
	}

	public void setContT(int contT) {
		this.contT = contT;
	}
}

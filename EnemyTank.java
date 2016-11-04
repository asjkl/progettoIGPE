package progettoIGPE.davide.giovanni.unical2016;

import java.util.Random;

public class EnemyTank extends AbstractDynamicObject {
	private int point;
	private int passi;
	private int contatorePassi;
	private boolean riprendoValori;
	private int tempX;
	private int tempY;
	private boolean directionUp;
	private boolean directionDown;
	private boolean directionLeft;
	private boolean directionRight;

	public EnemyTank(int x, int y, World world, Speed speed, Speed speedShot, Direction direction, int health,int point) {
		super(x, y, world, speed, speedShot, direction, health);
		this.point = point;
		passi = 0;
		contatorePassi = 0;
		riprendoValori = false;
		tempX = x;
		tempY = y;
		directionUp = false;
		directionDown = false;
		directionLeft = false;
		directionRight = false;
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
			// prendo solo Helmet
			if (next instanceof PowerUp && ((PowerUp) next).getPowerUp() == Power.HELMET) {
				curr = ((PowerUp) next).getBefore();
			} 
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

	@Override
	public String toString() {
		return "E";
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

	public int getPassi() {
		return passi;
	}

	public void setPassi(int passi) {
		this.passi = passi;
	}

	public int getContatorePassi() {
		return contatorePassi;
	}

	public void setContatorePassi(int contatorePassi) {
		this.contatorePassi = contatorePassi;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public boolean isRiprendoValori() {
		return riprendoValori;
	}

	public void setRiprendoValori(boolean riprendoValori) {
		this.riprendoValori = riprendoValori;
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
}

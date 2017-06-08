package progettoIGPE.davide.giovanni.unical2016;

public class BasicTank extends EnemyTank {

	public BasicTank(int x, int y, World mondo, Direction direction) {
		super(x, y, mondo, Speed.SLOW, Speed.SLOWROCKET, direction, 1, 100);
	}

	@Override
	public String toString() {
		return " BT ";
	}

}

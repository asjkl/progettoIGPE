package core;

public class PowerTank extends EnemyTank {

	public PowerTank(int x, int y, World mondo, Direction direction,int numOfPlayers) {
		super(x, y, mondo, Speed.NORMAL, Speed.FASTROCKET, direction, 1, 300, numOfPlayers);
	}

	@Override
	public String toString() {
		return " PT ";
	}
}

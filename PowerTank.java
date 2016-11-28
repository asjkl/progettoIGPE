package progettoIGPE.davide.giovanni.unical2016;

public class PowerTank extends EnemyTank {

	public PowerTank(int x, int y, World mondo, Direction direction) {
		super(x, y, mondo, Speed.NORMAL, Speed.FAST, direction, 1, 300);
	}

	@Override
	public String toString() {
		return " PT ";
	}
}

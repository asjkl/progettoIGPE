package progettoIGPE.davide.giovanni.unical2016;

public class PowerTank extends EnemyTank {

	public PowerTank(int x, int y, World mondo, Direction direction,int numOfPlayers, int c) {
		super(x, y, mondo, Speed.NORMAL, Speed.FASTROCKET, direction, 1, 300, numOfPlayers, c);
	}

	@Override
	public String toString() {
		return " PT"+getNameEnemy();
	}
}

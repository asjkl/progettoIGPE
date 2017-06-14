package progettoIGPE.davide.giovanni.unical2016;

public class FastTank extends EnemyTank {

	public FastTank(int x, int y, World world, Direction direction, int numOfPlayers) {
		super(x, y, world, Speed.FAST, Speed.NORMALROCKET, direction, 1, 200, numOfPlayers);
	}

	@Override
	public String toString() {
		return " FT ";
	}

}

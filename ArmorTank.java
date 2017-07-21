package core;

public class ArmorTank extends EnemyTank {

	public ArmorTank(int x, int y, World mondo, Direction direction, int numOfPlayers) {
		super(x, y, mondo, Speed.SLOW, Speed.NORMALROCKET, direction, 4, 400, numOfPlayers);
	}

	@Override
	public String toString (){
		return " AT ";
	}
}

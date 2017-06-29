package progettoIGPE.davide.giovanni.unical2016;

public class ArmorTank extends EnemyTank {

	public ArmorTank(int x, int y, World mondo, Direction direction, int numOfPlayers, int c) {
		super(x, y, mondo, Speed.SLOW, Speed.NORMALROCKET, direction, 4, 400, numOfPlayers, c);
	}

	@Override
	public String toString (){
		return " AT"+getNameEnemy();
	}
}

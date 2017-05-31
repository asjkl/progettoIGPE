package progettoIGPE.davide.giovanni.unical2016;

public class ArmorTank extends EnemyTank {

	public ArmorTank(int x, int y, World mondo, Direction direction) {
		super(x, y, mondo, Speed.SLOW, Speed.NORMAL, direction, 4, 400);
	}

	@Override
	public String toString (){
		return " AT " ;
	}
}

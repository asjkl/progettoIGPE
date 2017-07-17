package progettoIGPE.davide.giovanni.unical2016;

public class World {
	
	private int row;
	private int column;
	private AbstractStaticObject[][] world;
	private AbstractStaticObject[][] objectStatic;

	public World(int row, int column) {
		this.row = row;
		this.column = column;
		this.setWorld(new AbstractStaticObject[row][column]);
		this.setObjectStatic(new AbstractStaticObject[row][column]);
	}

	public World() {
		row = 20;
		column = 21;
		this.setWorld(new AbstractStaticObject[row][column]);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void print() {
		for (int a = 0; a < row; a++) {
			for (int b = 0; b < column; b++) {
				System.out.print(getWorld()[a][b]);
			}
			System.out.println();
		}
	}

	public AbstractStaticObject[][] getObjectStatic() {
		return objectStatic;
	}

	public void setObjectStatic(AbstractStaticObject[][] objectStatic) {
		this.objectStatic = objectStatic;
	}

	public AbstractStaticObject[][] getWorld() {
		return world;
	}

	public void setWorld(AbstractStaticObject[][] world) {
		this.world = world;
	}



}

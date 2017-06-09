package progettoIGPE.davide.giovanni.unical2016;

public class World {
	private int row;
	private int column;
	public AbstractStaticObject[][] world;
	public AbstractStaticObject[][] objectStatic;

	public World(int row, int column) {
		this.row = row;
		this.column = column;
		this.world = new AbstractStaticObject[row][column];
		this.objectStatic=new AbstractStaticObject[row][column];
	}

	public World() {
		row = 20;
		column = 21;
		this.world = new AbstractStaticObject[row][column];
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
				System.out.print(world[a][b]);
			}
			System.out.println();
		}
	}
}

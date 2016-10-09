package progettoIGPE.davide.giovanni.unical2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

public class World {
	private int row;
	private int column;
	AbstractStaticObject[][] world;

	public World(int row, int column) {
		this.row = row;
		this.column = column;
		world = new AbstractStaticObject[row][column];
		importMatrix();
	}

	public World() {
		row = 20;
		column = 20;
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

	public void importMatrix() {
		int i = 0;// indice di riga

		try {
			BufferedReader reader = new BufferedReader(new FileReader("C:/Users/Giovanni/Desktop/mappa.txt"));// Cambiare
																												// percorso
																												// file
																												// in
																												// base
																												// alla
																												// cartella
			String line = reader.readLine();
			while (line != null) {

				StringTokenizer st = new StringTokenizer(line, " ");
				int j = 0;// indice di colonna
				String tmp = st.nextToken();

				while (st.hasMoreTokens()) {
					if (tmp.equals("null"))
						world[i][j] = null;
					else if (tmp.equals("[//]"))
						world[i][j] = new SteelWall(i, j, this, 4);
					else if (tmp.equals("@@@@"))
						world[i][j] = new Ice(i, j, this);
					else if (tmp.equals("TTTT"))
						world[i][j] = new Trees(i, j, this);
					else if (tmp.equals("[||]"))
						world[i][j] = new BrickWall(i, j, this, 2);
					else if (tmp.equals("~~~~"))
						world[i][j] = new Water(i, j, this);
					else if (tmp.equals("FLAG"))
						world[i][j] = new Flag(i, j, this, true);
					j++;
					tmp = st.nextToken();
				} // while

				i++;
				line = reader.readLine();
			} // while

			reader.close();
		} // try
		catch (Exception e) {

			e.printStackTrace();

		}

	}
}

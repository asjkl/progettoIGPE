package progettoIGPE.davide.giovanni.unical2016;

import java.awt.Point;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;


public class EnemyTank extends Tank {

	private int point;
	private boolean appearsInTheMap;
	private boolean powerUpOn;
	private boolean noUpdateG;
	private boolean stopEnemy; // powerUp TIMER
	private boolean stopEnemyGraphic; // powerUp TIMER
	private boolean nextShot;
	public boolean[] directions;
	public boolean stop;
	public boolean ok = false;
	public long nextDirTime = 0;

	/////////////////// DIFFICULT ////////////////////////////////
	private boolean [][]minimalRoute;
	private ArrayList<Point> blocchi=new ArrayList<>();
	private Point flag;

	// Blocked cells are just null Cell values in grid
	private Cell[][] grid;

	private PriorityQueue<Cell> open;

	private boolean closed[][];
	private int startI, startJ;
	private int endI, endJ;
	
	
	public EnemyTank(int x, int y, World world, Speed speed, Speed speedShot, Direction direction, int health,
			int point) {
		super(x, y, world, speed, speedShot, direction, health);

		this.setPoint(point);
		this.appearsInTheMap = false;
		this.powerUpOn = false;
		this.noUpdateG = false;
		this.stopEnemy = false;
		this.stopEnemyGraphic = false;
		this.setReadyToSpawn(false);
		this.setUpdateObject(true);
		this.nextShot = false;
		this.stop = false;
		this.grid = new Cell[5][5];

		directions = new boolean[4];
		for (int i = 0; i < directions.length; i++) {
			directions[i] = false;
		}

		for(int a=0; a<world.getRow(); a++){
			for(int b=0; b<world.getColumn(); b++){
				if(world.world[a][b]!=null && !(world.world[a][b] instanceof Flag)){
					blocchi.add(new Point(a,b));
				}
				if(world.world[a][b] instanceof Flag){
					flag=new Point(a, b);
				}
			}
		}
			
		//tiene traccia del percorso
		minimalRoute = new boolean[world.getRow()][world.getColumn()];
		for(int i = 0;i < world.getRow(); i++) {
			for(int j = 0;j < world.getColumn(); j++) {
				minimalRoute[i][j] = false;
			}
		}
		
		test(1, world.getRow(), world.getColumn(), getX(), getY(), (int)flag.getX(),(int)flag.getY(), blocchi);
	}

	@Override	
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;
	}
	
	@Override
	public boolean sameObject() {

		if (next instanceof Rocket && ((Rocket) next).getTank() instanceof EnemyTank) {
			next = ((Rocket) next).getCurr();
		}

		if (!(next instanceof Wall) && !(next instanceof Tank) && !(next instanceof Water) && !(next instanceof Rocket)
				&& !(next instanceof Flag)) {

			// prende solo Helmet
			if (next instanceof PowerUp && ((PowerUp) next).getPowerUp() == Power.HELMET
					&& !(((PowerUp) next).getBefore() instanceof Tree)) {
				curr = null;
				setProtection(true);
			} else if (next instanceof PowerUp && ((PowerUp) next).getPowerUp() == Power.HELMET
					&& (((PowerUp) next).getBefore() instanceof Tree || ((PowerUp) next).getBefore() instanceof Ice))
				curr = ((PowerUp) next).getBefore();
			else
				curr = next;

			return true;
		}
		return false;
	}
	////////////////////////////// EASY ////////////////////////////////////////////////////////////////////////
	
	public void easy() {
		
		 if(!canGo && !ok){		 	
		 		chooseDirection();
				 int dir = -1;
			     do { 
			    	 dir = new Random().nextInt(4); 
			     } while(!directions[dir]);
			    setDir(dir);
			    ok=true;
			    nextDirTime = ((GameManager.currentTime + 1)%60);
		 }
		 
		 if( nextDirTime == GameManager.currentTime)
			ok=false;
	 }
	
	public void chooseDirection() {
	    
		int x = getX();
		int y = getY();
		int left, right, up, down;
		
		//up
		if(x > 0) {
			up = x - 1;
			if(!(getWorld().world[up][y] instanceof SteelWall)  &&
					 !(getWorld().world[up][y] instanceof Water)) {
			directions[0] = true;
			}
			else
				directions[0] = false;
		}
		else
			directions[0] = false;
			
		//down
		if(x < getWorld().getRow() - 1) {
			down = x + 1;
			if(!(getWorld().world[down][y] instanceof SteelWall)
				 && !(getWorld().world[down][y] instanceof Water)){
			directions[1] = true;
	
			}
			else
				directions[1] = false;
		}
		else
			directions[1] = false;
		
		//right
		if(y < getWorld().getColumn() - 1) {
			right = y + 1;
			if(!(getWorld().world[x][right] instanceof SteelWall) &&
				  !(getWorld().world[x][right] instanceof Water)){
			directions[2] = true;
			}
			else
				directions[2] = false;
		}
		else
			directions[2] = false;
		
		//left
		if(y > 0) {
			left = y - 1;
			if(!(getWorld().world[x][left] instanceof SteelWall) &&
				 !(getWorld().world[x][left] instanceof Water)){
			directions[3] = true;
			}
			else
				directions[3] = false;
		}
		else
			directions[3] = false;
	}
	////////////////////////////// MEDIUM //////////////////////////////////////////////////////////////////////
	
	////////////////////////////// DIFFICULT ///////////////////////////////////////////////////////////////////
	
	//public final int DIAGONAL_COST = 14;
	public final int V_H_COST = 5;

	class Cell {
		int heuristicCost = 0; // Heuristic cost
		int finalCost = 0; // G+H
		int i, j;
		Cell parent;

		Cell(int i, int j) {
			this.i = i;
			this.j = j;
		}

		@Override
		public String toString() {
			return "[" + this.i + ", " + this.j + "]";
		}
	}

	public void setBlocked(int i, int j) {
		grid[i][j] = null;
	}

	public void setStartCell(int i, int j) {
		startI = i;
		startJ = j;
	}

	public void setEndCell(int i, int j) {
		endI = i;
		endJ = j;
	}

	void checkAndUpdateCost(Cell current, Cell t, int cost) {
		
		//se t è nullo o close[i][j] è stato gia visitato
		if (t == null || closed[t.i][t.j])
			return;
		
		int t_final_cost = t.heuristicCost + cost;

		boolean inOpen = open.contains(t);
		if (!inOpen || t_final_cost < t.finalCost) {
			t.finalCost = t_final_cost;
			t.parent = current;
			//new route
			if (!inOpen)
				open.add(t);
		}
	}

	public void AStar() {

		// add the start location to open list.
		open.add(grid[startI][startJ]);

		Cell current;

		while (true) {
			current = open.poll();
			if (current == null)
				break;
			//set true corrent pos
			closed[current.i][current.j] = true;

			//if near flag return
			if (current.equals(grid[endI][endJ])) {
				return;
			}

			Cell t;
			if (current.i - 1 >= 0) {
				t = grid[current.i - 1][current.j];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
			}

			if (current.j - 1 >= 0) {
				t = grid[current.i][current.j - 1];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
			}

			if (current.j + 1 < grid[0].length) {
				t = grid[current.i][current.j + 1];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
			}

			if (current.i + 1 < grid.length) {
				t = grid[current.i + 1][current.j];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
			}
		}
	}

	/*
	 * Params : tCase = test case No. x, y = Board's dimensions si, sj =
	 * start location's x and y coordinates ei, ej = end location's x and y
	 * coordinates int[][] blocked = array containing inaccessible cell
	 * coordinates
	 */
	public void test(int tCase, int x, int y, int si, int sj, int ei, int ej, ArrayList<Point> blocchi2) {
		System.out.println("\n\nTest Case #" + tCase);
		// Reset
		grid = new Cell[x][y];
		closed = new boolean[x][y];
		open = new PriorityQueue<>((Object o1, Object o2) -> {
			Cell c1 = (Cell) o1;
			Cell c2 = (Cell) o2;

			return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
		});
		// Set start position
		setStartCell(si, sj); // Setting to 0,0 by default. Will be useful
								// for the UI part

		// Set End Location
		setEndCell(ei, ej);

		for (int i = 0; i < x; ++i) {
			for (int j = 0; j < y; ++j) {
				grid[i][j] = new Cell(i, j);
				grid[i][j].heuristicCost = Math.abs(i - endI) + Math.abs(j - endJ);
				// System.out.print(grid[i][j].heuristicCost+" ");
			}
			// System.out.println();
		}
		grid[si][sj].finalCost = 0;
		
		/*
		 * Set blocked cells. Simply set the cell values to null for blocked
		 * cells.
		 */
		for (int i = 0; i < blocchi2.size(); ++i) {
			setBlocked((int)blocchi2.get(i).getX(), (int)blocchi2.get(i).getY());
		}

		// Display initial map
		System.out.println("Grid: ");
		for (int i = 0; i < x; ++i) {
			for (int j = 0; j < y; ++j) {
				if (i == si && j == sj)
					System.out.print("SO  "); // Source
				else if (i == ei && j == ej)
					System.out.print("DE  "); // Destination
				else if (grid[i][j] != null)
					System.out.printf("%-3d ", 0);
				else
					System.out.print("BL  ");
			}
			System.out.println();
		}
		System.out.println();

		AStar();
		System.out.println("\nScores for cells: ");
		for (int i = 0; i < x; ++i) {
			for (int j = 0; j < x; ++j) {
				if (grid[i][j] != null)
					System.out.printf("%-3d ", grid[i][j].finalCost);
				else
					System.out.print("BL  ");
			}
			System.out.println();
		}
		System.out.println();

		if (closed[endI][endJ]) {
			// Trace back the path
			System.out.println("Path: ");
	
			Cell current = grid[endI][endJ];
			
			minimalRoute[current.i][current.j] = true; //traccia il percorso minimo
			
			System.out.print(current);
			while (current.parent != null) {
				System.out.print(" -> " + current.parent);
				current = current.parent;
				minimalRoute[current.i][current.j] = true;
			}
			System.out.println();
		} else
			System.out.println("No possible path");
	}
	
	public void difficult() {
		
		int currX = getX();
		int currY = getY();
		
		minimalRoute[currX][currY] = false;
		
		if(currX - 1 >= 0) {
			if(minimalRoute[currX - 1][currY]) {
				setDirection(Direction.UP);
			}
		}
		
		if (currY - 1 >= 0) {
			if(minimalRoute[currX][currY - 1]) {
			setDirection(Direction.LEFT);
			}
		}
		
		if (currY + 1 < world.getColumn() - 1) {
			if(minimalRoute[currX][currY + 1]) {
				setDirection(Direction.RIGHT);
			}
		}
	
		if (currX + 1 < world.getRow() - 1) {
				if(minimalRoute[currX + 1][currY]) {
					setDirection(Direction.DOWN);
				}
			}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void setDir(int dir) {
		switch (dir) {
		case 0:
			this.setDirection(Direction.UP);
			break;
		case 1:
			this.setDirection(Direction.DOWN);
			break;
		case 2:
			this.setDirection(Direction.RIGHT);
			break;
		case 3:
			this.setDirection(Direction.LEFT);
			break;
		default:
			this.setDirection(Direction.STOP);
			break;
		}
	}

	public boolean isAppearsInTheMap() {
		return appearsInTheMap;
	}

	public void setAppearsInTheMap(boolean appearsInTheMap) {
		this.appearsInTheMap = appearsInTheMap;
	}

	public boolean isPowerUpOn() {
		return powerUpOn;
	}

	public void setPowerUpOn(boolean powerUpOn) {
		this.powerUpOn = powerUpOn;
	}

	public boolean isNoUpdateG() {
		return noUpdateG;
	}

	public void setNoUpdateG(boolean noUpdateG) {
		this.noUpdateG = noUpdateG;
	}

	public boolean isStopEnemy() {
		return stopEnemy;
	}

	public void setStopEnemy(boolean stopEnemy) {
		this.stopEnemy = stopEnemy;
	}

	public boolean isStopEnemyGraphic() {
		return stopEnemyGraphic;
	}

	public void setStopEnemyGraphic(boolean stopEnemyGraphic) {
		this.stopEnemyGraphic = stopEnemyGraphic;
	}

	public boolean isNextShot() {
		return nextShot;
	}

	public void setNextShot(boolean nextShot) {
		this.nextShot = nextShot;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
}

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
	private boolean firstShot;
	private long nextShotTime;
	private boolean[] directions;
	private boolean ok = false;
	private long nextDirTime = 0;

	/////////////////// DIFFICULT ////////////////////////////////
	private ArrayList<Point> blocchi = new ArrayList<>();
	private Cell[][] grid;
	private boolean[][] minimalRoute;
	private PriorityQueue<Cell> open;
	private Point flag;
	private boolean closed[][];
	private int startI, startJ;
	private int endI, endJ;
	public final int V_H_COST = 5;

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
		this.setFirstShot(true);
		this.nextShotTime = 0;
		this.grid = new Cell[5][5];

		directions = new boolean[4];
		for (int i = 0; i < directions.length; i++) {
			directions[i] = false;
		}
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
	////////////////////////////// EASY
	////////////////////////////// ////////////////////////////////////////////////////////////////////////

	public void easy() {

		if (!canGo && !ok) {
			chooseDirection();
			int dir = -1;
			do {
				dir = new Random().nextInt(4);
			} while (!directions[dir]);
			setDir(dir);
			ok = true;
			nextDirTime = ((GameManager.currentTime + 1) % 60);
		}

		if (nextDirTime == GameManager.currentTime)
			ok = false;
	}

	public void chooseDirection() {

		int x = getX();
		int y = getY();
		int left, right, up, down;

		// up
		if (x > 0) {
			up = x - 1;
			if (!(getWorld().world[up][y] instanceof SteelWall) && !(getWorld().world[up][y] instanceof Water)) {
				directions[0] = true;
			} else
				directions[0] = false;
		} else
			directions[0] = false;

		// down
		if (x < getWorld().getRow() - 1) {
			down = x + 1;
			if (!(getWorld().world[down][y] instanceof SteelWall) && !(getWorld().world[down][y] instanceof Water)) {
				directions[1] = true;

			} else
				directions[1] = false;
		} else
			directions[1] = false;

		// right
		if (y < getWorld().getColumn() - 1) {
			right = y + 1;
			if (!(getWorld().world[x][right] instanceof SteelWall) && !(getWorld().world[x][right] instanceof Water)) {
				directions[2] = true;
			} else
				directions[2] = false;
		} else
			directions[2] = false;

		// left
		if (y > 0) {
			left = y - 1;
			if (!(getWorld().world[x][left] instanceof SteelWall) && !(getWorld().world[x][left] instanceof Water)) {
				directions[3] = true;
			} else
				directions[3] = false;
		} else
			directions[3] = false;
	}
	////////////////////////////// MEDIUM
	////////////////////////////// //////////////////////////////////////////////////////////////////////

	////////////////////////////// DIFFICULT
	////////////////////////////// ///////////////////////////////////////////////////////////////////

	void checkAndUpdateCost(Cell current, Cell t, int cost) {

		// se t Ã¨ nullo o close[i][j] Ã¨ stato gia visitato
		if (t == null || closed[t.i][t.j])
			return;

		int t_final_cost = t.heuristicCost + cost;

		boolean inOpen = open.contains(t);
		if (!inOpen || t_final_cost < t.finalCost) {
			t.finalCost = t_final_cost;
			t.parent = current;
			// new route
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
			// set true corrent pos
			closed[current.i][current.j] = true;

			// if near flag return
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

	public void searchRoute(int x, int y, int si, int sj, int ei, int ej, ArrayList<Point> blocchi2) {
		grid = new Cell[x][y];
		closed = new boolean[x][y];
		open = new PriorityQueue<>((Object o1, Object o2) -> {
			Cell c1 = (Cell) o1;
			Cell c2 = (Cell) o2;

			return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
		});
		setStartCell(si, sj); 
		setEndCell(ei, ej);

		for (int i = 0; i < x; ++i) {
			for (int j = 0; j < y; ++j) {
				grid[i][j] = new Cell(i, j);
				grid[i][j].heuristicCost = Math.abs(i - endI) + Math.abs(j - endJ);
			}
		}
		grid[si][sj].finalCost = 0;

		for (int i = 0; i < blocchi2.size(); ++i) {
			setBlocked((int)blocchi2.get(i).getX(), (int)blocchi2.get(i).getY());
		}
		AStar();

		if (closed[endI][endJ]) {
			Cell current = grid[endI][endJ];
			minimalRoute[current.i][current.j] = true; 
			while (current.parent != null) {
				//System.out.print(current+" ");
				current = current.parent;
				minimalRoute[current.i][current.j] = true;
			}
		} else{
			//NON HO TROVATO UN PERCORSO LIBERO, QUINDI DEVO COMINCIARE A FARMI UN PERCORSO DISTRUGGENDO LE MURA
			//TODO NON è FINITO ANCORA; LA RICORSIONE SERVE PERCHè DEVO RIFARE TUTTO DALL'INIZIO RESETTANDO TUTTO PERò IN QUESTO CASO CAMBIA SOLO L'ARRAY DI BLOCCHI
			blocchi.clear();
			for(int a=0; a<world.getRow(); a++){
				for(int b=0; b<world.getColumn(); b++){
					if(world.world[a][b] != null && !(world.world[a][b] instanceof Flag)
							&& !(world.world[a][b] instanceof Tree) && !(world.world[a][b] instanceof Ice)
							&& !(world.world[a][b] instanceof BrickWall) && world.world[a][b] != this){
							blocchi.add(new Point(a,b));
					}
				}
			}
			searchRoute(world.getRow(), world.getColumn(), getX(), getY(), (int) flag.getX(), (int) flag.getY(), blocchi);
		}
			
	}

	public void difficult() {
		blocchi.clear();

		for (int a = 0; a < world.getRow(); a++) {
			for (int b = 0; b < world.getColumn(); b++) {
				if (world.world[a][b] != null && !(world.world[a][b] instanceof Flag)
						&& !(world.world[a][b] instanceof Tree) && !(world.world[a][b] instanceof Ice)
						&& world.world[a][b] != this) {
					blocchi.add(new Point(a, b));
				}
				if (world.world[a][b] instanceof Flag) {
					flag = new Point(a, b);
				}
			}
		}
		minimalRoute = new boolean[world.getRow()][world.getColumn()];
		for (int i = 0; i < world.getRow(); i++) {
			for (int j = 0; j < world.getColumn(); j++) {
				minimalRoute[i][j] = false;
			}
		}

		searchRoute(world.getRow(), world.getColumn(), getX(), getY(), (int) flag.getX(), (int) flag.getY(), blocchi);

		int currX = getX();
		int currY = getY();

		minimalRoute[currX][currY] = false;

		if (currX - 1 >= 0) {
			if (minimalRoute[currX - 1][currY]) {
				setDirection(Direction.UP);
			}
		}

		if (currY - 1 >= 0) {
			if (minimalRoute[currX][currY - 1]) {
				setDirection(Direction.LEFT);
			}
		}

		if (currY + 1 < world.getColumn() - 1) {
			if (minimalRoute[currX][currY + 1]) {
				setDirection(Direction.RIGHT);
			}
		}

		if (currX + 1 < world.getRow() - 1) {
			if (minimalRoute[currX + 1][currY]) {
				setDirection(Direction.DOWN);
			}
		}
	}

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

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public long getNextShotTime() {
		return nextShotTime;
	}

	public void setNextShotTime(long nextShotTime) {
		this.nextShotTime = nextShotTime;
	}

	public boolean isFirstShot() {
		return firstShot;
	}

	public void setFirstShot(boolean firstShot) {
		this.firstShot = firstShot;
	}
}

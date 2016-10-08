package progettoIGPE.davide.giovanni.unical2016;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameManager {
	private int x;
	private int y;
	private int contEnemy = 0;
	private Direction direction;
	private static final int size = 20; // non modificare
	private Random random = new Random();
	private World matrix;
	private static PlayerTank player;
	private static ArrayList<EnemyTank> enemy;
	private ArrayList<PowerUp> power;
	private static ArrayList<AllWall> wall;
	private Flag flag;
	private Direction tmp = Direction.STOP;
	
	public static void main(String[] args) {

		GameManager game = new GameManager();
		game.randomEnemy(10); // quanti soldati generare
		updateObjects(game); // muovi playerTank
	}

	public static void updateObjects(GameManager game) {

		// lo scanner deve essere chiuso alla fine
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		String c;

		game.matrix.stampa();
		c = s.nextLine();
		while (true) {

			switch (c) {
			case "w": // up
				GameManager.player.setDirection(Direction.UP);
				game.tmp = Direction.UP; // IN TMP RIMANE LA DIREZIONE PRECEDENTE PERCHè ABBIAMO SETTATO
											// AD OGNI CICLO LO STOP DEL PLAYER
				break;
			case "a": // sx
				GameManager.player.setDirection(Direction.LEFT);
				game.tmp = Direction.LEFT;
				break;
			case "d": // dx
				GameManager.player.setDirection(Direction.RIGHT);
				game.tmp = Direction.RIGHT;
				break;
			case "s": // down
				GameManager.player.setDirection(Direction.DOWN);
				game.tmp = Direction.DOWN;
				break;

			case "r": // ROCKET
				game.moveRocket();
				GameManager.player.getRocket().setShot(true);
				break;

			default:
				break;
			}
			
			System.out.println("---->  Numero Nemici rimasti: " + enemy.size());
			
			if (GameManager.player.getRocket().isShot())
				GameManager.player.getRocket().update();
				
			game.enemyPositionRandom();
			GameManager.player.update();
			
			if( enemy.size() > 0)
			game.matrix.stampa();
			else
			{
				System.out.println();System.out.println();System.out.println();System.out.println();
				System.out.println(" ---------------------------  GAME OVER  -------------------------- ");
				System.out.println();System.out.println();System.out.println();System.out.println();
				break;
			}
			c = s.nextLine();
		}
	}

	public GameManager() {
		matrix = new World(size, size);
		flag = new Flag(size - 1, size / 2, matrix, true);
		matrix.world[size - 1][size / 2] = flag;
		player = new PlayerTank((matrix.getRow() - 1), (matrix.getColumn() / 2) - 2, matrix);
		matrix.world[(matrix.getRow() - 1)][(matrix.getColumn() / 2) - 2] = player;
		enemy = new ArrayList<>();

		// crea protezione intorno Flag
		int r = matrix.getRow() - 1;
		int c = (matrix.getColumn() / 2);
		for (int a = 0; a < 2; a++) {
			matrix.world[r - a][c - 1] = new BrickWall(r - a, c - 1, matrix, 2);
			matrix.world[r - a][c + 1] = new BrickWall(r - a, c + 1, matrix, 2);
		}
		matrix.world[size - 2][size / 2] = new BrickWall(size - 2, size / 2, matrix, 2);

		// BRICK WALL
		for (int i = 0; i < 5; i++) {
			matrix.world[10][i] = new BrickWall(10, i, matrix, 2);
			matrix.world[11][i] = new BrickWall(11, i, matrix, 2);
			matrix.world[12][i] = new BrickWall(12, i, matrix, 2);
		}
		// STEEL WALL
		for (int i = 0; i < 8; i++) {
			matrix.world[2][19 - i] = new SteelWall(2, 19 - i, matrix, 4);
			matrix.world[3][19 - i] = new SteelWall(3, 19 - i, matrix, 4);
			matrix.world[4][19 - i] = new SteelWall(7, 19 - i, matrix, 4);

		}
		// WATER
		for (int i = 0; i < 5; i++) {
			matrix.world[14][18 - i] = new Water(14, 18 - i, matrix);
			matrix.world[15][18 - i] = new Water(15, 18 - i, matrix);
			matrix.world[16][18 - i] = new Water(16, 18 - i, matrix);
		}
		// TREES
		for (int i = 0; i < 5; i++) {
			matrix.world[3][3 + i] = new Trees(3, 3 + i, matrix);
			matrix.world[4][3 + i] = new Trees(4, 3 + i, matrix);
			matrix.world[5][3 + i] = new Trees(5, 3 + i, matrix);
			matrix.world[6][3 + i] = new Trees(6, 3 + i, matrix);
			matrix.world[17][1 + i] = new Trees(6, 3 + i, matrix);
			matrix.world[13][1 + i] = new Trees(13, 3 + i, matrix);
			matrix.world[14][1 + i] = new Trees(14, 3 + i, matrix);
			matrix.world[15][1 + i] = new Trees(15, 3 + i, matrix);
			matrix.world[16][1 + i] = new Trees(16, 3 + i, matrix);
			matrix.world[17][1 + i] = new Trees(17, 3 + i, matrix);
		}
		// ICE
		for (int i = 0; i < 5; i++) {
			matrix.world[9][7 + i] = new Ice(9, 3 + i, matrix);
			matrix.world[10][7 + i] = new Ice(10, 3 + i, matrix);
			matrix.world[11][7 + i] = new Ice(11, 3 + i, matrix);
			matrix.world[12][7 + i] = new Ice(12, 3 + i, matrix);
		}
	}

	public void moveRocket() {
		switch (tmp) {
		case UP:
			player.setRocket(new Rocket(player.getX(), player.getY(), player.getWorld(), Direction.UP));
			break;
		case DOWN:
			player.setRocket(new Rocket(player.getX(), player.getY(), player.getWorld(), Direction.DOWN));
			break;
		case LEFT:
			player.setRocket(new Rocket(player.getX(), player.getY(), player.getWorld(), Direction.LEFT));
			break;
		case RIGHT:
			player.setRocket(new Rocket(player.getX(), player.getY(), player.getWorld(), Direction.RIGHT));
			break;
		case STOP:
			player.setRocket(new Rocket(player.getX(), player.getY(), player.getWorld(), Direction.UP));
			break;
		default:
			break;
		}
	}

	public void randomEnemy(int value) {
		while (enemy.size() < value) { // BISOGNA INSERIRE IL TEMPO PER OGNI
										// CARRO NEMICO COSI CHE SI POSSONO
										// SPOSTARE DALLO SPOWN
			selectPosition(value);
		}
	}

	private void selectPosition(int num) {
		int value;
		if ((num - enemy.size()) > 3) {
			value = random.nextInt(4);
		} else if ((num - enemy.size()) > 2) {
			value = random.nextInt(3);
		} else {
			value = random.nextInt(2);
		}

		int position = 0;
		switch (random.nextInt(3)) {
		case 0:
			position = 0;
			break;
		case 1:
			position = size / 2;
			break;
		case 2:
			position = size - 1;
			break;
		default:
			position = 0;
			break;
		}

		while (value != 0) {
			selectEnemy(position);
			value--;
		}
	}

	private void selectEnemy(int y) {
		switch (random.nextInt(4)) {
		case 0:
			enemy.add(new BasicTank(0, y, matrix, direction));
			break;
		case 1:
			enemy.add(new FastTank(0, y, matrix, direction));
			break;
		case 2:
			enemy.add(new PowerTank(0, y, matrix, direction));
			break;
		case 3:
			enemy.add(new ArmorTank(0, y, matrix, direction));
			break;
		}
		matrix.world[0][y] = enemy.get(contEnemy);
		contEnemy++;
	}

	void enemyPositionRandom() {
		for (int a = 0; a < enemy.size(); a++) {
			if (enemy.get(a).getContatorePassi() == 0 || enemy.get(a).isRiprendoValori()) {
				enemy.get(a).setPositionDirection();
				do {
					enemy.get(a).directionEnemyRandom();
				} while (!enemy.get(a).positionCorrect() && !enemy.get(a).notSamePosition() && !enemy.get(a).allTrue());
				// System.out.println("direzione decisa--->" +
				// enemy.get(a).getDirection());
				// System.out.println("DOWN->" +
				// enemy.get(a).isDirectionDown());
				// System.out.println("LEFT->" +
				// enemy.get(a).isDirectionLeft());
				// System.out.println("RIGHT->" +
				// enemy.get(a).isDirectionRight());
				// System.out.println("UP->" + enemy.get(a).isDirectionUp());
				int tempCont;
				tempCont = random.nextInt(size);
				enemy.get(a).setPassi(tempCont);
				// enemy.get(a).setPositionDirection(); //
			}

			if (enemy.get(a).getPassi() >= enemy.get(a).getContatorePassi()) {
				enemy.get(a).update();
				if (enemy.get(a).getX() == enemy.get(a).getTempX() && enemy.get(a).getY() == enemy.get(a).getTempY()) {
					enemy.get(a).setRiprendoValori(true);
					// System.out.println("DIREZIONE UGUALE A QUELLA
					// PRECEDENTE");
					// enemy.get(a).setPositionDirection(enemy.get(a).getDirection());
				} else {
					enemy.get(a).setPositionXY();
					matrix.world[enemy.get(a).getX()][enemy.get(a).getY()] = enemy.get(a);
					enemy.get(a).setContatorePassi(enemy.get(a).getContatorePassi() + 1);
					// System.out.println(enemy.get(a).getContatorePassi());
					if (!enemy.get(a).positionCorrect()) {
						enemy.get(a).setRiprendoValori(true);
					} else {
						enemy.get(a).setRiprendoValori(false);
					}
				}
			} else {
				enemy.get(a).setContatorePassi(0);
				// enemy.get(a).setPositionDirection(enemy.get(a).getDirection());
			}
		}
	}
	
	public static ArrayList<EnemyTank> enemies()
	{
		return enemy;
	}
	

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getContEnemy() {
		return contEnemy;
	}

	public void setContEnemy(int contEnemy) {
		this.contEnemy = contEnemy;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public World getMatrix() {
		return matrix;
	}

	public void setMatrix(World matrix) {
		this.matrix = matrix;
	}

	public PlayerTank getPlayer() {
		return player;
	}

	public void setPlayer(PlayerTank player) {
		GameManager.player = player;
	}

	public ArrayList<EnemyTank> getEnemy() {
		return enemy;
	}

	public void setEnemy(ArrayList<EnemyTank> enemy) {
		GameManager.enemy = enemy;
	}

	public ArrayList<PowerUp> getPower() {
		return power;
	}

	public void setPower(ArrayList<PowerUp> power) {
		this.power = power;
	}

	public ArrayList<AllWall> getWall() {
		return wall;
	}

	public void setWall(ArrayList<AllWall> wall) {
		GameManager.wall = wall;
	}

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}

	public static int getSize() {
		return size;
	}

	public Direction getTmp() {
		return tmp;
	}

	public void setTmp(Direction tmp) {
		this.tmp = tmp;
	}
}
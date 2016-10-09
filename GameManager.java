package progettoIGPE.davide.giovanni.unical2016;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameManager {
	private int x;
	private int y;
	private int contEnemy = 0;
	private static final int size = 20; // non modificare
	private Direction direction;
	private Random random = new Random();
	private Direction tmp = Direction.STOP;

	private World matrix;
	private PlayerTank player;
	private ArrayList<EnemyTank> enemy;
	private ArrayList<Rocket> allRocket;
	// private ArrayList<PowerUp> power;
	// private ArrayList<AllWall> wall;
	// private Flag flag;

	public static void main(String[] args) {
		GameManager game = new GameManager();
		game.randomEnemy(10); // quanti soldati generare
		updateObjects(game); // muovi playerTank
	}

	public static void updateObjects(GameManager game) {

		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		String c;

		game.matrix.print();
		c = s.nextLine();
		while (true) {

			switch (c) {
			case "w": // up
				game.player.setDirection(Direction.UP);
				game.tmp = Direction.UP; // IN TMP RIMANE LA DIREZIONE
											// PRECEDENTE PERCHè ABBIAMO SETTATO
											// AD OGNI CICLO LO STOP DEL PLAYER
				break;
			case "a": // sx
				game.player.setDirection(Direction.LEFT);
				game.tmp = Direction.LEFT;
				break;
			case "d": // dx
				game.player.setDirection(Direction.RIGHT);
				game.tmp = Direction.RIGHT;
				break;
			case "s": // down
				game.player.setDirection(Direction.DOWN);
				game.tmp = Direction.DOWN;
				break;

			case "r": // ROCKET
				game.moveRocket();
				game.player.getRocket().setShot(true);
				break;

			default:
				break;
			}

			if (game.player.getRocket() != null && game.player.getRocket().isShot())
				game.update();

			game.enemyPositionRandom();
			game.player.update();

			if (game.enemy.size() > 0)
				game.matrix.print();
			else {
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println(" ---------------------------  GAME OVER  -------------------------- ");
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				break;
			}
			c = s.nextLine();
		}
	}

	public GameManager() {
		matrix = new World(size, size);
		enemy = new ArrayList<>();
		allRocket = new ArrayList<>();
		player = new PlayerTank((size - 1), ((size / 2) - 3), matrix);
		matrix.world[size - 1][((size / 2) - 3)] = player;

	}

	public void update() {
		for (int a = 0; a < allRocket.size(); a++) {
			if (!allRocket.get(a).destroyRocket())
				allRocket.get(a).update();
			else {
				if (allRocket.get(a).isEnemy() == false) {
					player.setContRocket(player.getContRocket() - 1);
				}
				allRocket.remove(a);
			}
		}
	}

	public void moveRocket() {
		if ((player.getLevel() == 3 && player.getContRocket() < 2)
				|| (player.getLevel() < 3 && player.getContRocket() == 0)) {
			switch (tmp) {
			case UP:
				player.setRocket(new Rocket(player.getX(), player.getY(), player.getWorld(), Direction.UP, false));
				break;
			case DOWN:
				player.setRocket(new Rocket(player.getX(), player.getY(), player.getWorld(), Direction.DOWN, false));
				break;
			case LEFT:
				player.setRocket(new Rocket(player.getX(), player.getY(), player.getWorld(), Direction.LEFT, false));
				break;
			case RIGHT:
				player.setRocket(new Rocket(player.getX(), player.getY(), player.getWorld(), Direction.RIGHT, false));
				break;
			case STOP:
				player.setRocket(new Rocket(player.getX(), player.getY(), player.getWorld(), Direction.UP, false));
				break;
			default:
				break;
			}
			allRocket.add(player.getRocket());
			player.setContRocket(player.getContRocket() + 1);
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
			if (enemy.get(a).getHealth() == 0) { // CANCELLO IL NEMICO CHE HA
													// VITA 0
				enemy.remove(a);
			} else {
				if (enemy.get(a).getContatorePassi() == 0 || enemy.get(a).isRiprendoValori()) {
					enemy.get(a).setPositionDirection();
					do {
						enemy.get(a).directionEnemyRandom();
					} while (!enemy.get(a).positionCorrect() && !enemy.get(a).notSamePosition()
							&& !enemy.get(a).allTrue());
					// System.out.println("direzione decisa--->" +
					// enemy.get(a).getDirection());
					// System.out.println("DOWN->" +
					// enemy.get(a).isDirectionDown());
					// System.out.println("LEFT->" +
					// enemy.get(a).isDirectionLeft());
					// System.out.println("RIGHT->" +
					// enemy.get(a).isDirectionRight());
					// System.out.println("UP->" +
					// enemy.get(a).isDirectionUp());
					int tempCont;
					tempCont = random.nextInt(size);
					enemy.get(a).setPassi(tempCont);
					// enemy.get(a).setPositionDirection(); //
				}

				if (enemy.get(a).getPassi() >= enemy.get(a).getContatorePassi()) {
					enemy.get(a).update();
					if (enemy.get(a).getX() == enemy.get(a).getTempX()
							&& enemy.get(a).getY() == enemy.get(a).getTempY()) {
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
	}

	public ArrayList<EnemyTank> enemies() {
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
		this.player = player;
	}

	public ArrayList<EnemyTank> getEnemy() {
		return enemy;
	}

	public void setEnemy(ArrayList<EnemyTank> enemy) {
		this.enemy = enemy;
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

	public ArrayList<Rocket> getAllRocket() {
		return allRocket;
	}

	public void setAllRocket(ArrayList<Rocket> allRocket) {
		this.allRocket = allRocket;
	}
}
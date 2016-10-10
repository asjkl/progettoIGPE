package progettoIGPE.davide.giovanni.unical2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

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
	private ArrayList<AllWall> wall;
	private Flag flag;
	private Direction tmp = Direction.STOP;
	
	public static void main(String[] args) {

		GameManager game = new GameManager();
		game.randomEnemy(2); // quanti soldati generare
		updateObjects(game); // muovi playerTank
		System.out.println("CIAO");
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
			
			if (GameManager.player.getRocket().isShot())
				GameManager.player.getRocket().update();
				
			game.enemyPositionRandom();
			GameManager.player.update();
			

			 			if( enemy.size() > 0)
			 			game.matrix.stampa();
			 			else
			 			{
			 				System.out.println();
			 				System.out.println();
			 				System.out.println();
			 				System.out.println();System.out.println();System.out.println();System.out.println();
			 				System.out.println(" ---------------------------  GAME OVER  -------------------------- ");
			 				System.out.println();System.out.println();System.out.println();System.out.println();
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
		importMatrix();

	}

	public void importMatrix() {
		int i = 0;// indice di riga

		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/mappa.txt"));
			String line = reader.readLine();
			while (line != null) {
				
				StringTokenizer st = new StringTokenizer(line, " ");
				int j = 0;// indice di colonna
				String tmp ;
				while (st.hasMoreTokens()) {
					
					tmp = st.nextToken();
					
					if (tmp.equals("null"))
						getMatrix().world[i][j] = null;
					else if (tmp.equals("[//]"))
						getMatrix().world[i][j] = new SteelWall(i, j, getMatrix(), 4);
					else if (tmp.equals("@@@@"))
						getMatrix().world[i][j] = new Ice(i, j, getMatrix());
					else if (tmp.equals("TTTT"))
						getMatrix().world[i][j] = new Trees(i, j, getMatrix());
					else if (tmp.equals("[||]"))
						getMatrix().world[i][j] = new BrickWall(i, j, getMatrix(), 2);
					else if (tmp.equals("~~~~"))
						getMatrix().world[i][j] = new Water(i, j, getMatrix());
					else if(tmp.equals("****")){
						player=new PlayerTank(i, j, matrix);
						getMatrix().world[i][j]=player;
					}
					else if(tmp.equals("FLAG")){
						flag=new Flag(i, j, matrix,true);
						getMatrix().world[i][j]=flag;
					}
					j++;
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

	public static ArrayList<EnemyTank> getEnemy() {
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
		this.wall = wall;
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
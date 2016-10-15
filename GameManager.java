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
	private static final int size = 20;
	private Random random;
	private World matrix;
	private PlayerTank player;
	private ArrayList<EnemyTank> enemy;
	private ArrayList<PowerUp> power;
	private ArrayList<Rocket> rocket;
	private Flag flag;
	private boolean exit = false;
	private boolean timer = false;

	public GameManager() {
		matrix = new World(size, size);
		enemy = new ArrayList<>();
		rocket = new ArrayList<>();
		power = new ArrayList<>();
		random = new Random();
		importMatrix();
		randomPowerUp();
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		GameManager game = new GameManager();
		game.randomEnemy(1); // quanti soldati generare
		Scanner s = new Scanner(System.in);
		String c;
		Direction tmp = Direction.STOP; // IN TMP RIMANE LA DIREZIONE
		// PRECEDENTE PERCHE' ABBIAMO SETTATO
		// AD OGNI CICLO LO STOP DEL PLAYER
		boolean enter = false;
		game.matrix.stampa();
		c = s.nextLine();
		while (!game.exit) {
			switch (c) {
			case "w": // up
				game.player.setDirection(Direction.UP);
				tmp = Direction.UP;
				break;
			case "a": // sx
				game.player.setDirection(Direction.LEFT);
				tmp = Direction.LEFT;
				break;
			case "d": // dx
				game.player.setDirection(Direction.RIGHT);
				tmp = Direction.RIGHT;
				break;
			case "s": // down
				game.player.setDirection(Direction.DOWN);
				tmp = Direction.DOWN;
				break;
			case "r": // ROCKET
				game.createRocketTank(tmp, game.player);
				enter = true;
				break;
			default:
				break;
			}

			// spara il doppio rocket al livello > 1
			if (enter && game.player.getLevel() > 1) {
				game.createRocketTank(tmp, game.player);
				enter = false;
			}

			// aggiorna posizione enemy
			//contiene pure funzione che aggiorna tutti rocket
			game.enemyUpdate();

			// aggiorna posizione player
			game.player.update();
			
			System.out.println("Player "+ game.player.getContRocket());
			for(int i=0;i<game.enemy.size();i++)
				System.out.println("Enemy "+ game.enemy.get(i).getContRocket());
			
			// GAME OVER / WIN
			if (game.flag.isHit() || game.player.getResume() == 0) { 
				game.printGameOver();// stampa sconfitta
				game.exit = true;
			} else if (game.enemy.size() == 0) {
				game.printWin(); // se non ci sono piu nemici stampa vittoria
				game.exit = true;
			}

			if (game.exit == false) {
				game.matrix.stampa();
				c = s.nextLine();
			}
		}
	}

	public void printWin() {

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(" ---------------------------------  YOU WIN  ---------------------------------- ");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}

	public void printGameOver() {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(" ---------------------------------  Game Over  ---------------------------------- ");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}

	public void randomPowerUp() {

		int cont = 0;
		int tmp;

		while (cont < 5) {

			tmp = random.nextInt(6);

			addPowerUp(tmp);

			cont++;
		}

	}

	public void addPowerUp(int t) {
		PowerUp tmp = null;
		switch (t) {
		case 0:
			foundPosition();
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.GRANADE);
			tmp.setBefore(getMatrix().world[getX()][getY()]); // salvo oggetto
																// su cui cade
																// PowerUp
			power.add(tmp); // aggiungi in list
			getMatrix().world[getX()][getY()] = tmp; // posiziono PowerUp
			break;
		case 1:
			foundPosition();
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.HELMET);
			tmp.setBefore(getMatrix().world[getX()][getY()]);
			power.add(tmp); // aggiungi in list
			getMatrix().world[getX()][getY()] = tmp;
			break;
		case 2:
			foundPosition();
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.SHOVEL);
			tmp.setBefore(getMatrix().world[getX()][getY()]);
			power.add(tmp); // aggiungi in list
			getMatrix().world[getX()][getY()] = tmp;
			break;
		case 3:
			foundPosition();
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.STAR);
			tmp.setBefore(getMatrix().world[getX()][getY()]);
			power.add(tmp); // aggiungi in list
			getMatrix().world[getX()][getY()] = tmp;
			break;
		case 4:
			foundPosition();
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.TANK);
			tmp.setBefore(getMatrix().world[getX()][getY()]);
			power.add(tmp); // aggiungi in list
			getMatrix().world[getX()][getY()] = tmp;
			break;
		case 5:
			foundPosition();
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.TIMER);
			tmp.setBefore(getMatrix().world[getX()][getY()]);
			power.add(tmp); // aggiungi in list
			getMatrix().world[getX()][getY()] = tmp;
			break;
		default:
			break;
		}
	}

	public void foundPosition() {

		boolean flag = false;

		while (!flag) {

			x = random.nextInt(size);
			y = random.nextInt(size);

			if (!(getMatrix().world[x][y] instanceof Wall) && !(getMatrix().world[x][y] instanceof PlayerTank)
					&& !(getMatrix().world[x][y] instanceof EnemyTank) && !(getMatrix().world[x][y] instanceof PowerUp)
					&& !(getMatrix().world[x][y] instanceof Rocket))

				flag = true;
		}
	}

	public void usePowerUp(Power power) {

		switch (power) {
		case GRANADE:
			for (int i = 0; i < enemy.size(); i++)
				enemy.remove(i);
			break;
		// TODO gestire secondi poi deve sparire oppure illimitato e sparisce
		// dopo sparo
		case HELMET:
			player.setProtection(true);
			lenghtPowerUp(5);
			break;
		// TODO deve duare tot secondi e poi si deve ripristinare come era prima
		// con i brickwall
		case SHOVEL:
			for (int i = size - 2; i < size; i++)

				for (int j = (size / 2) - 2; j <= size / 2; j++)

					if (!(getMatrix().world[i][j] instanceof Flag))
						getMatrix().world[i][j] = new BrickWall(i, j, getMatrix(), 4);
			break;
		// TODO dopo esser stato colpito si riparte da livello 1
		case STAR:
			if (player.getLevel() < 3)
				player.setLevel(player.getLevel() + 1);
			break;
		case TANK:
			player.setLevel(player.getResume() + 1);
			break;
		case TIMER:
			// TODO gestire secondi blocco
			System.out.println("POWERUP");
			for (int i = 0; i < enemy.size(); i++)
				enemy.get(i).setDirection(Direction.STOP);
			lenghtPowerUp(10);
			break;
		}
	}

	public void lenghtPowerUp(int t) {

		long second = (System.currentTimeMillis() / 1000) % 60;
		long tmp = second + t;

		if (tmp >= 59)

			tmp -= 59;

		while (!timer) {

			second = (System.currentTimeMillis() / 1000) % 60;

			if (second == tmp)

				timer = true;
		}
	}

	public void updateRocket() {
		
		for (int a = 0; a < rocket.size(); a++) {
			rocket.get(a).update();

			if (destroyRocket(rocket.get(a))) {
				
				// contatore rocket presenti
				if (!(rocket.get(a).getTank() instanceof EnemyTank))
				
					player.setContRocket(player.getContRocket() - 1);
				else {
					for (int b = 0; b < enemy.size(); b++) {
						if (enemy.get(b) == rocket.get(a).getTank()) {
							
							enemy.get(b).setContRocket(enemy.get(b).getContRocket() - 1);
							break;
						}
					}
				}
					
				matrix.world[rocket.get(a).getX()][rocket.get(a).getY()] = rocket.get(a).getCurr();

				// distruggi Wall
				if (rocket.get(a).getNext() instanceof Wall)
					if (((Wall) rocket.get(a).getNext()).getHealth() == 0)
						destroyWall(rocket.get(a));
				
				// distruggi EnemyTank
				if (rocket.get(a).getNext() instanceof EnemyTank)
					if (((EnemyTank) rocket.get(a).getNext()).getHealth() == 0)
						destroyTank(rocket.get(a), (EnemyTank) rocket.get(a).getNext());
				
				// distruggi Flag
				if (rocket.get(a).getNext() instanceof Flag)
					flag.setHit(true);
				
				// distruggi Player
				// per il momento viene terminato il gioco senza cancellare
					
				//distruggi rocket alla fine
				rocket.remove(a);
				a--;
			}
		}
	}

	public boolean destroyRocket(Rocket rocket) {
		
		
		//se rocket si trova al bordo o tocca flag o si scontra con un altro rocket 
		if (rocket.isBordo() || rocket.getNext() instanceof Flag || rocket.getNext() instanceof Rocket) {
			return true;
		}

		if (rocket.getNext() instanceof Wall) {
			damageWall(rocket);
			return true;
		}

		if (rocket.getNext() instanceof EnemyTank) {
			damageEnemyTank(rocket);
			return true;
		}

		if (rocket.getNext() instanceof PlayerTank) {
			damagePlayerTank(rocket);
			return true;
		}
		
		// se Rocket tocca bordo ( è collegato al primo if )
		if ((rocket.getX() == 0 && rocket.getDirection() == Direction.UP)
				|| (rocket.getX() == matrix.getRow() - 1 && rocket.getDirection() == Direction.DOWN)
				|| (rocket.getY() == 0 && rocket.getDirection() == Direction.LEFT)
				|| (rocket.getY() == matrix.getColumn() - 1 && rocket.getDirection() == Direction.RIGHT)) {
			rocket.setBordo(true);
		}
		return false;
	}
	
	public void destroyR(Rocket r)
	{
		matrix.world[r.getX()][r.getY()] = r.getCurr();
		
		for(int i=0;i<rocket.size();i++)
		{
			if(rocket.get(i).equals(r))
			{
				rocket.remove(i);
				i--;
			}
		}
	}
	
	public void destroyWall(Rocket rocket) {
		switch (rocket.getDirection()) {
		case UP:
			matrix.world[rocket.getX() - 1][rocket.getY()] = null;
			break;
		case DOWN:
			matrix.world[rocket.getX() + 1][rocket.getY()] = null;
			break;
		case RIGHT:
			matrix.world[rocket.getX()][rocket.getY() + 1] = null;
			break;
		case LEFT:
			matrix.world[rocket.getX()][rocket.getY() - 1] = null;
			break;
		default:
			break;
		}
	}

	public void destroyTank(Rocket rocket, EnemyTank enemyT) {
		switch (rocket.getDirection()) {
		case UP:
			matrix.world[rocket.getX() - 1][rocket.getY()] = enemyT.getCurr();
			break;
		case DOWN:
			matrix.world[rocket.getX() + 1][rocket.getY()] = enemyT.getCurr();
			break;
		case RIGHT:
			matrix.world[rocket.getX()][rocket.getY() + 1] = enemyT.getCurr();
			break;
		case LEFT:
			matrix.world[rocket.getX()][rocket.getY() - 1] = enemyT.getCurr();
			break;
		default:
			break;
		}
	}

	private void damageWall(Rocket rocket) {
		if (player.getLevel() == 3) {
			((Wall) rocket.getNext()).setHealth(((Wall) rocket.getNext()).getHealth() - 2);
		} else if (!(rocket.getNext() instanceof SteelWall)) // e non è
																// steelwall
		{
			((Wall) rocket.getNext()).setHealth(((Wall) rocket.getNext()).getHealth() - 1);
		}
	}

	private void damageEnemyTank(Rocket rocket) {
		((EnemyTank) rocket.getNext()).setHealth(((EnemyTank) rocket.getNext()).getHealth() - 1);
	}

	private void damagePlayerTank(Rocket rocket) {
		((PlayerTank) rocket.getNext()).setResume(((PlayerTank) rocket.getNext()).getResume() - 1);
		getMatrix().world[size - 1][size / 2 - 3] = player;
		getMatrix().world[player.getX()][player.getY()] = player.getCurr();
		player.setX(size - 1);
		player.setY(size / 2 - 3);

	}

	public void importMatrix() {
		int i = 0;// indice di riga

		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/mappa.txt"));
			String line = reader.readLine();
			while (line != null) {

				StringTokenizer st = new StringTokenizer(line, " ");
				int j = 0;// indice di colonna
				String tmp;
				while (st.hasMoreTokens()) {

					tmp = st.nextToken();

					switch (tmp) {
					case ("null"):
						getMatrix().world[i][j] = null;
						break;
					case ("[//]"):
						getMatrix().world[i][j] = new SteelWall(i, j, getMatrix(), 4);
						break;
					case ("@@@@"):
						getMatrix().world[i][j] = new Ice(i, j, getMatrix());
						break;
					case ("TTTT"):
						getMatrix().world[i][j] = new Trees(i, j, getMatrix());
						break;
					case ("[||]"):
						getMatrix().world[i][j] = new BrickWall(i, j, getMatrix(), 2);
						break;
					case ("~~~~"):
						getMatrix().world[i][j] = new Water(i, j, getMatrix());
						break;
					case ("****"):
						player = new PlayerTank(i, j, matrix);
						getMatrix().world[i][j] = player;
						break;
					case ("FLAG"):
						flag = new Flag(i, j, matrix);
						getMatrix().world[i][j] = flag;
						break;
					}// switch

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

	public void createRocketTank(Direction tmp, AbstractDynamicObject tank) {
		
		//sapia commenta capito? e cittu...
		if ((tank instanceof PlayerTank && player.getLevel() < 3 && player.getContRocket() == 0)
				|| (tank instanceof PlayerTank && player.getLevel() > 1 && player.getContRocket() < 2)
				|| (tank instanceof EnemyTank && tank.getContRocket() == 0)) {
			switch (tmp) {
			case UP:
				rocket.add(new Rocket(tank.getX(), tank.getY(), matrix, Direction.UP, tank));
				break;
			case DOWN:
				rocket.add(new Rocket(tank.getX(), tank.getY(), matrix, Direction.DOWN, tank));
				break;
			case LEFT:
				rocket.add(new Rocket(tank.getX(), tank.getY(), matrix, Direction.LEFT, tank));
				break;
			case RIGHT:
				rocket.add(new Rocket(tank.getX(), tank.getY(), matrix, Direction.RIGHT, tank));
				break;
			case STOP:
				rocket.add(new Rocket(tank.getX(), tank.getY(), matrix, Direction.UP, tank));
				break;
			default:
				break;
			}
			tank.setContRocket(tank.getContRocket() + 1); // conta rocket
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

	public void enemyPositionRandom() {
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
		}

	}

	public void enemyUpdate() {

		enemyPositionRandom(); // genera pos enemy

		for (int a = 0; a < enemy.size(); a++) {
			if (enemy.get(a).getPassi() >= enemy.get(a).getContatorePassi()) {
				createRocketTank(enemy.get(a).getDirection(), enemy.get(a)); //crea rocket nemico
				updateRocket(); //lo aggiorna insieme agli altri
				
				if (!controlDestroyEnemyInArrayList(enemy.get(a))) { 
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
				}
			} else {
				enemy.get(a).setContatorePassi(0);
				updateRocket();
				// enemy.get(a).setPositionDirection(enemy.get(a).getDirection());
			}
		}
	}

	private boolean controlDestroyEnemyInArrayList(EnemyTank enemyTank) {
		for (int i = 0; i < enemy.size(); i++) {
			if (enemy.get(i).getHealth() == 0 && enemy.get(i) == enemyTank) {
				enemy.remove(i);
				return true;
			}
		}
		return false;
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

	public ArrayList<PowerUp> getPower() {
		return power;
	}

	public void setPower(ArrayList<PowerUp> power) {
		this.power = power;
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

	public ArrayList<Rocket> getRocket() {
		return rocket;
	}

	public void setRocket(ArrayList<Rocket> rocket) {
		this.rocket = rocket;
	}
}

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
	private int max3Enemy = 0;
	private static final int size = 20;
	private Random random;
	private World matrix;
	private PlayerTank player;
	private ArrayList<EnemyTank> enemy;
	private ArrayList<PowerUp> power;
	private ArrayList<Rocket> rocket;
	private Flag flag;
	private Direction direction;
	private boolean exit = false;
	private ArrayList<AbstractStaticObject> recoveryWall;
	private long currentTime;
	private boolean updateAll=true;

	public GameManager() {
		matrix = new World(size, size);
		enemy = new ArrayList<>();
		rocket = new ArrayList<>();
		power = new ArrayList<>();
		random = new Random();
		recoveryWall = new ArrayList<>();
		importMatrix();
		randomPowerUp();
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
						player = new PlayerTank(i, j, getMatrix());
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
	@SuppressWarnings("resource")
	public static void main(String[] args) {

		GameManager game = new GameManager();
		game.randomEnemy(6); // quanti soldati generare
		Scanner s = new Scanner(System.in);
		String c;
		Direction tmp = Direction.STOP; // IN TMP RIMANE LA DIREZIONE
		// PRECEDENTE PERCHE' ABBIAMO SETTATO
		// AD OGNI CICLO LO STOP DEL PLAYER
		boolean enter = false;
		game.matrix.stampa();
		c = s.nextLine();
		while (!game.exit) {

			// aggiorna time in tempo reale
			game.currentTime = (System.currentTimeMillis() / 1000) % 60;

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
			
			// NUMERO NEMICI MAX SULLA MAPPA
			if (game.max3Enemy < 3)
				game.bringUpTheEnemyInTheMap();

			game.enemyPositionRandom(); // CREAZIONE ENEMY
			game.updateRocket(); // AGGIORNAMENTO DI TUTTI I ROCKET
			game.player.update(); // AGGIORNAMENTO PLAYER
			game.enemyUpdate(); // AGGIORNAMENTO ENEMY + POWERUP CONDITION

			// --------------------------POWERUP----------------------------------------------------

			if (game.player.getNext() instanceof PowerUp) {
				((PowerUp) game.player.getNext()).setActivate(true); 
				((PowerUp) game.player.getNext()).setTimer((System.currentTimeMillis() / 1000) % 60);
				game.usePowerUp(((PowerUp) game.player.getNext()));
			}
			//CONTROLLA POWERUP
			game.timeOut();

			// -------------------------------------------------------------------------------------

			// spara il doppio rocket al livello > 1
			if (enter && game.player.getLevel() > 1) {
				game.createRocketTank(tmp, game.player);
				enter = false;
			}

			// GAME OVER
			if (game.flag.isHit() || game.player.getResume() == 0) {
				game.matrix.stampa();
				game.printGameOver();
				game.exit = true;
			}
			// WIN
			if (game.enemy.size() == 0) {
				game.matrix.stampa();
				game.printWin();
				game.exit = true;
			}
			//STAMPE
			if (game.exit == false) {
				System.out.println();
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

	// ----------------------------------------POWERUP-------------------------------------

	private void timeOut() {
		for (int a = 0; a < power.size(); a++)
			if (power.get(a).isActivate()) { // se powerUp � attivo
				System.out.println("attivo: "+power.get(a));
				
				long tmp =(power.get(a).getTimer() + power.get(a).getDuration())%60;
				if ( tmp == currentTime) {
					System.out.println("------timeOut: "+power.get(a));
					managePowerUp(power.get(a));
					power.get(a).setActivate(false);
					power.remove(a);
					a--;
				}
			}
	}

	private void managePowerUp(PowerUp p) {
				
		switch (p.getPowerUp()) {

		case HELMET:
			player.setProtection(false);
			break;
		case SHOVEL:
			int x = 0;
			for (int i = size - 2; i < size; i++)
				for (int j = (size / 2) - 2; j <= size / 2; j++)
					if (!(getMatrix().world[i][j] instanceof Flag))
						if (x < recoveryWall.size())
							getMatrix().world[i][j] = recoveryWall.get(x++);
			recoveryWall.clear();
			break;
		case STAR:
			player.setLevel(player.getLevel()-1);
			break;
		case TIMER:
			updateAll=true;
			break;
		default:
			break;
		}
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
					&& !(getMatrix().world[x][y] instanceof Rocket) && !(getMatrix().world[x][y] instanceof Water))

				flag = true;
		}
	}

	public void usePowerUp(PowerUp power) {

		switch (power.getPowerUp()) {
		case GRANADE:
			for (int i = 0; i < enemy.size(); i++) {
				matrix.world[enemy.get(i).getX()][enemy.get(i).getY()] = enemy.get(i).getCurr();
				enemy.remove(i);
				i--;
			}
			break;
		case HELMET:
			player.setProtection(true);
			break;
		case SHOVEL:
			for (int i = size - 2; i < size; i++)
				for (int j = (size / 2) - 2; j <= size / 2; j++)
					if (!(getMatrix().world[i][j] instanceof Flag)) {
						recoveryWall.add(getMatrix().world[i][j]); 
						getMatrix().world[i][j] = new SteelWall(i, j, getMatrix(), 4);
					}
			power.setDuration(20);
			break;
		case STAR:
			if (player.getLevel() < 3)
				player.setLevel(player.getLevel()+1);
			break;
		case TANK:
			player.setResume(player.getResume() + 1);
			break;
		case TIMER:
			updateAll=false;
			power.setDuration(10);
			break;
		}
	}

	public void lenghtPowerUp(int t) {

		long second = (System.currentTimeMillis() / 1000) % 60;
		long tmp = second + t;

		tmp = (tmp + 1) % 60; // ritorna a capo

		while (second < tmp) {
			second = (System.currentTimeMillis() / 1000) % 60;
		}
	}

	// ---------------------------------------ROCKET----------------------------------------

	public void updateRocket() {
		Rocket r = null; // rocket temporaneo

		for (int a = 0; a < rocket.size(); a++) {
			rocket.get(a).update();

			if (destroyRocket(rocket.get(a))) {
				countRockets(rocket.get(a));

				// aggiorna rocket se curr non � un tank
				if (!(rocket.get(a).getCurr() instanceof PlayerTank) && !(rocket.get(a).getCurr() instanceof EnemyTank))
					matrix.world[rocket.get(a).getX()][rocket.get(a).getY()] = rocket.get(a).getCurr();

				// distruggi enemy
				if (rocket.get(a).getNext() instanceof EnemyTank && rocket.get(a).getTank() instanceof PlayerTank)
					if (((EnemyTank) rocket.get(a).getNext()).getHealth() == 0) {
						switchCurrTank(((EnemyTank) rocket.get(a).getNext()));
						destroyEnemyTank((EnemyTank) rocket.get(a).getNext());
					}

				// distruggi player
				if (rocket.get(a).getNext() instanceof PlayerTank && rocket.get(a).getTank() instanceof EnemyTank) {
					if (player.isProtection()==false) { //se non � protetto
						switchCurrTank(player);
						damageAndDestroyPlayerTank();
					} 
				}

				if (rocket.get(a).getNext() instanceof Rocket)
					r = ((Rocket) rocket.get(a).getNext());

				rocket.remove(a);
				a--;
			}
		}
		if (r != null) // distruggi Rocket2
			destroyOtherRocket(r);
	}

	private void countRockets(Rocket r) {
		if (!(r.getTank() instanceof EnemyTank))
			player.setContRocket(player.getContRocket() - 1);
		else {
			for (int b = 0; b < enemy.size(); b++) {
				if (enemy.get(b) == r.getTank()) {
					enemy.get(b).setContRocket(enemy.get(b).getContRocket() - 1);
					break;
				}
			}
		}
	}

	private boolean destroyRocket(Rocket rocket) {

		if (rocket.isBordo() || rocket.getNext() instanceof Rocket || rocket.getNext() instanceof PlayerTank)
			return true;

		if (rocket.getNext() instanceof Flag) {
			flag.setHit(true);
			return true;
		}

		if (rocket.getNext() instanceof Wall) {
			damageWall(rocket);
			if (((Wall) rocket.getNext()).getHealth() == 0)
				destroyWall(rocket);
			return true;
		}

		if (rocket.getNext() instanceof EnemyTank) {
			if (rocket.getTank() instanceof PlayerTank)
				damageEnemyTank(rocket);
			return true;
		}

		// se Rocket tocca bordo ( � legato al primo if )
		if ((rocket.getX() == 0 && rocket.getDirection() == Direction.UP)
				|| (rocket.getX() == matrix.getRow() - 1 && rocket.getDirection() == Direction.DOWN)
				|| (rocket.getY() == 0 && rocket.getDirection() == Direction.LEFT)
				|| (rocket.getY() == matrix.getColumn() - 1 && rocket.getDirection() == Direction.RIGHT)) {
			rocket.setBordo(true);
		}
		return false;
	}

	private void destroyOtherRocket(Rocket r) {

		for (int a = 0; a < rocket.size(); a++)
			if (r == rocket.get(a)) {
				countRockets(rocket.get(a));
				matrix.world[rocket.get(a).getX()][rocket.get(a).getY()] = rocket.get(a).getCurr();
				rocket.remove(a);
				a--;
			}
	}

	private void damageEnemyTank(Rocket rocket) {
		((EnemyTank) rocket.getNext()).setHealth(((EnemyTank) rocket.getNext()).getHealth() - 1);
	}

	private void damageWall(Rocket rocket) {

		if (player.getLevel() == 3)
			((Wall) rocket.getNext()).setHealth(((Wall) rocket.getNext()).getHealth() - 2);
		else if (!(rocket.getNext() instanceof SteelWall)) // e non � SteelWall
			((Wall) rocket.getNext()).setHealth(((Wall) rocket.getNext()).getHealth() - 1);

	}

	private void damageAndDestroyPlayerTank() {
		getMatrix().world[player.getX()][player.getY()] = player.getCurr();
		getMatrix().world[size - 1][(size / 2) - 3] = player;
		player.setResume(player.getResume() - 1);
		player.setX(size - 1);
		player.setY((size / 2) - 3);
		player.setDirection(Direction.STOP);
		player.setCurr(null);
	}

	private void destroyWall(Rocket rocket) {
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

	private void destroyEnemyTank(EnemyTank enemyT) {

		matrix.world[enemyT.getX()][enemyT.getY()] = enemyT.getCurr();

		// distruggi enemy dalla lista
		for (int i = 0; i < enemy.size(); i++)
			if (enemy.get(i) == enemyT) {
				enemy.remove(i);
				max3Enemy--;
				break;
			}
	}

	public void createRocketTank(Direction tmp, AbstractDynamicObject tank) {

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

	private void switchCurrTank(AbstractDynamicObject tank) {
		for (int a = 0; a < rocket.size(); a++)
			if (rocket.get(a).getTank() == tank && rocket.get(a).getCurr() == tank)
				rocket.get(a).setCurr(tank.getCurr());
	}

	// -------------------------------------ENEMY-------------------------------------------

	public void randomEnemy(int value) {
		while (enemy.size() < value) { // TODO BISOGNA INSERIRE IL TEMPO PER
										// OGNI
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
		// matrix.world[0][y] = enemy.get(contEnemy);
		contEnemy++;
	}

	public void enemyPositionRandom() {
		for (int a = 0; a < enemy.size(); a++) {
			if (enemy.get(a).isappearsInTheMap()) {
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
				if (!(enemy.get(a).getNext() instanceof EnemyTank) && updateAll==true)
					createRocketTank(enemy.get(a).getDirection(), enemy.get(a));
			}
		}

	}

	public void enemyUpdate() {

		//AGGIORNA NEMICI SOLO SE NON E' STATO PRESO POWERUP TIMER
		if(updateAll == true){
			

			for (int a = 0; a < enemy.size(); a++) {
				if (enemy.get(a).isappearsInTheMap()) {
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
						matrix.world[enemy.get(a).getX()][enemy.get(a).getY()] = enemy.get(a);
					}
				}
			}
		} // POWERUP 
		
	}

	private void bringUpTheEnemyInTheMap() {
		for (int a = 0; a < enemy.size(); a++) {
			if ((random.nextInt(2)) == 1 && max3Enemy < 3 && !enemy.get(a).isappearsInTheMap()) {
				enemy.get(a).setAppearsInTheMap(true);
				max3Enemy++;

				if (max3Enemy == 3) // ESCO DAL CICLO
					break;
			}
		}
	}

	// -----------------------------SET & GET-----------------------------------------------

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

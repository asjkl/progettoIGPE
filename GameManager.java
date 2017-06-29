package progettoIGPE.davide.giovanni.unical2016;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JTextField;

public class GameManager {

	private final int width = 21;
	private final int height = 20;
	private int x;
	private int y;
	public static boolean offline = false;
	public static long currentTime;
	private boolean soundPowerUp;
	public boolean pauseOptionDialog;
	public boolean paused;
	private Random random;
	private World matrix;
	public static Flag flag;
	private LinkedList<PlayerTank> playersArray;
	private ArrayList<EnemyTank> enemy;
	private ArrayList<PowerUp> power;
	private ArrayList<Rocket> rocket;
	private ArrayList<AbstractStaticObject> recoveryWall;
	private ArrayList<AbstractStaticObject> effects;

	// ENEMY
	private int numberOfEnemyToSpawn;
	private int numberOfEnemyOnMap;
	private int numberOfEnemyReadyToSpwan;
	private int numbersOfEnemy;

	// POWERUPS
	private int durationPowerUp;
	private int numEnemyDropsPowerUp;
	private int xTmp;
	private int yTmp;
	private Direction dir;
	private long blinkTime;

	// Timer
	public Timer timer;
	public TimerTask task;
	public Timer timer2;
	public TimerTask task2;

	private JTextField filename;
	private boolean explosion;

	private PlayerTank playerTmp;
	int pos[] = { 0, width / 2, width - 1 };

	private boolean exit;
	private boolean waitToExit;
	public Runnable runnable = null;

	public Lock lock = new ReentrantLock();

	// OFFLINE
	public GameManager(JTextField filename, boolean singlePlayer) {
		GameManager.offline = true;
		if (singlePlayer) {
			startGameManager(filename, 1); // IL NUMERO MI STA A DIRE SE C'� UN
											// SINGOLO GIOCATORE O DI PI�
		} else {
			startGameManager(filename, 2);
		}
	}

	// ONLINE CLIENT
	public GameManager(JTextField filename, String name) {
		matrix = new World(height, width);
		enemy=new ArrayList<>();
		rocket = new ArrayList<>();
		effects = new ArrayList<>();
		playersArray = new LinkedList<>();
		power = new ArrayList<>();
		GameManager.offline = false;
		// crea player
		if (name.equals("P1")) {
			playersArray.addFirst(new PlayerTank(19, 5, getMatrix(), name));
			getMatrix().world[19][5] = playersArray.get(0);

			playersArray.addLast(new PlayerTank(19, 14, getMatrix(), "P2"));
			getMatrix().world[19][14] = playersArray.get(1);
		} else if (name.equals("P2")) {
			playersArray.addFirst(new PlayerTank(19, 14, getMatrix(), name));
			getMatrix().world[19][14] = playersArray.get(0);

			playersArray.addLast(new PlayerTank(19, 5, getMatrix(), "P1"));
			getMatrix().world[19][5] = playersArray.get(1);
		}
	}

	// ONLINE SERVER
	public GameManager(Runnable runnable, List<String> names, JTextField filename) {
		GameManager.offline = false;
		this.runnable = runnable;
		startGameManager(filename, 2);

		for (int i = 0; i < names.size(); i++) {
			// crea player
			if (names.get(i).equals("P1")) {
				playersArray.add(new PlayerTank(19, 5, getMatrix(), names.get(i)));
				getMatrix().world[19][5] = playersArray.get(0);
			} else if (names.get(i).equals("P2")) {
				playersArray.add(new PlayerTank(19, 14, getMatrix(), names.get(i)));
				getMatrix().world[19][14] = playersArray.get(0);
			}
		}

	}

	// USED FOR CONSTRUCTION
	public GameManager(World world, Flag flag) {
		this.matrix = world;
		GameManager.flag = flag;
	}

	// --------------------------------------OTHER-----------------------------------------
	public void startGameManager(JTextField filename, int numOfPlayer) {
		currentTime = 0;
		pauseOptionDialog = false;
		paused = false;
		numberOfEnemyOnMap = 0;
		numberOfEnemyReadyToSpwan = 0;
		durationPowerUp = 20;
		numEnemyDropsPowerUp = 1; // indica ogni quanti enemie far cadere
									// powerUp
		xTmp = -1;
		yTmp = -1;
		blinkTime = 5; // quanti secondi alla fine deve lampeggiare
		soundPowerUp = false;
		explosion = false;

		matrix = new World(height, width);
		enemy = new ArrayList<>();
		rocket = new ArrayList<>();
		power = new ArrayList<>();
		recoveryWall = new ArrayList<>();
		effects = new ArrayList<>();
		random = new Random();
		playersArray = new LinkedList<>();

		this.setFilename(filename);

		importMap(filename, numOfPlayer);

		if (playersArray.size() == 1)
			numberOfEnemyToSpawn = 4;
		else
			numberOfEnemyToSpawn = 6;

		timer = new Timer();
		task = new MyTask();
		timer.schedule(task, 85, 85);

		timer2 = new Timer();
		task2 = new CurrentTime();
		timer2.schedule(task2, 0, 1000);
		setExit(false);
		waitToExit = false;
		setNumbersOfEnemy(enemy.size());
	}

	public class MyTask extends TimerTask {

		public void run() {

			// STAMPA
			// getMatrix().print();
			// System.out.println();

			if (!pauseOptionDialog) {

				// EFFECTS
				for (int i = 0; i < effects.size(); i++) {
					if (effects.get(i) instanceof Tank)
						((Tank) (effects.get(i))).setInc(((Tank) (effects.get(i))).getInc() + 1);
					else if (effects.get(i) instanceof Rocket)
						((Rocket) (effects.get(i))).setInc(((Rocket) (effects.get(i))).getInc() + 1);
					else if (effects.get(i) instanceof PowerUp)
						((PowerUp) (effects.get(i))).setInc(((PowerUp) (effects.get(i))).getInc() + 1);
					else if (effects.get(i) instanceof Flag)
						((Flag) (effects.get(i))).setInc(((Flag) (effects.get(i))).getInc() + 1);
				}

				for (int i = 0; i < getEnemy().size(); i++) {
					// EFFETTO SPAWN ENEMY
					if (getEnemy().get(i).isReadyToSpawn())
						getEnemy().get(i).setCountdown((getEnemy().get(i).getCountdown() + 1) % 4);

					// EFFETTO PROTEZIONE ENEMY
					if (enemy.get(i).isAppearsInTheMap() && enemy.get(i).isProtection()) {
						enemy.get(i).setCountdown((enemy.get(i).getCountdown() + 1) % 2);
					}
				}

				for (int a = 0; a < playersArray.size(); a++) {
					// EFFETTO SPAWN / PROTEZIONE PLAYER
					if (currentTime == playersArray.get(a).getSpawnTime())
						playersArray.get(a).setReadyToSpawn(false);
					if (playersArray.get(a).isReadyToSpawn() || playersArray.get(a).isProtection()) {
						playersArray.get(a).setCountdown((playersArray.get(a).getCountdown() + 1) % 2);
					}
				}
			}
		}
	}

	public ArrayList<AbstractStaticObject> getEffects() {
		return effects;
	}

	public void setEffects(ArrayList<AbstractStaticObject> effects) {
		this.effects = effects;
	}

	public class CurrentTime extends TimerTask {

		public void run() {

			if (!pauseOptionDialog) {

				currentTime = (currentTime + 1) % 60;

				// System.out.println(currentTime);

				// Manage PowerUp
				for (int a = 0; a < power.size(); a++) {

					if (power.get(a).isActivate()) { // timeout
						power.get(a).setTime(power.get(a).getTime() - 1);

						if (power.get(a).getTime() <= 0) {
							managePowerUp(power.get(a));
							power.remove(a);
							a--;
						}
					} else if (power.get(a).isDrop() && !power.get(a).isActivate()) { // Dropped

						power.get(a).setDropTime(power.get(a).getDropTime() - 1);

						// EFFETTO LAMPEGGIO
						if (power.get(a).getDropTime() == blinkTime) {
							power.get(a).setBlink(true);
						}

						if (power.get(a).getDropTime() <= 0) {
							power.get(a).setDrop(false);

							if (power.get(a).getBefore() instanceof Water) {
								getMatrix().world[power.get(a).getX()][power.get(a).getY()] = power.get(a)
										.getBeforeWater();
								getMatrix().world[((Water) power.get(a).getBefore())
										.getX()][((Water) power.get(a).getBefore()).getY()] = power.get(a).getBefore();
							} else {

								if (power.get(a).getBefore() instanceof BrickWall)
									((BrickWall) power.get(a).getBefore()).setBefore(null);
								else if (power.get(a).getBefore() instanceof SteelWall)
									((SteelWall) power.get(a).getBefore()).setBefore(null);
								getMatrix().world[power.get(a).getX()][power.get(a).getY()] = power.get(a).getBefore();
							}
							power.remove(a);
							a--;
						}
					}
				}
			}
		}
	}

	public void importMap(JTextField filename, int numOfPlayer) {
		int i = 0;// indice di riga
		try {
			BufferedReader reader = null;
			File career = new File("./maps/career/" + filename.getText());
			if (!career.exists()) {
				File multiplayer = new File("./maps/editor/multiplayer/" + filename.getText());
				if (!multiplayer.exists()) {
					File singleplayer = new File("./maps/editor/singleplayer/" + filename.getText());
					reader = new BufferedReader(new FileReader(singleplayer.toString()));
				} else {
					reader = new BufferedReader(new FileReader(multiplayer.toString()));
				}
			} else {
				reader = new BufferedReader(new FileReader(career.toString()));
			}
			String line = reader.readLine();
			while (i < height) {

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
						getMatrix().objectStatic[i][j] = new Ice(i, j, getMatrix());
						break;
					case ("TTTT"):
						getMatrix().world[i][j] = new Tree(i, j, getMatrix());
						getMatrix().objectStatic[i][j] = new Tree(i, j, getMatrix());
						break;
					case ("[||]"):
						getMatrix().world[i][j] = new BrickWall(i, j, getMatrix(), 2);
						break;
					case ("~~~~"):
						getMatrix().world[i][j] = new Water(i, j, getMatrix());
						getMatrix().objectStatic[i][j] = new Water(i, j, getMatrix());
						break;
					case ("P1"):
						if (offline) {
							playersArray.addFirst(new PlayerTank(i, j, getMatrix(), "P1"));
							getMatrix().world[i][j] = playersArray.get(playersArray.size() - 1);
						}
						break;
					case ("P2"):
						if (offline) {
							playersArray.addLast(new PlayerTank(i, j, getMatrix(), "P2"));
							getMatrix().world[i][j] = playersArray.get(playersArray.size() - 1);
						}
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

			importEnemies(reader, line, numOfPlayer);
			reader.close();

		} // try
		catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void importEnemies(BufferedReader reader, String line, int numOfPlayer) {

		while (line != null) {
			StringTokenizer st = new StringTokenizer(line, " ");

			String typology = null;
			String number = null;

			if (st.hasMoreTokens())
				typology = st.nextToken();
			if (st.hasMoreTokens())
				number = st.nextToken();

			if (typology != null && number != null)
				addEnemies(typology, Integer.parseInt(number), numOfPlayer);

			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // while

	}

	public double returnSpeed(Speed speed, AbstractStaticObject object) {
		// SERVE PER CONVERTIRE LA SPEED AD UN INTERO
		if (speed == Speed.SLOW) {
			return 0.1d;
		} else if (speed == Speed.NORMAL) {
			return 0.2d;
		} else if (speed == Speed.FAST) {
			return 0.3d;
		} else if (speed == Speed.SLOWROCKET) {
			return 0.4d;
		} else if (speed == Speed.NORMALROCKET) {
			return 0.5d;
		} else if (speed == Speed.FASTROCKET) {
			return 0.6d;
		}

		return 0.0d;
	}

	// ----------------------------------------POWERUP-------------------------------------

	private void extendAddPowerUp(PowerUp tmp) {

		tmp.setDropTime(durationPowerUp);
		tmp.setBefore(getMatrix().world[getX()][getY()]); // prima di spostare
															// powerUp mi salvo
															// l oggetto su cui
															// �
															// caduto
															// precedentemente.
		if (tmp.getBefore() instanceof Water) {
			tmp.setBeforeWater(getMatrix().world[xTmp][yTmp]); // mi salvo l
																// oggetto che
																// verr�
																// sovvraascritto
			tmp.setX(xTmp); // powerUp viene spostato dall acqua alla cella
							// accanto (pos buona )
			tmp.setY(yTmp);
		}
		tmp.setDropDirection(dir);
		power.add(tmp);
		if (getMatrix().world[getX()][getY()] instanceof BrickWall)
			((BrickWall) getMatrix().world[getX()][getY()]).setBefore(tmp);
		else if (getMatrix().world[getX()][getY()] instanceof SteelWall)
			((SteelWall) getMatrix().world[getX()][getY()]).setBefore(tmp);
		else
			getMatrix().world[tmp.getX()][tmp.getY()] = tmp; // attenzione al
																// tmp.getX();
	}

	public void addPowerUp(int t) {

		PowerUp tmp = null;
		foundPosition();

		soundPowerUp = true;

		switch (t) {
		case 0:
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.GRENADE);
			extendAddPowerUp(tmp);
			break;
		case 1:
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.HELMET);
			extendAddPowerUp(tmp);
			break;
		case 2:
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.SHOVEL);
			extendAddPowerUp(tmp);
			break;
		case 3:
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.STAR);
			extendAddPowerUp(tmp);
			break;
		case 4:
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.TANK);
			extendAddPowerUp(tmp);
			break;
		case 5:
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.TIMER);
			extendAddPowerUp(tmp);
			break;
		default:
			break;
		}
	}

	private boolean movePowerUpInCorrectPosition() {

		// CONTROLLO POS DEL POWERUP SE E' BUONA

		if (x - 1 >= 0 && !(getMatrix().world[x - 1][y] instanceof Water)
				&& !(getMatrix().world[x - 1][y] instanceof EnemyTank)
				&& !(getMatrix().world[x - 1][y] instanceof PlayerTank)) { // UP

			xTmp = x - 1; // necessito sapere le coordinate della nuova pos.
							// buona su cui verr� spostato in seguito
			yTmp = y; // il powerUp, qui non � possibile farlo perke prima mi
						// devo creare il powerUp e il before e poi...
			dir = Direction.UP;

			return true;
		}
		if (x + 1 < height && !(getMatrix().world[x + 1][y] instanceof Water)
				&& !(getMatrix().world[x + 1][y] instanceof EnemyTank)
				&& !(getMatrix().world[x + 1][y] instanceof PlayerTank)) { // DOWN
			xTmp = x + 1;
			yTmp = y;
			dir = Direction.DOWN;
			return true;
		}
		if (y - 1 >= 0 && !(getMatrix().world[x][y - 1] instanceof Water)
				&& !(getMatrix().world[x][y - 1] instanceof EnemyTank)
				&& !(getMatrix().world[x][y - 1] instanceof PlayerTank)) { // LEFT
			xTmp = x;
			yTmp = y - 1;
			dir = Direction.LEFT;
			return true;
		}
		if (y + 1 < width && !(getMatrix().world[x][y + 1] instanceof Water)
				&& !(getMatrix().world[x][y + 1] instanceof EnemyTank)
				&& !(getMatrix().world[x][y + 1] instanceof PlayerTank)) { // RIGHT
			xTmp = x;
			yTmp = y + 1;
			dir = Direction.RIGHT;
			return true;
		}
		return false;
	}

	public void foundPosition() {
		boolean flag = false;

		while (!flag) {
			x = random.nextInt(height);
			y = random.nextInt(width);
			if (!(getMatrix().world[x][y] instanceof PlayerTank) && !(getMatrix().world[x][y] instanceof EnemyTank)
					&& !(getMatrix().world[x][y] instanceof PowerUp) && !(getMatrix().world[x][y] instanceof Rocket)
					&& !(getMatrix().world[x][y] instanceof Flag && getMatrix().world[x][y] != null)) {
				flag = true;
			}
			if (getMatrix().world[x][y] instanceof Water) // se cade nell'acqua
															// controlla
				if (!movePowerUpInCorrectPosition()) // se la condizione non �
														// soddisfatta
					flag = false; // continua a ciclare
		}
	}

	private void managePowerUp(PowerUp p) {

		playerTmp.getStatistics().calculate(p);

		if (p.getPowerUp() == Power.HELMET) {
			((Tank) p.getTank()).setProtection(false);
		} else if (p.getPowerUp() == Power.SHOVEL) {
			buildWall("recover");
		} else if (p.getPowerUp() == Power.TIMER) {
			for (int i = 0; i < enemy.size(); i++) {
				if (enemy.get(i).isStopEnemy()) {
					enemy.get(i).setStopEnemy(false);
					enemy.get(i).setStopEnemyGraphic(false);
				}
			}
		}
	}

	public void usePowerUp(PowerUp power) {
		switch (power.getPowerUp()) {
		case GRENADE:

			for (int i = 0; i < enemy.size(); i++)
				if (enemy.get(i).isAppearsInTheMap())
					destroyEnemyTank(enemy.get(i--));
			break;
		case HELMET:
			((Tank) power.getTank()).setProtection(true);
			break;
		case SHOVEL:
			buildWall("steel");
			break;
		case STAR:
			if (((PlayerTank) power.getTank()).getLevel() < 3) {
				((PlayerTank) power.getTank()).setLevel(((PlayerTank) power.getTank()).getLevel() + 1);

				if (((PlayerTank) power.getTank()).getLevel() == 1) {
					((PlayerTank) power.getTank()).setSpeed(Speed.NORMAL);
					((PlayerTank) power.getTank()).setSpeedShot(Speed.FASTROCKET);
				} else if (((PlayerTank) power.getTank()).getLevel() == 2) {
					((PlayerTank) power.getTank()).setSpeed(Speed.NORMAL);
					((PlayerTank) power.getTank()).setSpeedShot(Speed.NORMALROCKET);
				} else if (((PlayerTank) power.getTank()).getLevel() == 3) {
					((PlayerTank) power.getTank()).setSpeed(Speed.SLOW);
					((PlayerTank) power.getTank()).setSpeedShot(Speed.FASTROCKET);
				}
			}
			break;
		case TANK:
			((PlayerTank) power.getTank()).setResume(((PlayerTank) power.getTank()).getResume() + 1);
			break;
		case TIMER: // STOPPO SOLO NEMICI PRESENTI SULLA MAPPA IN QUEL MOMENTO
			for (int i = 0; i < enemy.size(); i++) {
				if (enemy.get(i).isAppearsInTheMap()) {
					enemy.get(i).setStopEnemy(true);
				}
			}
			break;
		}
	}

	private void buildWall(String S) {

		int x = flag.getX();
		int y = flag.getY();
		int reset = 0;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {

				if (!(i == x && j == y) && i >= 0 && j >= 0 && j < width && i < height) {
					if (S == "steel") {
						recoveryWall.add(getMatrix().world[i][j]);
						matrix.world[i][j] = new SteelWall(i, j, matrix, 4);
					} else if (S == "recover" && reset < recoveryWall.size()) {
						getMatrix().world[i][j] = recoveryWall.get(reset++);
					}
				}
			}
		}
		if (S == "recover")
			recoveryWall.clear();
	}

	public boolean isPresent(Tank t, PowerUp p) {
		for (int i = 0; i < power.size(); i++)
			if (power.get(i).getTank() == t && power.get(i).getPowerUp() == p.getPowerUp())
				return true;
		return false;
	}

	public void sumPowerUp(Tank t, PowerUp p) {
		for (int i = 0; i < power.size(); i++)
			if (power.get(i).getPowerUp().equals(p.getPowerUp()) && power.get(i).getTank() == t) {
				power.get(i).setTime(power.get(i).getTime() + power.get(i).getDuration());
			}
	}

	// ---------------------------------------ROCKET----------------------------------------

	public void updateRocket(Rocket rocket) {
		rocket.update();
		rocket.setUpdateObject(false);
	}

	public boolean collision(Rocket rocket) {

		int tile = 35;
		AbstractStaticObject object = null;
		int up = -1, down = -1, right = -1, left = -1;

		if (rocket.getNext() instanceof Wall || rocket.getNext() instanceof Flag)
			object = rocket.getNext();

		// BORDO
		if (object == null) {
			up = 0;
			down = (getMatrix().getRow() * tile);
			right = (getMatrix().getColumn() * tile);
			left = 0;
			if (rocket.rect.getX() < up) {
				return true;
			} else if ((rocket.rect.getX() + 9) > down) {
				return true;
			} else if (rocket.rect.getY() < left) {
				return true;
			} else if ((rocket.rect.getY() + 9) > right) {
				return true;
			}
		}

		if (!rocket.rect.contains(rocket.getTank().rect)) {
			// FLAG
			if (object instanceof Flag) {
				if (object.rect.intersects(rocket.rect)) {
					if (!effects.contains(flag)) {
						effects.add(flag);
						flag.setHit(true);
						explosion = true;
						return true;
					}
				}
			}

			// WALL
			if ((object instanceof Wall) && (object instanceof BrickWall || object instanceof SteelWall)) {
				if (object.rect.intersects(rocket.rect)) {
					damageWall(rocket);
					if (((Wall) object).getHealth() <= 0)
						destroyWall(rocket);
					return true;
				}
			}

			// CONTROLLO SE IL ROCKET HA INTERSECATO QUALCHE ROCKET
			for (int b = 0; b < getRocket().size(); b++) {
				if (rocket != getRocket().get(b) && rocket.rect.intersects(getRocket().get(b).rect)
						&& rocket.getTank() != getRocket().get(b).getTank()) {
					destroyRocket(getRocket().get(b));
					return true;
				}
			}

			// CONTROLLO SE IL ROCKET HA INTERSECATO UN PLAYER TANK
			for (int a = 0; a < getPlayersArray().size(); a++) {
				if (!getPlayersArray().get(a).isDied() && rocket.rect.intersects(getPlayersArray().get(a).rect)
						&& rocket.getTank() != getPlayersArray().get(a)) {
					if (rocket.getTank() instanceof EnemyTank) {
						if (!getPlayersArray().get(a).isProtection() && !getPlayersArray().get(a).isReadyToSpawn()) {
							switchCurrTank(getPlayersArray().get(a));
							destroyPlayerTank(getPlayersArray().get(a));
						}
					}
					return true;
				}
			}

			// CONTORLLO SE IL ROCKET HA INTERESECATO UN ENEMY OK (V)
			for (int a = 0; a < getEnemy().size(); a++) {
				if (getEnemy().get(a).isAppearsInTheMap() && getEnemy().get(a).rect.intersects(rocket.rect)
						&& rocket.getTank() != getEnemy().get(a)) {
					if (rocket.getTank() instanceof PlayerTank) {

						// TODO
						playerTmp = (PlayerTank) rocket.getTank();

						if (getEnemy().get(a).isProtection() == false) {
							damageEnemyTank(getEnemy().get(a));
						} else {
							getEnemy().get(a).setProtection(true);
						}

						if (getEnemy().get(a).getHealth() == 0) {
							switchCurrTank(getEnemy().get(a));
							destroyEnemyTank(getEnemy().get(a));
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	public void destroyRocket(Rocket r) {

		countRockets(r);

		if (r.getCurr() != r.getTank())
			matrix.world[r.getX()][r.getY()] = r.getCurr();

		effects.add(r);
		rocket.remove(r);

	}

	public void countRockets(Rocket r) {
		if (!(r.getTank() instanceof EnemyTank) && r.getTank().getContRocket() > 0)
			r.getTank().setContRocket(r.getTank().getContRocket() - 1);
		else {
			for (int b = 0; b < enemy.size(); b++) {
				if (enemy.get(b) == r.getTank()) {
					enemy.get(b).setContRocket(enemy.get(b).getContRocket() - 1);
					break;
				}
			}
		}
	}

	private void damageEnemyTank(EnemyTank enemyTank) {
		enemyTank.setHealth(enemyTank.getHealth() - 1);
	}

	private void damageWall(Rocket rocket) {

		if (rocket.getTank() instanceof PlayerTank && ((PlayerTank) rocket.getTank()).getLevel() == 3)
			((Wall) rocket.getNext()).setHealth(((Wall) rocket.getNext()).getHealth() - 2);
		else if (!(rocket.getNext() instanceof SteelWall)) // e non �
															// SteelWall
			((Wall) rocket.getNext()).setHealth(((Wall) rocket.getNext()).getHealth() - 1);
	}

	private void destroyPlayerTank(PlayerTank player) {
		PlayerTank old = player;
		effects.add(old);
		getMatrix().world[old.getX()][old.getY()] = old.getCurr();
		explosion = true;
		player = new PlayerTank(player.getBornX(), player.getBornY(), matrix, old.toString());
		matrix.world[player.getX()][player.getY()] = player;
		player.setOldDirection(false);
		player.setResume(old.getResume() - 1);
		if (player.getResume() < 0) {
			matrix.world[player.getX()][player.getY()] = null;
			player.setDied(true);
			for (int a = 0; a < enemy.size(); a++) {
				int random = 0;
				do {
					random = new Random().nextInt(playersArray.size());
				} while (playersArray.get(random).isDied());
				enemy.get(a).setRandomObject(random);
			}
		}
		player.setSpawnTime((currentTime + 4) % 60);

		for (int a = 0; a < playersArray.size(); a++) {
			if (playersArray.get(a).equals(old)) {
				playersArray.set(a, player);
				break;
			}
		}

	}

	private void destroyWall(Rocket rocket) {

		int xR = rocket.getNext().getX();
		int yR = rocket.getNext().getY();

		if (matrix.world[xR][yR] instanceof BrickWall)
			matrix.world[xR][yR] = ((BrickWall) matrix.world[xR][yR]).getBefore();
		else if (matrix.world[xR][yR] instanceof SteelWall)
			matrix.world[xR][yR] = ((SteelWall) matrix.world[xR][yR]).getBefore();
		else
			matrix.world[xR][yR] = null;
	}

	private void destroyEnemyTank(EnemyTank enemyT) {

		playerTmp.getStatistics().calculate(enemyT);

		if (numberOfEnemyOnMap > 0)
			numberOfEnemyOnMap--;

		if (numberOfEnemyReadyToSpwan > 0)
			numberOfEnemyReadyToSpwan--;

		// GENERA POWERUP
		if (enemyT.isPowerUpOn())
			// addPowerUp(new Random().nextInt(6));
			addPowerUp(1);

		// RIMETTI CURR
		matrix.world[enemyT.getX()][enemyT.getY()] = enemyT.getCurr();
		setNumbersOfEnemy(getNumbersOfEnemy() - 1);
		effects.add(enemyT);
		enemy.remove(enemyT);
		explosion = true;
	}

	public void createRocketTank(Direction tmp, AbstractDynamicObject tank) {

		if ((tank instanceof PlayerTank && ((PlayerTank) tank).getLevel() > 1 && tank.getContRocket() < 2)
				|| (tank instanceof PlayerTank && ((PlayerTank) tank).getLevel() <= 1 && tank.getContRocket() == 0)
				|| (tank instanceof EnemyTank && tank.getContRocket() == 0)) {

			if (tmp == Direction.STOP && tank instanceof PlayerTank)
				tmp = Direction.UP;

			if (tank instanceof PlayerTank)
				((PlayerTank) tank).setShot(true);

			rocket.add(new Rocket(tank.getX(), tank.getY(), matrix, tmp, tank));

			tank.setContRocket(tank.getContRocket() + 1); // conta rocket
		}
	}

	private void switchCurrTank(AbstractDynamicObject tank) {
		for (int a = 0; a < rocket.size(); a++)
			if (rocket.get(a).getTank() == tank && rocket.get(a).getCurr() == tank)
				rocket.get(a).setCurr(tank.getCurr());
	}

	// -------------------------------------ENEMY-------------------------------------------

	public void addEnemies(String T, int N, int numOfPlayer) {

		int c = 0;
		int saveLastPosition = 0;

		while (c < N) {
			chooseEnemy(T, pos[saveLastPosition % 3], numOfPlayer);
			saveLastPosition++;
			c++;
		}
	}

	private void chooseEnemy(String typology, int y, int numOfPlayer) {
		switch (typology) {
		case "basic":
			enemy.add(new BasicTank(0, y, matrix, Direction.STOP, numOfPlayer));
			break;
		case "fast":
			enemy.add(new FastTank(0, y, matrix, Direction.STOP, numOfPlayer));
			break;
		case "power":
			enemy.add(new PowerTank(0, y, matrix, Direction.STOP, numOfPlayer));
			break;
		case "armor":
			enemy.add(new ArmorTank(0, y, matrix, Direction.STOP, numOfPlayer));
			break;
		}
		if ((enemy.size() % numEnemyDropsPowerUp) == 0) { // ogni quanti nemici
															// assegnare powerUp
			enemy.get(enemy.size() - 1).setPowerUpOn(true);
		}
	}

	public void spawnEnemy() {

		int count = 0;

		while (count < enemy.size() && numberOfEnemyOnMap < numberOfEnemyToSpawn) {

			if (numberOfEnemyReadyToSpwan < numberOfEnemyToSpawn && !enemy.get(count).isReadyToSpawn()
					&& !enemy.get(count).isAppearsInTheMap() && isFree(enemy.get(count))) {
				enemy.get(count).setReadyToSpawn(true);
				enemy.get(count).setSpawnTime((currentTime + 4) % 60); // +4
																		// spawntime
				numberOfEnemyReadyToSpwan++;
			}

			else if (enemy.get(count).isReadyToSpawn() && !(isFree(enemy.get(count)))) {
				enemy.get(count).setY(changeSpawnPosition(enemy.get(count)));
				enemy.get(count).setReadyToSpawn(false);
				enemy.get(count).setSpawnTime(0); // +4 spawntime
				numberOfEnemyReadyToSpwan--;
				return;
			}

			else if (enemy.get(count).isReadyToSpawn() && currentTime == enemy.get(count).getSpawnTime()) {
				enemy.get(count).setAppearsInTheMap(true);
				enemy.get(count).setReadyToSpawn(false);
				numberOfEnemyOnMap++;
			}
			count++;
		}
	}

	private int changeSpawnPosition(EnemyTank e) {

		for (int i = 0; i < pos.length; i++)
			if (e.getY() == pos[i])
				return pos[(i + 1) % pos.length];
		return 0;
	}

	public boolean isFree(EnemyTank e) {
		for (int i = 0; i < enemy.size(); i++)
			if ((e.getX() == enemy.get(i).getX() && e.getY() == enemy.get(i).getY()
					&& enemy.get(i).isAppearsInTheMap()))
				return false;

		for (int i = 0; i < playersArray.size(); i++)
			if (e.getX() == playersArray.get(i).getX() && e.getY() == playersArray.get(i).getY()) {
				return false;
			}

		return true;
	}

	// ------------------------------ONLINE------------------------------------------

	// STRING TO DATA
	public void parseStatusFromString(String status) {
		String [] elements=status.split("#");
		String [] variableOfSystem=elements[0].split(";");
		String [] map=elements[1].split(";");
		String [] players=elements[2].split(";");
		String [] enemy=elements[3].split(";");
		String [] rockets=elements[4].split(";");
	
		for(String s : variableOfSystem){
			String [] split= s.split(":");
			numbersOfEnemy=Integer.parseInt(split[0]);
		}	
		
		int x=0;
		for(String s : map){
			String [] split= s.split(":");
			int y=0;
			for(String s1: split){
				if(s1.equals("null")){
					getMatrix().world[x][y]=null;
				}else if(s1.equals("[//]")){
					getMatrix().world[x][y] = new SteelWall(x, y, getMatrix(), 4);
				}else if(s1.equals("@@@@")){
					getMatrix().world[x][y] = new Ice(x, y, getMatrix());
				}else if(s1.equals("TTTT")){
					getMatrix().world[x][y] = new Tree(x,y, getMatrix());
				}else if(s1.equals("[  ]")){
					getMatrix().world[x][y] = new BrickWall(x, y, getMatrix(), 2);
				}else if(s1.equals("~~~~")){
					getMatrix().world[x][y] = new Water(x, y, getMatrix());
				}else if(s1.equals(" && ")){
					flag = new Flag(x, y, matrix);
					getMatrix().world[x][y] = flag;
				}
				y++;
			}
			x++;
		}

		
		for(String s : players){
			String [] split= s.split(":");
			for(int a=0; a<getPlayersArray().size(); a++){
				if(getPlayersArray().get(a).toString().equals(split[0])){
					getPlayersArray().get(a).setxGraphics(Double.parseDouble(split[1]));
					getPlayersArray().get(a).setyGraphics(Double.parseDouble(split[2]));
					getPlayersArray().get(a).setTmpDirection(Direction.valueOf(split[3]));
					getPlayersArray().get(a).setKeyPressedMillis(Long.parseLong(split[4]));
					getPlayersArray().get(a).setPressed(Boolean.parseBoolean(split[5]));
				}
			}
		}
		
		lock.lock();
		getEnemy().clear();
		for(String s : enemy){
			String [] split= s.split(":");
			if(split[0].equals(" AT ")){
				getEnemy().add(new ArmorTank(Integer.parseInt(split[1]), Integer.parseInt(split[2]), matrix, Direction.valueOf(split[5]),2));
			}else if(split[0].equals(" BT ")){
				getEnemy().add(new BasicTank(Integer.parseInt(split[1]), Integer.parseInt(split[2]), matrix, Direction.valueOf(split[5]),2));
			}else if(split[0].equals(" FT ")){
				getEnemy().add(new FastTank(Integer.parseInt(split[1]), Integer.parseInt(split[2]), matrix, Direction.valueOf(split[5]),2));
			}else if(split[0].equals(" PT ")){
				getEnemy().add(new FastTank(Integer.parseInt(split[1]), Integer.parseInt(split[2]), matrix, Direction.valueOf(split[5]),2));
			}
			getEnemy().get(getEnemy().size()-1).setxGraphics(Double.parseDouble(split[3]));
			getEnemy().get(getEnemy().size()-1).setyGraphics(Double.parseDouble(split[4]));
			getEnemy().get(getEnemy().size()-1).setTmpDirection(Direction.valueOf(split[5]));
			getEnemy().get(getEnemy().size()-1).setAppearsInTheMap(Boolean.parseBoolean(split[6]));
			getEnemy().get(getEnemy().size()-1).setReadyToSpawn(Boolean.parseBoolean(split[7]));
		}	
		lock.unlock();
		
		//HO DOVUTO METTERE IL LOCK QUI XK � LA PARTE DOVE VIENE FATTO CLEAR E VIENE ANCHE DISEGNATO...XK C'� ANCHE IL LOCK NEL PAINT COMPONENT NELLA PARTE ROCKET
		lock.lock();
		if (rockets.length > 1 || rockets.length == 1 && !rockets[0].trim().isEmpty()) {
			rocket.clear();
			for(String s : rockets){
				String [] split= s.split(":");
				getRocket().add(new Rocket(Integer.parseInt(split[0]), Integer.parseInt(split[1]), matrix, Direction.valueOf(split[2]), null));
				getRocket().get(getRocket().size()-1).setxGraphics(Double.parseDouble(split[3]));
				getRocket().get(getRocket().size()-1).setyGraphics(Double.parseDouble(split[4]));
				getRocket().get(getRocket().size()-1).setFirstAnimationNo(Boolean.parseBoolean(split[5]));
			}
		}
		lock.unlock();			
	}

	// DATA TO STRING
	public String statusToString() {
		StringBuilder stringBuilder = new StringBuilder();
		
		//QUI MANDO VARIABILI DI SISTEMA, COME PER ESEMPIO SIZE DEGLI ENEMY ECC
		stringBuilder.append(getEnemy().size()+";");
		stringBuilder.append("#");
		
		for (int a = 0; a < getMatrix().getRow(); a++) {
			for (int b = 0; b < getMatrix().getColumn(); b++) {
				if(matrix.world[a][b]==null){
					stringBuilder.append("null"+":");
				}else{
					stringBuilder.append(matrix.world[a][b].toString()+":");
				}
			}
			stringBuilder.append(";");
		}
		stringBuilder.append("#");

		for (int a = 0; a < getPlayersArray().size(); a++) {
			stringBuilder.append(build(getPlayersArray().get(a)));
		}
		stringBuilder.append("#");

		for (int a = 0; a < getEnemy().size(); a++) {
			stringBuilder.append(build(getEnemy().get(a)));
		}
		stringBuilder.append("#");

		if (getRocket().isEmpty()) {
			stringBuilder.append(" ");
		} else {
			for (int a = 0; a < getRocket().size(); a++) {
				stringBuilder.append(build(getRocket().get(a)));
			}
		}

		return stringBuilder.toString();
	}

	private String build(AbstractStaticObject ob) {

		if (ob instanceof PlayerTank) {
			PlayerTank p = ((PlayerTank) ob);
			return (p.toString() + ":" + p.getxGraphics() + ":" + p.getyGraphics() + ":" + p.getTmpDirection() + ":"
					+ p.getKeyPressedMillis() + ":" + p.isPressed() + ";");
		} else if (ob instanceof EnemyTank) {
			EnemyTank e = ((EnemyTank) ob);
			return (e.toString() + ":" + e.getX() + ":" + e.getY() + ":" + e.getxGraphics() + ":" + e.getyGraphics()
					+ ":" + e.getTmpDirection() + ":" + e.isAppearsInTheMap() + ":" + e.isReadyToSpawn() + ";");
		} else if (ob instanceof Rocket) {
			Rocket r = ((Rocket) ob);
			return (r.getX() + ":" + r.getY() + ":" + r.getDirection() + ":" + r.getxGraphics() + ":" + r.getyGraphics()
					+ ":" + r.isFirstAnimationNo() + ";");
		} else if (ob instanceof PowerUp) {

		}

		return " ";
	}

	// -----------------------------SET&GET--------------------------------------

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
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

	public ArrayList<Rocket> getRocket() {
		return rocket;
	}

	public void setRocket(ArrayList<Rocket> rocket) {
		this.rocket = rocket;
	}

	public ArrayList<AbstractStaticObject> getRecoveryWall() {
		return recoveryWall;
	}

	public void setRecoveryWall(ArrayList<AbstractStaticObject> recoveryWall) {
		this.recoveryWall = recoveryWall;
	}

	public int getDurationPowerUp() {
		return durationPowerUp;
	}

	public void setDurationPowerUp(int durationPowerUp) {
		this.durationPowerUp = durationPowerUp;
	}

	public int getNumberOfEnemyToSpawn() {
		return numberOfEnemyToSpawn;
	}

	public void setNumberOfEnemyToSpawn(int numberOfEnemyToSpawn) {
		this.numberOfEnemyToSpawn = numberOfEnemyToSpawn;
	}

	public int getNumberOfEnemyOnMap() {
		return numberOfEnemyOnMap;
	}

	public void setNumberOfEnemyOnMap(int numberOfEnemyOnMap) {
		this.numberOfEnemyOnMap = numberOfEnemyOnMap;
	}

	public boolean isSoundPowerUp() {
		return soundPowerUp;
	}

	public void setSoundPowerUp(boolean soundPowerUp) {
		this.soundPowerUp = soundPowerUp;
	}

	public JTextField getFilename() {
		return filename;
	}

	public void setFilename(JTextField filename) {
		this.filename = filename;
	}

	public boolean isExplosion() {
		return explosion;
	}

	public void setExplosion(boolean explosion) {
		this.explosion = explosion;
	}

	public LinkedList<PlayerTank> getPlayersArray() {
		return playersArray;
	}

	public void setPlayersArray(LinkedList<PlayerTank> playersArray) {
		this.playersArray = playersArray;
	}

	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

	public boolean isWaitToExit() {
		return waitToExit;
	}

	public void setWaitToExit(boolean waitToExit) {
		this.waitToExit = waitToExit;
	}

	public int getNumbersOfEnemy() {
		return numbersOfEnemy;
	}

	public void setNumbersOfEnemy(int numbersOfEnemy) {
		this.numbersOfEnemy = numbersOfEnemy;
	}
}

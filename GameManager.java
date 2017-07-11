package progettoIGPE.davide.giovanni.unical2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
	
	//OTHER
	public static Flag flag;
	public static boolean singlePlayer; 
	public static boolean offline = false;
	public static long currentTime;
	private boolean medium = false;
	
	private boolean exit;
	private JTextField filename;
	private boolean explosion;
	public Lock lock = new ReentrantLock();
	public Runnable runnable = null;
	private boolean soundPowerUp;
	public boolean pauseOptionDialog;
	public boolean paused;
	private Random random;
	private World matrix;
	private boolean shotEnabled;
	private LinkedList<PlayerTank> playersArray;
	private ArrayList<EnemyTank> enemy;
	private ArrayList<PowerUp> power;
	private ArrayList<Rocket> rocket;
	private ArrayList<AbstractStaticObject> recoveryWall;
	private ArrayList<AbstractStaticObject> effects;

	// ENEMY
	int pos[] = { 0, width / 2, width - 1 };
	private int numberOfEnemyToSpawn;
	private int numberOfEnemyOnMap;
	private int numberOfEnemyReadyToSpwan;
	private int numbersOfEnemiesOnline;

	// POWERUPS
	private int x;
	private int y;
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
	
	// OFFLINE
	public GameManager(JTextField filename) {
		GameManager.offline = true;
		startGameManager(filename);
	}

	// ONLINE CLIENT
	public GameManager(JTextField filename, String name) {
		matrix = new World(height, width);
		enemy = new ArrayList<>();
		rocket = new ArrayList<>();
		effects = new ArrayList<>();
		playersArray = new LinkedList<>();
		power = new ArrayList<>();
		GameManager.offline = false;
		// crea player
		if (name.equals("P1")) {
			playersArray.addFirst(new PlayerTank(19, 8, getMatrix(), name));
			getMatrix().world[19][8] = playersArray.get(0);
						
			playersArray.addLast(new PlayerTank(19, 12, getMatrix(), "P2"));
			getMatrix().world[19][12] = playersArray.get(1);
		} else if (name.equals("P2")) {
			playersArray.addFirst(new PlayerTank(19, 12, getMatrix(), name));
			getMatrix().world[19][12] = playersArray.get(0);
			
			playersArray.addLast(new PlayerTank(19, 8, getMatrix(), "P1"));
			getMatrix().world[19][8] = playersArray.get(1);
		}
	}

	// ONLINE SERVER
	public GameManager(Runnable runnable, HashMap<String, String> name, JTextField filename) {
		GameManager.offline = false;
		GameManager.singlePlayer=false;
		this.runnable = runnable;
		startGameManager(filename);

		
		for(Map.Entry<String, String> entry : name.entrySet()) {
			String key=entry.getKey();
		    String names= entry.getValue();	
		    if (names.equals("P1")) {
				playersArray.addFirst(new PlayerTank(19, 8, getMatrix(), names));
				((PlayerTank)getPlayersArray().get(0)).setNameOfPlayerTank(key);
				getMatrix().world[19][8] = playersArray.get(0);
			} else if (names.equals("P2")) {
				playersArray.add(new PlayerTank(19, 12, getMatrix(), names));
				if(playersArray.size()==1){
					getMatrix().world[19][12] = playersArray.get(0);
					((PlayerTank)getPlayersArray().get(0)).setNameOfPlayerTank(key);
				}
				else{
					getMatrix().world[19][12] = playersArray.get(1);
					((PlayerTank)getPlayersArray().get(1)).setNameOfPlayerTank(key);
				}
			}
		}
	}

	// USED FOR CONSTRUCTION
	public GameManager(World world, Flag flag) {
		this.matrix = world;
		GameManager.flag = flag;
	}

	// --------------------------------------OTHER-----------------------------------------
	public void startGameManager(JTextField filename) {
		currentTime = 0;
		pauseOptionDialog = false;
		paused = false;
		numberOfEnemyOnMap = 0;
		
		numberOfEnemyReadyToSpwan = 0;
		durationPowerUp = 20;
		numEnemyDropsPowerUp = 1; // indica ogni quanti enemie far cadere
									// powerUp
		shotEnabled = true; // i nemici possono sparare
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

		importMap(filename);

		if (singlePlayer)
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
		setNumbersOfEnemiesOnline(enemy.size());
	}

	public class MyTask extends TimerTask {

		public void run() {


			if(!paused) {
			// STAMPA
//			 getMatrix().print();
//			 System.out.println();

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

	public class CurrentTime extends TimerTask {

		public void run() {
			if(!paused) {
				currentTime = (currentTime + 1) % 60;

				if(medium) { //difficoltà media
					for(int i=0;i<enemy.size();i++) {
						if(enemy.get(i).isAppearsInTheMap())
							enemy.get(i).setSwitchT(enemy.get(i).getSwitchT()+1);
					}
				}
				
				
				for (int a = 0; a < power.size(); a++) {

					if (power.get(a).isActivate()) {			
						power.get(a).setTime(power.get(a).getTime() - 1);		
						
						// EFFETTO LAMPEGGIO SHOVEL
						if(power.get(a).getPowerUp() == Power.SHOVEL && power.get(a).getTime() <= 5) {
						
							if(power.get(a).isBlinkShovel()) {
								buildWall("steel");
								power.get(a).setBlinkShovel(false);
							}
							else {
								
								buildWall("brick");
								power.get(a).setBlinkShovel(true);
							}
						}

						if (power.get(a).getTime() <= 0) {
//							System.out.println(power.get(a) + "---------- disattivo!");
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
			}// paused
		}
	}

	public void importMap(JTextField filename) {
		int i = 0;// indice di riga
		try {
			BufferedReader reader = null;
			if(filename.getText().contains("single")){
				GameManager.singlePlayer=true;
			}else{
				GameManager.singlePlayer=false;
			}

			reader = new BufferedReader(new FileReader(filename.getText()));

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
		
			if(singlePlayer){
				importEnemies(reader, line, 1);
			}else{
				importEnemies(reader, line, 2);
			}
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
															// ï¿½
															// caduto
															// precedentemente.
		if (tmp.getBefore() instanceof Water) {
			tmp.setBeforeWater(getMatrix().world[xTmp][yTmp]); // mi salvo l
																// oggetto che
																// verrï¿½
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
							// buona su cui verrï¿½ spostato in seguito
			yTmp = y; // il powerUp, qui non ï¿½ possibile farlo perke prima mi
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
					&& !(getMatrix().world[x][y] instanceof Flag && getMatrix().world[x][y] != null) && !spawnPosition(x,y)) {
				flag = true;
			}
			if (getMatrix().world[x][y] instanceof Water) // se cade nell'acqua
															// controlla
				if (!movePowerUpInCorrectPosition()) // se la condizione non ï¿½
														// soddisfatta
					flag = false; // continua a ciclare
		}
	}

	private void managePowerUp(PowerUp p) {
		
		if(p.getTank() instanceof PlayerTank)
			((PlayerTank)p.getTank()).getStatistics().calculate(p);

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
				if (enemy.get(i).isAppearsInTheMap()) {
					((PlayerTank)power.getTank()).getStatistics().calculate(power);
					destroyEnemyTank(enemy.get(i--));
				}
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
			stopEnemies();
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
						if(!(getMatrix().world[i][j] instanceof Tank) &&
							!(getMatrix().world[i][j] instanceof Rocket))
						recoveryWall.add(getMatrix().world[i][j]);
						matrix.world[i][j] = new SteelWall(i, j, matrix, 4);
					}
					else if(S == "brick"){
						if(!(getMatrix().world[i][j] instanceof Tank) &&
								!(getMatrix().world[i][j] instanceof Rocket))
							recoveryWall.add(getMatrix().world[i][j]);
							matrix.world[i][j] = recoveryWall.get(reset++);
					}
					else if (S == "recover" && reset < recoveryWall.size()) {
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

	public boolean spawnPosition(int x, int y) {
		if( (x == 0 && y == 0) || (x == 0 && y == width/2 ) || (x == 0 && y == width - 1 ))
			return true;
		return false;
	}
	
	public void stopEnemies() {
		for (int i = 0; i < enemy.size(); i++) {
			if (enemy.get(i).isAppearsInTheMap()) {
				enemy.get(i).setStopEnemy(true);
			}
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

						if (getEnemy().get(a).isProtection() == false) {
							damageEnemyTank(getEnemy().get(a));
						} else {
							getEnemy().get(a).setProtection(true);
						}

						if (getEnemy().get(a).getHealth() == 0) {
							((PlayerTank)rocket.getTank()).getStatistics().calculate(getEnemy().get(a));
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

		if(!effects.contains(r))
			effects.add(r);
//		System.out.println("destroyRocket");
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
		else if (!(rocket.getNext() instanceof SteelWall)) // e non ï¿½
															// SteelWall
			((Wall) rocket.getNext()).setHealth(((Wall) rocket.getNext()).getHealth() - 1);
	}

	public void destroyPlayerTank(PlayerTank player) {
		
		PlayerTank tmp = new PlayerTank(player.getX(), player.getY(), matrix, player.toString());
		if(!effects.contains(tmp))
			effects.add(tmp);

		matrix.world[player.getX()][player.getY()] = player.getCurr();
		explosion = true; //sound
		player.setOldDirection(false);
		player.setResume(player.getResume() - 1);
		player.setSpawnTime((currentTime + 4) % 60);
		player.setLevel(0);
		player.setReadyToSpawn(true);
		player.setCountdown(0);
		player.setInc(0);
		player.setRotateDegrees(0);
		player.setDirection(Direction.STOP);
		player.setTmpDirection(Direction.UP);
		player.setCurr(null);
		player.setX(player.getBornX());
		player.setY(player.getBornY());
		player.setxGraphics(player.getX()*35);
		player.setyGraphics(player.getY()*35);
		
		//---------
		//A-STAR ALGORITHM
		if (player.getResume() <= 0) {
			player.setDied(true);
			matrix.world[player.getX()][player.getY()]=null;	
			
			for (int a = 0; a < enemy.size(); a++) {
				int random = 0;
				do {
					random = new Random().nextInt(playersArray.size());
				} while (playersArray.get(random).isDied());
				enemy.get(a).setRandomObject(random);
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

		if (numberOfEnemyOnMap > 0)
			numberOfEnemyOnMap--;

		if (numberOfEnemyReadyToSpwan > 0)
			numberOfEnemyReadyToSpwan--;

		// GENERA POWERUP
		if (enemyT.isPowerUpOn())
//			 addPowerUp(new Random().nextInt(6));
			addPowerUp(5);
			

		// RIMETTI CURR
		matrix.world[enemyT.getX()][enemyT.getY()] = enemyT.getCurr();
		setNumbersOfEnemiesOnline(getNumbersOfEnemiesOnline() - 1);
		if(!effects.contains(enemyT))
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
		String[] elements = status.split("#");
		String[] variableOfSystem = elements[0].split(";");
		String[] map = elements[1].split(";");
		String[] mapObjectStatic=elements[2].split(";");
		String[] players = elements[3].split(";");
		String[] enemy = elements[4].split(";");
		String[] rockets = elements[5].split(";");
		String[] powerUp = elements[6].split(";");
		String[] effects = elements[7].split(";");
		String[] flagElement=elements[8].split(";");
		String[] sounds=elements[9].split(";");
		
		for (String s : variableOfSystem) {
			String[] split = s.split(":");
			setNumbersOfEnemiesOnline(Integer.parseInt(split[0]));
			exit=Boolean.parseBoolean(split[1]);
		}

		int x = 0;
		for (String s : map) {
			String[] split = s.split(":");
			int y = 0;
			for (String s1 : split) {
				if (s1.equals("null")) {
					getMatrix().world[x][y] = null;
				} else if (s1.equals("[//]")) {
					getMatrix().world[x][y] = new SteelWall(x, y, getMatrix(), 4);
				}  else if (s1.equals("[  ]")) {
					getMatrix().world[x][y] = new BrickWall(x, y, getMatrix(), 2);
				}
				y++;
			}
			x++;
		}
		
		x=0;
		for (String s : mapObjectStatic) {
			String[] split = s.split(":");
			int y = 0;
			for (String s1 : split) {
				if (s1.equals("null")) {
					getMatrix().objectStatic[x][y] = null;
				}else if (s1.equals(" @@ ")) {
					getMatrix().objectStatic[x][y] = new Ice(x, y, getMatrix());
				}else if (s1.equals(" TT ")) {
					getMatrix().objectStatic[x][y] = new Tree(x, y, getMatrix());
				}else if (s1.equals(" ~~ ")) {
					getMatrix().objectStatic[x][y] = new Water(x, y, getMatrix());
				}
				y++;
			}
		x++;
		}

		for (String s : players) {
			String[] split = s.split(":");
			for (int a = 0; a < getPlayersArray().size(); a++) {
				if (getPlayersArray().get(a).toString().equals(split[0])) {
					getPlayersArray().get(a).setxGraphics(Double.parseDouble(split[1]));
					getPlayersArray().get(a).setyGraphics(Double.parseDouble(split[2]));
					getPlayersArray().get(a).setTmpDirection(Direction.valueOf(split[3]));
					getPlayersArray().get(a).setKeyPressedMillis(Long.parseLong(split[4]));
					getPlayersArray().get(a).setPressed(Boolean.parseBoolean(split[5]));
					getPlayersArray().get(a).setProtection(Boolean.parseBoolean(split[6]));
					getPlayersArray().get(a).setReadyToSpawn(Boolean.parseBoolean(split[7]));
					getPlayersArray().get(a).setCountdown(Integer.parseInt(split[8]));
					getPlayersArray().get(a).setResume(Integer.parseInt(split[9]));
					getPlayersArray().get(a).setExitOnline(Boolean.parseBoolean(split[11]));
					getPlayersArray().get(a).setNameOfPlayerTank(split[12]);
					getPlayersArray().get(a).setDied(Boolean.parseBoolean(split[13]));
				}
			}
		}

		lock.lock();
		if (enemy.length > 1 || enemy.length == 1 && !enemy[0].trim().isEmpty()) {
			getEnemy().clear();
			for (String s : enemy) {
				String[] split = s.split(":");
				if (split[1].equals(" AT ")) {
					getEnemy().add(new ArmorTank(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
							Direction.valueOf(split[6]), 2));
				} else if (split[1].equals(" BT ")) {
					getEnemy().add(new BasicTank(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
							Direction.valueOf(split[6]), 2));
				} else if (split[1].equals(" FT ")) {
					getEnemy().add(new FastTank(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
							Direction.valueOf(split[6]), 2));
				} else if (split[1].equals(" PT ")) {
					getEnemy().add(new FastTank(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
							Direction.valueOf(split[6]), 2));
				}
				getEnemy().get(getEnemy().size() - 1).setxGraphics(Double.parseDouble(split[4]));
				getEnemy().get(getEnemy().size() - 1).setyGraphics(Double.parseDouble(split[5]));
				getEnemy().get(getEnemy().size() - 1).setTmpDirection(Direction.valueOf(split[6]));
				getEnemy().get(getEnemy().size() - 1).setAppearsInTheMap(Boolean.parseBoolean(split[7]));
				getEnemy().get(getEnemy().size() - 1).setReadyToSpawn(Boolean.parseBoolean(split[8]));
				getEnemy().get(getEnemy().size() - 1).setProtection(Boolean.parseBoolean(split[10]));
				getEnemy().get(getEnemy().size() - 1).setCountdown(Integer.parseInt(split[11]));
			}
		}
		lock.unlock();

		// HO DOVUTO METTERE IL LOCK QUI XK è LA PARTE DOVE VIENE FATTO CLEAR E
		// VIENE ANCHE DISEGNATO...XK C'è ANCHE IL LOCK NEL PAINT COMPONENT
		// NELLA PARTE ROCKET
		lock.lock();
		if (rockets.length > 1 || rockets.length == 1 && !rockets[0].trim().isEmpty()) {
			rocket.clear();
			for (String s : rockets) {
				String[] split = s.split(":");
				getRocket().add(new Rocket(Integer.parseInt(split[1]), Integer.parseInt(split[2]), matrix,
						Direction.valueOf(split[3]), null));
				getRocket().get(getRocket().size() - 1).setxGraphics(Double.parseDouble(split[4]));
				getRocket().get(getRocket().size() - 1).setyGraphics(Double.parseDouble(split[5]));
				getRocket().get(getRocket().size() - 1).setFirstAnimationNo(Boolean.parseBoolean(split[6]));
			}
		}
		lock.unlock();

		lock.lock();
		if (powerUp.length > 1 || powerUp.length == 1 && !powerUp[0].trim().isEmpty()) {
			power.clear();
			for (String s : powerUp) {
				String[] split = s.split(":");
				if (split[1].equals("GRENADE")) {
					power.add(
							new PowerUp(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix, Power.GRENADE));
				} else if (split[1].equals("HELMET")) {
					power.add(
							new PowerUp(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix, Power.HELMET));
				} else if (split[1].equals("SHOVEL")) {
					power.add(
							new PowerUp(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix, Power.SHOVEL));
				} else if (split[1].equals("STAR")) {
					power.add(new PowerUp(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix, Power.STAR));
				} else if (split[1].equals("TANK")) {
					power.add(new PowerUp(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix, Power.TANK));
				} else if (split[1].equals("TIMER")) {
					power.add(new PowerUp(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix, Power.TIMER));
				}
				power.get(power.size() - 1).setBlink(Boolean.parseBoolean(split[4]));
				if (split[5].equals(" @@ ")) {
					power.get(power.size() - 1).setBefore(new Ice(0, 0, matrix));
				} else if (split[5].equals("[//]")) {
					power.get(power.size() - 1).setBefore(new SteelWall(0, 0, matrix, 4));
				} else if (split[5].equals(" TT ")) {
					power.get(power.size() - 1).setBefore(new Tree(0, 0, matrix));
				} else if (split[5].equals(" ~~ ")) {
					power.get(power.size() - 1).setBefore(new Water(0, 0, matrix));
				} else if (split[5].equals("[  ]")) {
					power.get(power.size() - 1).setBefore(new BrickWall(0, 0, matrix, 4));
				}

				if (split[6].equals("[  ]")) {
					power.get(power.size() - 1).setBeforeWater(new BrickWall(0, 0, matrix, 1));
				} else if (split[6].equals("[//]")) {
					power.get(power.size() - 1).setBeforeWater(new SteelWall(0, 0, matrix, 1));
				} else if (split[6].equals(" TT ")) {
					power.get(power.size() - 1).setBeforeWater(new Tree(0, 0, matrix));
				} else if (split[6].equals(" @@ ")) {
					power.get(power.size() - 1).setBeforeWater(new Ice(0, 0, matrix));
				}
				if (!split[7].equals("null"))
					power.get(power.size() - 1).setDropDirection(Direction.valueOf(split[7]));
				else
					power.get(power.size() - 1).setDropDirection(null);

				power.get(power.size() - 1).setActivate(Boolean.parseBoolean(split[9]));
				if (split[10].equals("P1") || split[10].equals("P2"))
					power.get(power.size() - 1).setTank(new PlayerTank(0, 0, matrix, split[10]));
				power.get(power.size() - 1).setTime(Integer.parseInt(split[11]));
			}
		}
		lock.unlock();
		
		lock.lock();
		if (effects.length > 1 || effects.length == 1 && !effects[0].trim().isEmpty()) {
			getEffects().clear();
			for (String s : effects) {
				String[] split = s.split(":");
				if (split[0].equals(" -- ")) {
					getEffects().add(new Rocket(Integer.parseInt(split[1]), Integer.parseInt(split[2]), matrix,
							Direction.valueOf(split[3]), null));
					((Rocket) getEffects().get(getEffects().size() - 1)).setInc(Integer.parseInt(split[7]));
					getEffects().get(getEffects().size() - 1).setxGraphics(Double.parseDouble(split[4]));
					getEffects().get(getEffects().size() - 1).setyGraphics(Double.parseDouble(split[5]));
				} else if (split[0].equals(" && ")) {
					getEffects().add(new Flag(Integer.parseInt(split[1]), Integer.parseInt(split[2]), matrix));
					((Flag) getEffects().get(getEffects().size() - 1)).setInc(Integer.parseInt(split[5]));
					getEffects().get(getEffects().size() - 1).setxGraphics(Double.parseDouble(split[3]));
					getEffects().get(getEffects().size() - 1).setyGraphics(Double.parseDouble(split[4]));
					((Flag) getEffects().get(getEffects().size() - 1)).setHit(Boolean.parseBoolean(split[6]));
				} else if (split[0].equals("ENEMY")) {
					if (split[1].equals(" AT ")) {
						getEffects().add(new ArmorTank(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
								Direction.valueOf(split[6]), 2));
					} else if (split[1].equals(" BT ")) {
						getEffects().add(new BasicTank(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
								Direction.valueOf(split[6]), 2));
					} else if (split[1].equals(" FT ")) {
						getEffects().add(new FastTank(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
								Direction.valueOf(split[6]), 2));
					} else if (split[1].equals(" PT ")) {
						getEffects().add(new FastTank(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
								Direction.valueOf(split[6]), 2));
					}
					getEffects().get(getEffects().size() - 1).setxGraphics(Double.parseDouble(split[4]));
					getEffects().get(getEffects().size() - 1).setyGraphics(Double.parseDouble(split[5]));
					((EnemyTank) getEffects().get(getEffects().size() - 1)).setInc(Integer.parseInt(split[9]));

				} else if (split[0].equals("POWERUP")) {
					if (split[1].equals("GRENADE")) {
						getEffects().add(new PowerUp(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
								Power.GRENADE));
					} else if (split[1].equals("HELMET")) {
						getEffects().add(new PowerUp(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
								Power.HELMET));
					} else if (split[1].equals("SHOVEL")) {
						getEffects().add(new PowerUp(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
								Power.SHOVEL));
					} else if (split[1].equals("STAR")) {
						getEffects().add(new PowerUp(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
								Power.STAR));
					} else if (split[1].equals("TANK")) {
						getEffects().add(new PowerUp(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
								Power.TANK));
					} else if (split[1].equals("TIMER")) {
						getEffects().add(new PowerUp(Integer.parseInt(split[2]), Integer.parseInt(split[3]), matrix,
								Power.TIMER));
					}
					((PowerUp) getEffects().get(getEffects().size() - 1)).setInc(Integer.parseInt(split[8]));
				}else if(split[0].equals("P1") || split[0].equals("P2")){
						getEffects().add(new PlayerTank(0, 0, matrix, ""));
						getEffects().get(getEffects().size() - 1).setxGraphics(Double.parseDouble(split[1]));
						getEffects().get(getEffects().size() - 1).setyGraphics(Double.parseDouble(split[2]));
						((PlayerTank)getEffects().get(getEffects().size() - 1)).setInc(Integer.parseInt(split[10]));
				}
			}
		}
		lock.unlock();
		
		lock.lock();
		for (String s : flagElement) {
			String[] split = s.split(":");
			flag=new Flag(Integer.parseInt(split[1]),Integer.parseInt(split[2]), matrix);
			flag.setHit(Boolean.parseBoolean(split[6]));
			getMatrix().world[Integer.parseInt(split[1])][Integer.parseInt(split[2])]=flag;
		}
		lock.unlock();
		
		for (String s : sounds) {
			String[] split = s.split(":");
			setSoundPowerUp(Boolean.parseBoolean(split[0]));
			setExplosion(Boolean.parseBoolean(split[1]));
			getPlayersArray().get(0).setShot(Boolean.parseBoolean(split[2]));
			getPlayersArray().get(1).setShot(Boolean.parseBoolean(split[3]));
			getPlayersArray().get(0).setShot(Boolean.parseBoolean(split[4]));
			getPlayersArray().get(1).setShot(Boolean.parseBoolean(split[5]));
		}
	}

	// DATA TO STRING
	public String statusToString() {
		StringBuilder stringBuilder = new StringBuilder();

		// QUI MANDO VARIABILI DI SISTEMA, COME PER ESEMPIO SIZE DEGLI ENEMY ECC
		stringBuilder.append(getEnemy().size() +":"+exit+";");
		stringBuilder.append("#");

		for (int a = 0; a < getMatrix().getRow(); a++) {
			for (int b = 0; b < getMatrix().getColumn(); b++) {
				if (matrix.world[a][b] == null) {
					stringBuilder.append("null" + ":");
				} else {
					stringBuilder.append(matrix.world[a][b].toString() + ":");
				}
			}
			stringBuilder.append(";");
		}
		stringBuilder.append("#");
		
		for (int a = 0; a < getMatrix().getRow(); a++) {
			for (int b = 0; b < getMatrix().getColumn(); b++) {
				if (matrix.objectStatic[a][b] == null) {
					stringBuilder.append("null" + ":");
				} else {
					stringBuilder.append(matrix.objectStatic[a][b].toString() + ":");
				}
			}
			stringBuilder.append(";");
		}
		stringBuilder.append("#");

		for (int a = 0; a < getPlayersArray().size(); a++) {
			stringBuilder.append(build(getPlayersArray().get(a)));
		}
		stringBuilder.append("#");

		if(getEnemy().isEmpty()){
			stringBuilder.append(" ");
		}else{
			for (int a = 0; a < getEnemy().size(); a++) {
				stringBuilder.append(build(getEnemy().get(a)));
			}
		}		
		stringBuilder.append("#");

		if (getRocket().isEmpty()) {
			stringBuilder.append(" ");
		} else {
			for (int a = 0; a < getRocket().size(); a++) {
				stringBuilder.append(build(getRocket().get(a)));
			}
		}
		stringBuilder.append("#");

		if (getPower().isEmpty()) {
			stringBuilder.append(" ");
		} else {
			for (int a = 0; a < getPower().size(); a++) {
				stringBuilder.append(build(getPower().get(a)));
			}
		}
		stringBuilder.append("#");

		if (getEffects().isEmpty()) {
			stringBuilder.append(" ");
		} else {
			for (int a = 0; a < getEffects().size(); a++) {
				stringBuilder.append(build(getEffects().get(a)));
			}
		}
		stringBuilder.append("#");
		stringBuilder.append(build(flag));
		stringBuilder.append("#");
		stringBuilder.append(sounds());

		return stringBuilder.toString();
	}

	private Object sounds() {
		return isSoundPowerUp()+":"+isExplosion()+":"+getPlayersArray().get(0).isShot()+":"+getPlayersArray().get(1).isShot()
				+":"+getPlayersArray().get(0).isPressed()+":"+getPlayersArray().get(1).isPressed()+";";
	}

	private String build(AbstractStaticObject ob) {

		if (ob instanceof PlayerTank) {
			PlayerTank p = ((PlayerTank) ob);
			return (p.toString() + ":" + p.getxGraphics() + ":" + p.getyGraphics() + ":" + p.getTmpDirection() + ":"
					+ p.getKeyPressedMillis() + ":" + p.isPressed() + ":" + p.isProtection() + ":" + p.isReadyToSpawn()
					+ ":" + p.getCountdown() + ":" + p.getResume() +":"+p.getInc()+":"+p.isExitOnline()+":"+p.getNameOfPlayerTank()+":"+p.isDied()+";");
		} else if (ob instanceof EnemyTank) {
			EnemyTank e = ((EnemyTank) ob);
			return ("ENEMY" + ":" + e.toString() + ":" + e.getX() + ":" + e.getY() + ":" + e.getxGraphics() + ":"
					+ e.getyGraphics() + ":" + e.getTmpDirection() + ":" + e.isAppearsInTheMap() + ":"
					+ e.isReadyToSpawn() + ":" + e.getInc() + ":" + e.isProtection() + ":" + e.getCountdown() + ";");
		} else if (ob instanceof Rocket) {
			Rocket r = ((Rocket) ob);
			return (r.toString() + ":" + r.getX() + ":" + r.getY() + ":" + r.getDirection() + ":" + r.getxGraphics()
					+ ":" + r.getyGraphics() + ":" + r.isFirstAnimationNo() + ":" + r.getInc() + ";");
		} else if (ob instanceof PowerUp) {
			PowerUp pu = (PowerUp) ob;
			AbstractStaticObject getbef = pu.getBefore();
			AbstractStaticObject getbefWa = pu.getBeforeWater();
			AbstractStaticObject getTan = pu.getTank();
			String s1, s2, s3;

			if (getbef == null) {
				s1 = "null";
			} else {
				s1 = getbef.toString();
			}
			if (getbefWa == null) {
				s2 = "null";
			} else {
				s2 = getbefWa.toString();
			}
			if (getTan == null) {
				s3 = "null";
			} else {
				s3 = pu.getTank().toString();
			}

			return ("POWERUP" + ":" + pu.toString() + ":" + pu.getX() + ":" + pu.getY() + ":" + pu.isBlink() + ":" + s1
					+ ":" + s2 + ":" + pu.getDropDirection() + ":" + pu.getInc() + ":" + pu.isActivate() + ":" + s3
					+ ":" + pu.getTime() + ";");
		} else if (ob instanceof Flag) {
			Flag f = (Flag) ob;
			return (f.toString() + ":" + f.getX() + ":" + f.getY() + ":" + f.getxGraphics() + ":" + f.getyGraphics()
					+ ":" + f.getInc() + ":"+f.isHit()+";");
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

	public int getNumbersOfEnemiesOnline() {
		return numbersOfEnemiesOnline;
	}

	public void setNumbersOfEnemiesOnline(int numbersOfEnemiesOnline) {
		this.numbersOfEnemiesOnline = numbersOfEnemiesOnline;
	}
	
	public void setEffects(ArrayList<AbstractStaticObject> effects) {
		this.effects = effects;
	}

	public boolean isShotEnabled() {
		return shotEnabled;
	}

	public void setShotEnabled(boolean shotEnabled) {
		this.shotEnabled = shotEnabled;
	}

	public ArrayList<AbstractStaticObject> getEffects() {
		return effects;
	}

	public boolean isMedium() {
		return medium;
	}

	public void setMedium(boolean medium) {
		this.medium = medium;
	}

}

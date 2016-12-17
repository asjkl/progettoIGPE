package progettoIGPE.davide.giovanni.unical2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
//import java.util.Scanner;
import java.util.StringTokenizer;

public class GameManager {
	
	private int x;
	private int y;
	private static final int size = 20;
	private int finalScore = 0;
	private int count[];
	
	private int numberOfEnemyToSpawn = 4; 
	private int numberOfEnemyOnMap = 0; 
	
	private Random random;
	private World matrix;
	private PlayerTank player;
	private ArrayList<EnemyTank> enemy;
	private ArrayList<PowerUp> power;
	private ArrayList<Rocket> rocket;
	private Flag flag;
	private ArrayList<AbstractStaticObject> recoveryWall;
	private long currentTime;
	private boolean updateAll = true;
	private int durationPowerUp = 20;
	private int numEnemyDropsPowerUp = 1;
	/*se powerUp è stato preso e non ti sei mai mai mosso dalla cella in cui l hai preso*/
	private boolean first=false;  
	//usato in addPowerUp
	private int xTmp = -1; 
	private int yTmp = -1; 
	private Direction dir; 
	
	public GameManager() {
		
		matrix = new World(size, size);
		enemy = new ArrayList<>();
		rocket = new ArrayList<>();
		power = new ArrayList<>();
		random = new Random();
		recoveryWall = new ArrayList<>();
		count = new int[4];

		for (int i = 0; i < count.length; i++) //conta occorrenze enemies?
			count[i] = 0;
		importMap();
	}

	public void importMap() {
		int i = 0;// indice di riga
		try {
			BufferedReader reader = new BufferedReader(new FileReader("maps/map01.txt"));
			String line = reader.readLine();
			while (i < size) {

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
			
			importEnemies(reader, line);
			reader.close();
			
		} // try
		catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void importEnemies(BufferedReader reader, String line){
				
			while (line != null) {
				StringTokenizer st = new StringTokenizer(line, " ");
				
				String typology = null;
				String number = null;
				
				if(st.hasMoreTokens())
					typology = st.nextToken();
				if(st.hasMoreTokens())
					number = st.nextToken();
				
				if(typology!=null && number!=null)
					addEnemies(typology,Integer.parseInt(number));
					
				try {
					line = reader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} // while
	
	}
	
	public void verifyPlayerPosition(PlayerTank p){
		//controllo se player si è mosso ( serve per PowerUp)
		if(p.getX() != x || p.getY() != y)
			setFirst(false);
	}
	
	// ----------------------------------------POWERUP-------------------------------------

	public void isDroppedOnTheMap(){
		
		for (int a = 0; a < power.size(); a++){		
			if (power.get(a).isDrop() && !power.get(a).isActivate()) {
				
				long tmp = (power.get(a).getDropTime() + getDurationPowerUp()) % 60;
				
//				System.out.println("tmpDrop: " + tmp);
//				System.out.println("getTimer"+power.get(a).getDropTime());

				if(tmp == currentTime) { //countdown durata prima di sparire
					power.get(a).setDrop(false);
					
					if(power.get(a).getBefore() instanceof Water){
						getMatrix().world[power.get(a).getX()][power.get(a).getY()] = null;
						getMatrix().world [((Water)power.get(a).getBefore()).getX()]
								          [((Water)power.get(a).getBefore()).getY()] = power.get(a).getBefore();
						
					}
					else{
						getMatrix().world[power.get(a).getX()][power.get(a).getY()] = power.get(a).getBefore();
						if(power.get(a).getBefore() instanceof BrickWall)
						((BrickWall)power.get(a).getBefore()).setBefore(null); //se PowerUp scaduto deve essere cancellato
					}
					power.remove(a);
					a--;
				}
			}
		}
	}
	
	public void timeOut() {

		for (int a = 0; a < power.size(); a++){			
			if (power.get(a).isActivate()) { 
				System.out.println(power.get(a) + "---------- attivo!");
				
				long tmp = (power.get(a).getTimer() + power.get(a).getDuration()) % 60;

				System.out.println("tmpTimeOut: " + tmp);
				System.out.println("getTimer"+power.get(a).getTimer());

				if (tmp == currentTime) {
					System.out.println(power.get(a) + "---------- disattivo!");
					managePowerUp(power.get(a));
					power.get(a).setActivate(false);
					power.remove(a);
					a--;
				}
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
			player.setLevel(player.getLevel() - 1);
			break;
		case TIMER:
			updateAll = true;
			break;
		default:
			break;
		}
	}

	private void extendAddPowerUp(PowerUp tmp){
		
		tmp.setDropTime(currentTime);
		tmp.setBefore(getMatrix().world[getX()][getY()]); //prima di spostare powerUp mi salvo l oggetto su cui è
														  //caduto precedentemente.
		if(tmp.getBefore() instanceof Water){
			tmp.setX(xTmp); //powerUp viene spostato dall acqua alla cella accanto (pos buona )
			tmp.setY(yTmp);	
		}
		tmp.setDropDirection(dir);
		power.add(tmp);
		if(getMatrix().world[getX()][getY()] instanceof BrickWall)
			((BrickWall)getMatrix().world[getX()][getY()]).setBefore(tmp);
		else
			getMatrix().world[tmp.getX()][tmp.getY()] = tmp; //attenzione al tmp.getX();
	}
	
	public void addPowerUp(int t) {
		PowerUp tmp = null;
		foundPosition();
		switch (t) {
		case 0:
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.GRENADE, 0, true);
			extendAddPowerUp(tmp);
			break;
		case 1:
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.HELMET, 12, true);
			extendAddPowerUp(tmp);
			break;
		case 2:
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.SHOVEL, 15, true);
			extendAddPowerUp(tmp);
			break;
		case 3:
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.STAR, 0, true);
			extendAddPowerUp(tmp);
			break;
		case 4:
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.TANK, 0, true);
			extendAddPowerUp(tmp);
			break;
		case 5:
			tmp = new PowerUp(getX(), getY(), getMatrix(), Power.TIMER, 10, true);
			extendAddPowerUp(tmp);
			break;
		default:
			break;
		}
	}

	private boolean movePowerUpInCorrectPosition(){
		
		//CONTROLLO POS DEL POWERUP SE E' BUONA
	
		if(x-1 >= 0 && !(getMatrix().world[x-1][y] instanceof Water) 
			&& !(getMatrix().world[x-1][y] instanceof EnemyTank)
			&& !(getMatrix().world[x-1][y] instanceof PlayerTank)){          //UP 
			xTmp=x-1; //necessito sapere le coordinate della nuova pos. buona su cui verrà spostato in seguito
			yTmp=y;	  //il powerUp, qui non è possibile farlo perke prima mi devo creare il powerUp e il before e poi...
			dir=Direction.UP;
			return true;
		}
		if(x+1 < getSize() && !(getMatrix().world[x+1][y] instanceof Water)
			&& !(getMatrix().world[x+1][y] instanceof EnemyTank)
			&& !(getMatrix().world[x+1][y] instanceof PlayerTank)){           //DOWN
			xTmp=x+1;
			yTmp=y;
			dir=Direction.DOWN;
			return true;
		}
		if(y-1 >= 0 && !(getMatrix().world[x][y-1] instanceof Water)
			&& !(getMatrix().world[x][y-1] instanceof EnemyTank)
			&& !(getMatrix().world[x][y-1] instanceof PlayerTank) ){          //LEFT
			xTmp=x;
			yTmp=y-1;
			dir=Direction.LEFT;
			return true;
		}
		if(y+1 < getSize() && !(getMatrix().world[x][y+1] instanceof Water)
			&& !(getMatrix().world[x][y+1] instanceof EnemyTank)
			&& !(getMatrix().world[x][y+1] instanceof PlayerTank) ){          //RIGHT
			xTmp=x;
			yTmp=y+1;
			dir=Direction.RIGHT;
			return true;
		}
		return false;
	}
	
	public void foundPosition() {
		boolean flag = false;

		while (!flag) {
			x = random.nextInt(size);
			y = random.nextInt(size);

			if (!(getMatrix().world[x][y] instanceof SteelWall) && !(getMatrix().world[x][y] instanceof PlayerTank)
					&& !(getMatrix().world[x][y] instanceof EnemyTank) && !(getMatrix().world[x][y] instanceof PowerUp)
					&& !(getMatrix().world[x][y] instanceof Rocket) && !(getMatrix().world[x][y] instanceof Flag)){		
				flag = true;
			}
			if(getMatrix().world[x][y] instanceof Water) //se cade nell'acqua controlla
				if(!movePowerUpInCorrectPosition()) //se la condizione non è soddisfatta
					flag=false; //continua a ciclare
		}
	}

	public void usePowerUp(PowerUp power) {

		switch (power.getPowerUp()) {
		case GRENADE:
			for (int i = 0; i < enemy.size(); i++)
				if (enemy.get(i).isAppearsInTheMap()) {
					matrix.world[enemy.get(i).getX()][enemy.get(i).getY()] = enemy.get(i).getCurr();
					// enemy.get(i).setAppearsInTheMap(false);
					enemy.remove(i);
					i--;
				}
			numberOfEnemyOnMap = 0;
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
			break;
		case STAR:
			if (player.getLevel() < 3){
				player.setLevel(player.getLevel() + 1);
			System.out.println("-----------------------------------------");
			}
			break;
		case TANK:
			player.setResume(player.getResume() + 1);
			break;
		case TIMER:
			updateAll = false;
			break;
		}
	}

	// ---------------------------------------ROCKET----------------------------------------

	public void updateRocket(int a) {
		Rocket r = null; // rocket temporaneo
			rocket.get(a).update();
			rocket.get(a).setUpdateRocket(false);
			
			if (destroyRocket(rocket.get(a))) {
				countRockets(rocket.get(a));

				// aggiorna rocket se curr non è un tank mettendo curr del rocket
				if (!(rocket.get(a).getCurr() instanceof PlayerTank) && !(rocket.get(a).getCurr() instanceof EnemyTank))
					matrix.world[rocket.get(a).getX()][rocket.get(a).getY()] = rocket.get(a).getCurr();
				// altrimenti metti curr del tank
				else
					matrix.world[rocket.get(a).getX()][rocket.get(a).getY()] = rocket.get(a).getTank().getCurr();
				

				// distruggi enemy
				if (rocket.get(a).getNext() instanceof EnemyTank && rocket.get(a).getTank() instanceof PlayerTank)
					if (((EnemyTank) rocket.get(a).getNext()).getHealth() == 0) {
						switchCurrTank(((EnemyTank) rocket.get(a).getNext()));
						if (((EnemyTank) rocket.get(a).getNext()).isPowerUpOn())
//							addPowerUp(new Random().nextInt(6)); // PRIMA DI MORIRE GENERA UN POWERUP
							//TODO
							addPowerUp(3); 
						destroyEnemyTank((EnemyTank) rocket.get(a).getNext());
					}

				// distruggi player
				if (rocket.get(a).getNext() instanceof PlayerTank && rocket.get(a).getTank() instanceof EnemyTank) {
					if (player.isProtection() == false) { // se non è protetto
						// quando ucciso, rimette il corrente nella posizione x,
						// e quando rinasce il corrente sarà null
						switchCurrTank(player);
						damageAndDestroyPlayerTank();
					}
				}

				if (rocket.get(a).getNext() instanceof Rocket)
					r = ((Rocket) rocket.get(a).getNext());
				
				rocket.remove(a);
			}
		
		if (r != null) // distruggi Rocket2
			destroyOtherRocket(r);
	}

	public void countRockets(Rocket r) {
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

		if (rocket.isBordo() || rocket.getNext() instanceof Rocket || rocket.getNext() instanceof PlayerTank){
			return true;
		}

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
				// se nemico non ha protezione
				if (((EnemyTank) rocket.getNext()).isProtection() == false)
				damageEnemyTank(rocket);
				else // caccio protezione
				((EnemyTank) rocket.getNext()).setProtection(false);
			return true;
		}

		// se Rocket tocca bordo ( è legato al primo if )
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
		else if (!(rocket.getNext() instanceof SteelWall)) // e non è SteelWall
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
		player.setDied(true);
	}

	private void destroyWall(Rocket rocket) {
		
		//se BRICKWALL allora metto il suo before che potrebbe essere un PowerUp
		//altriemtni se STEELWALL metto null
		
		switch (rocket.getDirection()) {
		case UP:
			if(matrix.world[rocket.getX() - 1][rocket.getY()] instanceof BrickWall)
				matrix.world[rocket.getX() - 1][rocket.getY()] = ((BrickWall)matrix.world[rocket.getX() - 1][rocket.getY()]).getBefore();
			else 
				matrix.world[rocket.getX() - 1][rocket.getY()] = null;
			break;
		case DOWN:
			if(matrix.world[rocket.getX() + 1][rocket.getY()] instanceof BrickWall)
				matrix.world[rocket.getX() + 1][rocket.getY()] = ((BrickWall)matrix.world[rocket.getX() + 1][rocket.getY()]).getBefore();
			else 
				matrix.world[rocket.getX() + 1][rocket.getY()] = null;
			break;
		case RIGHT:
			if(matrix.world[rocket.getX()][rocket.getY() + 1] instanceof BrickWall)
				matrix.world[rocket.getX()][rocket.getY() + 1] = ((BrickWall)matrix.world[rocket.getX()][rocket.getY() + 1]).getBefore();
			else 
				matrix.world[rocket.getX()][rocket.getY() + 1] = null;
			break;
		case LEFT:
			if(matrix.world[rocket.getX()][rocket.getY() - 1] instanceof BrickWall)
				matrix.world[rocket.getX()][rocket.getY() - 1] = ((BrickWall)matrix.world[rocket.getX()][rocket.getY() - 1]).getBefore();
			else
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
				finalScore += enemyT.getPoint();
				increaseCount(enemyT);
				enemy.get(i).setAppearsInTheMap(false);
				enemy.get(i).setDestroy(true);
				numberOfEnemyOnMap--;
				enemy.remove(i);
				break;
			}
	}

	public void increaseCount(EnemyTank e) {
		if (e instanceof BasicTank) {
			count[0]++;
		} else if (e instanceof FastTank) {
			count[1]++;
		} else if (e instanceof PowerTank) {
			count[2]++;
		} else if (e instanceof ArmorTank) {
			count[3]++;
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

	public void addEnemies(String T, int N){
		
		int []pos = {0, size/2-1, size-1}; //possibili pos per far spawnare enemies 
		int c=0;
		int saveLastPosition=0;
		
		while(c < N){
			chooseEnemy(T,pos[saveLastPosition%3]);
			saveLastPosition++;
			c++;
		}
	}

	private void chooseEnemy(String typology, int y) {
		switch (typology) {
		case "basic":
			enemy.add(new BasicTank(0, y, matrix, Direction.STOP));
			break;
		case "fast":
			enemy.add(new FastTank(0, y, matrix, Direction.STOP));
			break;
		case "power":
			enemy.add(new PowerTank(0, y, matrix, Direction.STOP));
			break;
		case "armor":
			enemy.add(new ArmorTank(0, y, matrix, Direction.STOP));
			break;
		}
		if ((enemy.size() % numEnemyDropsPowerUp) == 0) {
			enemy.get(enemy.size() - 1).setPowerUpOn(true);
		}
	}

	public void spawnEnemy() {

		int count=0;
		while(numberOfEnemyOnMap < numberOfEnemyToSpawn){
			if(!enemy.get(count).isAppearsInTheMap() && !enemy.get(count).isDestroy()){
				enemy.get(count).setAppearsInTheMap(true);
				numberOfEnemyOnMap++;
			}
			count++;
		}
	}

	
	//TODO da rivedere
	public void enemyPositionRandom(int a) {
			
		if (updateAll) {
			if (enemy.get(a).isAppearsInTheMap()) {
				if (enemy.get(a).getCountStep() == 0 || enemy.get(a).isRecoverValue()) {
					
					enemy.get(a).setPositionDirection();
					enemy.get(a).setCountStep(0);
					enemy.get(a).setStep(0);
					enemy.get(a).setNoUpdateG(false);
					do {
						enemy.get(a).directionEnemyRandom();
					} while (!enemy.get(a).positionCorrect() && !enemy.get(a).notSamePosition()
							&& !enemy.get(a).allTrue());
				
					int tempCont;
					do{
					tempCont = random.nextInt(size);
					}while(tempCont==matrix.getColumn());
					enemy.get(a).setStep(tempCont);
				}
				if (!(enemy.get(a).getNext() instanceof EnemyTank)&& updateAll == true);
					createRocketTank(enemy.get(a).getDirection(), enemy.get(a));
			}
		}

	}

	public void enemyUpdate(int a) {

		// AGGIORNA NEMICI SOLO SE NON E' STATO PRESO POWERUP TIMER
		if (updateAll) {

				if (enemy.get(a).isAppearsInTheMap()) {
					if (enemy.get(a).getStep() >= enemy.get(a).getCountStep()) {
						enemy.get(a).update();
					
						if (enemy.get(a).getX() == enemy.get(a).getTempX() && enemy.get(a).getY() == enemy.get(a).getTempY()) {
							enemy.get(a).setRecoverValue(true);
						} else {
							enemy.get(a).setPositionXY();
							matrix.world[enemy.get(a).getX()][enemy.get(a).getY()] = enemy.get(a);
							enemy.get(a).setCountStep(enemy.get(a).getCountStep() + 1);
							if (!enemy.get(a).positionCorrect()) 
								enemy.get(a).setRecoverValue(true);
							else 
								enemy.get(a).setRecoverValue(false);
						}
					} else {
						enemy.get(a).setCountStep(0);
						enemy.get(a).setNoUpdateG(true);
						matrix.world[enemy.get(a).getX()][enemy.get(a).getY()] = enemy.get(a);
					}
				}
			
		} // POWERUP

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

	public int getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(int finalScore) {
		this.finalScore = finalScore;
	}

	public int[] getCount() {
		return count;
	}

	public void setCount(int[] count) {
		this.count = count;
	}

	public ArrayList<AbstractStaticObject> getRecoveryWall() {
		return recoveryWall;
	}

	public void setRecoveryWall(ArrayList<AbstractStaticObject> recoveryWall) {
		this.recoveryWall = recoveryWall;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public boolean isUpdateAll() {
		return updateAll;
	}

	public void setUpdateAll(boolean updateAll) {
		this.updateAll = updateAll;
	}

	public int getDurationPowerUp() {
		return durationPowerUp;
	}

	public void setDurationPowerUp(int durationPowerUp) {
		this.durationPowerUp = durationPowerUp;
	}
	
	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
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

}

package progettoIGPE.davide.giovanni.unical2016;

import java.util.ArrayList;
import java.util.BitSet;

public class PlayerTank extends Tank {

	private int level;
	private int resume;
	private String id;
	private int point;
	private boolean died;
	private boolean first;
	private int bornX;
	private int bornY;
	private boolean shot;
	private long keyPressedMillis;
	private long keyPressLength;
	private ArrayList<Integer> keys;
	private boolean pressed;
	private boolean releaseKeyRocket;
	private boolean enter;
	private Statistics statistics;
	private ArrayList<Integer> defaultKeysPlayer;
	private boolean finish;
	private BitSet keyBits;
	private boolean exitOnline;
	private String nameOfPlayerTank;
	private int currentResume;
	private int currentLevel;
	
	public PlayerTank(int x, int y, World world, String id) {
		super(x, y, world, Speed.NORMAL, Speed.NORMALROCKET, Direction.STOP, 1);
		this.resume = 3;
		this.point = 0;
		this.keyPressedMillis=0;
		this.keyPressLength=0;
		this.level = 0;
		this.died = false;
		this.setStatistics(new Statistics());
		this.setReadyToSpawn(true);
		this.setCountdown(0);
		this.setInc(0);
		this.setRotateDegrees(0);
		this.setTmpDirection(Direction.UP);
		this.first=true;
		this.setBornX(x);
		this.setBornY(y);
		setShot(false);
		this.setKeys(new ArrayList<>());
		this.setPressed(false);
		this.setReleaseKeyRocket(false);
		this.setEnter(false);
		this.id=id;
		this.setFinish(false);
		defaultKeys();
		this.setKeyBits(new BitSet(128));
	} 

	public boolean isExitOnline() {
		return exitOnline;
	}

	public void setExitOnline(boolean exitOnline) {
		this.exitOnline = exitOnline;
	}

	@Override
	public void update() {
		super.update();
		getWorld().getWorld()[getX()][getY()] = this;
		setDirection(Direction.STOP); 
	}

	@Override
	public boolean sameObject() {
		
		// DA CONTROLLARE CAMBIATO THIS PRIMA ERA PLAYERTANK
		if(next instanceof Rocket && ((Rocket)next).getTank() == this ){
			next = ((Rocket)next).getCurr();
		}
		
		if(next instanceof Rocket){
			curr = ((Rocket)next).getCurr();
			return true;
		}
		if (!(next instanceof Wall) && !(next instanceof Tank) && !(next instanceof Water) && !(next instanceof Rocket) && !(next instanceof Flag)) {

			if (next instanceof PowerUp ){
				if (!(((PowerUp)next).getBefore() instanceof Tree) && !(((PowerUp)next).getBefore() instanceof Ice) && !(((PowerUp)next).getBefore() instanceof Water)) 
					curr = null;
				else if(((PowerUp)next).getBefore() instanceof Water)
					curr =((PowerUp)next).getBeforeWater();
				else if((((PowerUp)next).getBefore() instanceof Tree || ((PowerUp)next).getBefore() instanceof Ice) )
					curr = ((PowerUp)next).getBefore();
			}
			else
				curr = next;
			
				return true;
		}
		return false;
	}
	
	@SuppressWarnings("serial")
	private void defaultKeys(){
		if(GameManager.offline){
			if(id.equals("P1") || (id.equals("P2") && GameManager.singlePlayer ))  {
				this.setDefaultKeysPlayer(new ArrayList<Integer>() {
					{
						add(37);
						add(38);
						add(39);
						add(40);
						add(32);
					}
				});
			}else if(id.equals("P2")){
				this.setDefaultKeysPlayer(new ArrayList<Integer>() {
					{
						add(87);
						add(83);
						add(68);
						add(65);
						add(17);
		
					}
				});
			}}
			else{
				this.setDefaultKeysPlayer(new ArrayList<Integer>() {
					{
						add(37);
						add(38);
						add(39);
						add(40);
						add(32);
						add(10);
						add(27);
					}
				});
			}
	}
	
	@Override
	public Direction getDirection() {
		return super.getDirection();
	}

	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getResume() {
		return resume;
	}

	public void setResume(int resume) {
		this.resume = resume;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	@Override
	public String toString() {
		return id;
	}

	public boolean isDied() {
		return died;
	}

	public void setDied(boolean died) {
		this.died = died;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public int getBornX() {
		return bornX;
	}

	public void setBornX(int bornX) {
		this.bornX = bornX;
	}

	public int getBornY() {
		return bornY;
	}

	public void setBornY(int bornY) {
		this.bornY = bornY;
	}

	public boolean isShot() {
		return shot;
	}

	public void setShot(boolean shot) {
		this.shot = shot;
	}

	public long getKeyPressedMillis() {
		return keyPressedMillis;
	}

	public void setKeyPressedMillis(long keyPressedMillis) {
		this.keyPressedMillis = keyPressedMillis;
	}

	public long getKeyPressLength() {
		return keyPressLength;
	}

	public void setKeyPressLength(long keyPressLength) {
		this.keyPressLength = keyPressLength;
	}

	public ArrayList<Integer> getKeys() {
		return keys;
	}

	public void setKeys(ArrayList<Integer> keys) {
		this.keys = keys;
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public boolean isReleaseKeyRocket() {
		return releaseKeyRocket;
	}

	public void setReleaseKeyRocket(boolean releaseKeyRocket) {
		this.releaseKeyRocket = releaseKeyRocket;
	}

	public boolean isEnter() {
		return enter;
	}

	public void setEnter(boolean enter) {
		this.enter = enter;
	}

	public Statistics getStatistics() {
		return statistics;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}

	public ArrayList<Integer> getDefaultKeysPlayer() {
		return defaultKeysPlayer;
	}

	public void setDefaultKeysPlayer(ArrayList<Integer> defaultKeysPlayer) {
		this.defaultKeysPlayer = defaultKeysPlayer;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public String getNameOfPlayerTank() {
		return nameOfPlayerTank;
	}

	public void setNameOfPlayerTank(String nameOfPlayerTank) {
		this.nameOfPlayerTank = nameOfPlayerTank;
	}

	public BitSet getKeyBits() {
		return keyBits;
	}

	public void setKeyBits(BitSet keyBits) {
		this.keyBits = keyBits;
	}

	public int getCurrentResume() {
		return currentResume;
	}

	public void setCurrentResume(int currentResume) {
		this.currentResume = currentResume;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
}

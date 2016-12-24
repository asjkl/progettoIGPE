package progettoIGPE.davide.giovanni.unical2016;

public class Rocket extends AbstractDynamicObject {

	private boolean bordo; // se trovo bordo
	private AbstractDynamicObject tank; // rocket appartenenza
	private AbstractStaticObject beforeBordo;
	
	//PER GRAFICA
	private boolean firstAnimationNo;
	private boolean rocketForPlayer;	
	private boolean destroyRocketAndWall;
	
	public Rocket(int x, int y, World world, Direction direction, AbstractDynamicObject tank) {
		super(x, y, world, direction);
		this.bordo = false;
		this.tank = tank;
		curr = tank; //quando viene creato il Rocket il suo curr sarà il TANK
		beforeBordo = tank.getCurr();
		this.firstAnimationNo=true;
		setDestroyRocketAndWall(false);
		
		if(tank.getSpeedShot()==Speed.SLOW){
			this.setCont(1);
		}else if(tank.getSpeedShot()==Speed.NORMAL){
			this.setCont(getSizePixel()/2);
		}else{			
			this.setCont((getSizePixel()/2)+5);
		}
		
		if(tank instanceof PlayerTank){
			if(tank.getContRocket()<1){	
				setUpdateObject(true);
				setRocketForPlayer(true);
				}
			else{
				setUpdateObject(false);
				setRocketForPlayer(false);
			}
		}
		else{
			setUpdateObject(true);
			setRocketForPlayer(true);
		}
		
	}
	
	@Override
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;
	}
	
	@Override
	public boolean sameObject() {
		if (!(next instanceof Wall) && !(next instanceof PlayerTank) && !(next instanceof Rocket)
				&& !(next instanceof EnemyTank) && !(next instanceof Flag)) {
			curr=next;
			return true;
		}
		return false;
	}
	
	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}

	@Override
	public String toString() {
		return " -- ";
	}

	public boolean isBordo() {
		return bordo;
	}

	public void setBordo(boolean bordo) {
		this.bordo = bordo;
	}

	public AbstractDynamicObject getTank() {
		return tank;
	}

	public void setTank(AbstractDynamicObject tank) {
		this.tank = tank;
	}

	public AbstractStaticObject getBeforeBordo() {
		return beforeBordo;
	}

	public void setBeforeBordo(AbstractStaticObject beforeBordo) {
		this.beforeBordo = beforeBordo;
	}

	public boolean isFirstAnimationNo() {
		return firstAnimationNo;
	}

	public void setFirstAnimationNo(boolean firstAnimationNo) {
		this.firstAnimationNo = firstAnimationNo;
	}

	public boolean isRocketForPlayer() {
		return rocketForPlayer;
	}

	public void setRocketForPlayer(boolean rocketForPlayer) {
		this.rocketForPlayer = rocketForPlayer;
	}


	public boolean isDestroyRocketAndWall() {
		return destroyRocketAndWall;
	}


	public void setDestroyRocketAndWall(boolean destroyRocketAndWall) {
		this.destroyRocketAndWall = destroyRocketAndWall;
	}
}
package progettoIGPE.davide.giovanni.unical2016;

public class Rocket extends AbstractDynamicObject {

	private AbstractDynamicObject tank;
	private boolean firstAnimationNo;
	private boolean rocketForPlayer;	 

	public Rocket(int x, int y, World world, Direction direction, AbstractDynamicObject tank) {		
		super(x, y, world, direction);
		this.tank = tank;
		this.curr = tank; //quando viene creato il Rocket il suo curr sar� il TANK	
		this.firstAnimationNo = true;
		rocketForPlayer(); //spostato in una funzione
	}
	
	@Override
	public void update() {
		super.update();
				
		if (curr != tank && canGo)
			getWorld().world[getX()][getY()] = this;
	}
	
	@Override
	public boolean sameObject() {

		if(next instanceof Tank && (Tank)next == getTank()){
			next = ((Tank)next).getCurr();
			return true;
		}
			
		if(firstAnimationNo && next == tank){
			return true;							
		}
		
		if (!(next instanceof Wall) && !(next instanceof Tank) && !(next instanceof Rocket)
				 && !(next instanceof Flag)) {
			curr=next;
			return true;
		}
		return false;
	}
	
	public void updateRect() {
		if (this.getDirection() == Direction.UP) {
			rect.setLocation((int) (getxGraphics() + (sizePixel - 9)),  (int) getyGraphics() + ((sizePixel / 2) - 4));
		} else if (getDirection() == Direction.DOWN) {
			rect.setLocation((int) getxGraphics(), (int) getyGraphics() + ((sizePixel / 2) - 4));
		} else if (getDirection() == Direction.LEFT) {
			rect.setLocation((int) (getxGraphics() + ((sizePixel / 2) - 4)), (int) getyGraphics() + (sizePixel- 9));
		} else if (getDirection() == Direction.RIGHT) {
			rect.setLocation((int) (getxGraphics() + ((sizePixel / 2) - 4)), (int) getyGraphics());
		}	
	}
	
	void rocketForPlayer(){
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
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}

	@Override
	public String toString() {
		return " -- ";
	}

	public AbstractDynamicObject getTank() {
		return tank;
	}

	public void setTank(AbstractDynamicObject tank) {
		this.tank = tank;
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
}
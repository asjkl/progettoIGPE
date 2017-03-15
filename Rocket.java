package progettoIGPE.davide.giovanni.unical2016;

public class Rocket extends AbstractDynamicObject {

	private AbstractDynamicObject tank;
	private AbstractStaticObject beforeBorder;
	private boolean firstAnimationNo;
	private boolean rocketForPlayer;	
	private boolean finishAnimation;        

	public Rocket(int x, int y, World world, Direction direction, AbstractDynamicObject tank) {		
		super(x, y, world, direction);

		this.tank = tank;
		this.curr = tank; //quando viene creato il Rocket il suo curr sarà il TANK
		this.beforeBorder = tank.getCurr();
		this.firstAnimationNo=true;
		this.finishAnimation = false;
		
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
		
		if(next instanceof Rocket && ((Rocket) next).getTank() == this.getTank())
			return true;
		if (!(next instanceof Wall) && !(next instanceof Tank) && !(next instanceof Rocket)
				 && !(next instanceof Flag)) {
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

	public AbstractDynamicObject getTank() {
		return tank;
	}

	public void setTank(AbstractDynamicObject tank) {
		this.tank = tank;
	}

	public AbstractStaticObject getBeforeBorder() {
		return beforeBorder;
	}

	public void setBeforeBordo(AbstractStaticObject beforeBorder) {
		this.beforeBorder = beforeBorder;
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

	public boolean isFinishAnimation() {
		return finishAnimation;
	}

	public void setFinishAnimation(boolean finishAnimation) {
		this.finishAnimation = finishAnimation;
	}
}
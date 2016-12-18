package progettoIGPE.davide.giovanni.unical2016;

import java.awt.Point;

public class Rocket extends AbstractDynamicObject {

	private boolean bordo; // se trovo bordo
	private AbstractDynamicObject tank; // rocket appartenenza
	private Point pixel;
	private int cont;
	private boolean updateRocket;
	private AbstractStaticObject beforeBordo;
	
	private boolean firstAnimationNo=true;
	
	
	public Rocket(int x, int y, World world, Direction direction, AbstractDynamicObject tank) {
		super(x, y, world, direction);
		this.bordo = false;
		this.tank = tank;
		curr = tank; //quando viene creato il Rocket il suo curr sarà il TANK
		
		if(tank.getSpeedShot()==Speed.SLOW){
			this.cont=1;
		}else if(tank.getSpeedShot()==Speed.NORMAL){
			this.cont=17;
		}else{			
			this.cont=22;
		}
		this.setBeforeBordo(tank.getCurr());
		
		setPixel(new Point(x*35, y*35));
		this.cont=1;
		this.updateRocket=true;
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
	
	public int getCont() {
		return cont;
	}

	public void setCont(int cont) {
		this.cont = cont;
	}

	public boolean isUpdateRocket() {
		return updateRocket;
	}

	public void setUpdateRocket(boolean updateRocket) {
		this.updateRocket = updateRocket;
	}

	public Point getPixel() {
		return pixel;
	}

	public void setPixel(Point pixel) {
		this.pixel = pixel;
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
}
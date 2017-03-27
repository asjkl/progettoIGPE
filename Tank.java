package progettoIGPE.davide.giovanni.unical2016;

public abstract class Tank extends AbstractDynamicObject{

	//utilizzato per l effetto nel gamecomponent
	private int tmpCont;
	
	public Tank(int x, int y, World mondo, Speed speed, Speed speedShot, Direction direction,int health) {
		super(x, y, mondo, speed, speedShot, direction, health);
		setTmpCont(0);
	}
	
	@Override
	public void update() {
		super.update();
		getWorld().world[getX()][getY()] = this;
	}

	public int getTmpCont() {
		return tmpCont;
	}

	public void setTmpCont(int tmpCont) {
		this.tmpCont = tmpCont;
	}

}

package progettoIGPE.davide.giovanni.unical2016;

public class PowerUp extends AbstractStaticObject {

	private AbstractStaticObject before;
	private Power powerUp;

	public PowerUp(int x, int y, World world, Power powerUp) {
		super(x, y, world);
		this.powerUp = powerUp;
	}

	public AbstractStaticObject getBefore() {
		return before;
	}

	public void setBefore(AbstractStaticObject before) {
		this.before = before;
	}

	public Power getPowerUp() {
		return powerUp;
	}

	public void setPowerUp(Power powerUp) {
		this.powerUp = powerUp;
	}
	
	@Override
	public String toString(){
		switch(powerUp)
		{
			case GRANADE:
				return " $G ";
				break;
			case HELMET:
				return " $H ";
				break;
			case SHOVEL:
				return " $SH";
				break;
			case STAR:
				return " $ST";
				break;
			case TANK:
				return " $T ";
				break;
			case TIMER:
				return  " $TI" ;
				break;
			default:
				return null;
				break;
		}
	}

}

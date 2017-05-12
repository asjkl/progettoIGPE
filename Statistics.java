package progettoIGPE.davide.giovanni.unical2016;

public class Statistics {

	private int highScore; //record
	private int totalEnemies; //indica il numero di nemici uccisi
	
	//punteggio totale per ogni tipo di tank
	private int basicTank;
	private int powerTank;
	private int armorTank;
	private int fastTank;
	private int other; //powerUp
	
	//numero di enemy per ogni tipo
	private int basicTankOcc;
	private int powerTankOcc;
	private int armorTankOcc;
	private int fastTankOcc;
	
	public Statistics() {
		this.highScore = 0;
		this.totalEnemies = 0;
		this.basicTank = 0;
		this.powerTank = 0;
		this.armorTank = 0;
		this.fastTank = 0;
		this.basicTankOcc = 0;
		this.powerTankOcc = 0;
		this.armorTankOcc = 0;
		this.fastTankOcc = 0;
		this.other = 0;
	}

	public void calcolate(AbstractStaticObject A){
		if(A instanceof BasicTank){
			basicTank+=100;
			totalEnemies+=basicTank;
			basicTankOcc++;
		}else if(A instanceof PowerTank){
			powerTank+=300;
			totalEnemies+=powerTank;
			powerTankOcc++;
		}else if(A instanceof ArmorTank){
			armorTank+=400;
			totalEnemies+=armorTank;
			armorTankOcc++;
		}else if(A instanceof FastTank){
			fastTank+=200;
			totalEnemies+=fastTank;
			fastTankOcc++;
		}else if(A instanceof PowerUp){
			other+=500;
		}
	}
	
	public void setNewRecord() {
		
		if(totalEnemies > highScore)
			highScore = totalEnemies;
	}
	
	public int getBasicTankOcc() {
		return basicTankOcc;
	}

	public void setBasicTankOcc(int basicTankOcc) {
		this.basicTankOcc = basicTankOcc;
	}

	public int getPowerTankOcc() {
		return powerTankOcc;
	}

	public void setPowerTankOcc(int powerTankOcc) {
		this.powerTankOcc = powerTankOcc;
	}

	public int getArmorTankOcc() {
		return armorTankOcc;
	}

	public void setArmorTankOcc(int armorTankOcc) {
		this.armorTankOcc = armorTankOcc;
	}

	public int getFastTankOcc() {
		return fastTankOcc;
	}

	public void setFastTankOcc(int fastTankOcc) {
		this.fastTankOcc = fastTankOcc;
	}
	
	public int getHighScore() {
		return highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}

	public int getTotalEnemies() {
		return totalEnemies;
	}

	public void setTotalEnemies(int totalEnemies) {
		this.totalEnemies = totalEnemies;
	}
	public int getBasicTank() {
		return basicTank;
	}

	public void setBasicTank(int basicTank) {
		this.basicTank = basicTank;
	}

	public int getPowerTank() {
		return powerTank;
	}

	public void setPowerTank(int powerTank) {
		this.powerTank = powerTank;
	}

	public int getArmorTank() {
		return armorTank;
	}

	public void setArmorTank(int armorTank) {
		this.armorTank = armorTank;
	}

	public int getFastTank() {
		return fastTank;
	}

	public void setFastTank(int fastTank) {
		this.fastTank = fastTank;
	}

	public int getOther() {
		return other;
	}

	public void setOther(int other) {
		this.other = other;
	}
}

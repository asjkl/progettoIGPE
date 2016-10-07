package progettoIGPE.davide.giovanni.unical2016;

public interface DynamicObject extends StaticObject {
	Speed getSpeed();

	Speed getSpeedShot();

	Direction getDirection();

	int getHealth();

	void update(); //update
	
	void destroy();
}

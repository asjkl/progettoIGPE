package progettoIGPE.davide.giovanni.unical2016;

import java.awt.Rectangle;

public abstract class AbstractStaticObject implements StaticObject {
	private int x;
	private int y;
	private double xGraphics;
	private double yGraphics;
	public int sizePixel;
	protected World world;
	
	public Rectangle rect;
	public int differenzTank=3;
	
	public AbstractStaticObject(int x, int y, World world) {
		this.x = x;
		this.y = y;
		this.sizePixel=35;
		setxGraphics(x*sizePixel);
		setyGraphics(y*sizePixel);
		this.world = world;
		this.rect=new Rectangle(x*sizePixel, y*sizePixel, sizePixel, sizePixel);
	}

	@Override
	public int getX() {
		return x;
	}

	// eccezzione viene gestito nell' update
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getY() {
		return y;
	}

	// eccezzione viene gestito nell' update
	public void setY(int y) {
		this.y = y;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	@Override
	public String toString() {
		return "AbstractStaticObject [x=" + x + ", y=" + y + ", mondo=" + world + "]";
	}

	public double getxGraphics() {
		return xGraphics;
	}

	public void setxGraphics(double xGrafica) {
		this.xGraphics = xGrafica;
	}

	public double getyGraphics() {
		return yGraphics;
	}

	public void setyGraphics(double yGrafica) {
		this.yGraphics = yGrafica;
	}
}
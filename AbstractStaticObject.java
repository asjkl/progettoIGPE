package core;

import java.awt.Rectangle;

public abstract class AbstractStaticObject implements StaticObject {
	
	private int x;
	private int y;
	private int sizePixel;
	protected World world;
	private Rectangle rect;
	private double xGraphics;
	private double yGraphics;
	private int differenceTank;
	
	public AbstractStaticObject(int x, int y, World world) {
		this.x = x;
		this.y = y;
		this.setSizePixel(35);
		setxGraphics(x*getSizePixel());
		setyGraphics(y*getSizePixel());
		this.world = world;
		setDifferenceTank(3);
		this.setRect(new Rectangle(x*getSizePixel(), y*getSizePixel(), getSizePixel(), getSizePixel()));
	}

	@Override
	public int getX() {
		return x;
	}

	public void setX(int x) {
		// eccezzione viene gestito nell' update
		this.x = x;
	}

	@Override
	public int getY() {
		return y;
	}

	public void setY(int y) {
		// eccezzione viene gestito nell' update
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

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}

	public int getSizePixel() {
		return sizePixel;
	}

	public void setSizePixel(int sizePixel) {
		this.sizePixel = sizePixel;
	}

	public int getDifferenceTank() {
		return differenceTank;
	}

	public void setDifferenceTank(int differenceTank) {
		this.differenceTank = differenceTank;
	}
}
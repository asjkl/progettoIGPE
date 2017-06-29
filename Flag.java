package progettoIGPE.davide.giovanni.unical2016;

public class Flag extends AbstractStaticObject {
	private boolean hit;
	private int inc;

	public Flag(int x, int y, World world) {
		super(x, y, world);
		this.hit = false;
		this.setInc(0);
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	@Override
	public String toString() {
		return " && ";
	}

	public int getInc() {
		return inc;
	}

	public void setInc(int inc) {
		this.inc = inc;
	}
}

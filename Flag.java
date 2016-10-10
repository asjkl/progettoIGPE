package progettoIGPE.davide.giovanni.unical2016;

public class Flag extends AbstractStaticObject {
	private boolean hit;
	private boolean protection;

	public Flag(int x, int y, World world, boolean hit) {
		super(world.getRow(), (world.getColumn() / 2), world);
		this.hit = hit;
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public boolean isProtection() {
		return protection;
	}

	public void setProtection(boolean protection) {
		this.protection = protection;
	}

	@Override
	public String toString() {
		return " && ";
	}

}

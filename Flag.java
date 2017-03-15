package progettoIGPE.davide.giovanni.unical2016;

public class Flag extends AbstractStaticObject {
	private boolean hit;

	public Flag(int x, int y, World world) {
		super(world.getRow(), (world.getColumn() / 2), world);
		this.hit = false;
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

}

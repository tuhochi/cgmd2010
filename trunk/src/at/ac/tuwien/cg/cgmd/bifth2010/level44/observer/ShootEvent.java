package at.ac.tuwien.cg.cgmd.bifth2010.level44.observer;

public class ShootEvent implements Event {
	private int coinCount;

	public ShootEvent(int coinsLeft) {
		this.coinCount = coinsLeft;
	}
	
	public String toString() {
		return String.valueOf(coinCount);
	}
	
	public int getCoinCount() {
		return coinCount;
	}
}

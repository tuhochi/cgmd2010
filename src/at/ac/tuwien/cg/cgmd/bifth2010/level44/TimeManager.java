package at.ac.tuwien.cg.cgmd.bifth2010.level44;

/**
 * Game duration manager
 */
public class TimeManager {
	private static final int UPDATE_INTERVAL = 500;
	public static final int DEFAULT_DURATION = 90000;
	
	private long duration;
	private long elapsed;

	private long lastUpdate;
	
	public TimeManager() {
		this.duration = DEFAULT_DURATION;
		this.elapsed = 0;
		this.lastUpdate = -1;
	}
	
	public void update() {
		long now = System.currentTimeMillis();
		
		if (now - lastUpdate > UPDATE_INTERVAL) {
			if (lastUpdate > 0) {
				elapsed += (now-lastUpdate);
			}
			lastUpdate = now;
		}
	}
	
	public void onPause() {
		if (lastUpdate > 0) {
			elapsed += (System.currentTimeMillis()-lastUpdate);
		}
		lastUpdate = -1;
	}
	
	/**
	 * @return the remaining time in milliseconds for the level
	 */
	public long getRemainingTimeMillis() {
		return Math.max(0, duration - elapsed);
	}
	
	public long getDuration() {
		return duration;
	}
	
	public String toString() {
		long millis = this.getRemainingTimeMillis();
		int remainingSeconds = (int)(millis/1000);
		return String.valueOf(remainingSeconds);
	}

	public boolean timeIsUp() {
		return getRemainingTimeMillis() == 0;
	}

	public long getElapsed() {
		return elapsed;
	}

	public void setElapsed(long elapsed) {
		this.elapsed = elapsed;
	}
	
}

package at.ac.tuwien.cg.cgmd.bifth2010.level44;

/**
 * Game duration manager
 * 
 * @author Matthias
 */
public class TimeManager {
	/** update interval of the time */
	private static final int UPDATE_INTERVAL = 500;
	/** default duration of the game in milliseconds */
	public static final int DEFAULT_DURATION = 90000;
	/** duration of the current game */
	private long duration;
	/** elapsed time */
	private long elapsed;
	/** timestamp of last update of time */
	private long lastUpdate;

	/**
	 * creates the TimeManager
	 */
	public TimeManager() {
		this.duration = DEFAULT_DURATION;
		this.elapsed = 0;
		this.lastUpdate = -1;
	}

	/**
	 * updates the elapsed time
	 */
	public void update() {
		long now = System.currentTimeMillis();

		if (now - lastUpdate > UPDATE_INTERVAL) {
			if (lastUpdate > 0) {
				elapsed += (now - lastUpdate);
			}
			lastUpdate = now;
		}
	}

	/**
	 * when the scene is paused update elapsed time depending on timestamp lastUpdate
	 */
	public void onPause() {
		if (lastUpdate > 0) {
			elapsed += (System.currentTimeMillis() - lastUpdate);
		}
		lastUpdate = -1;
	}

	/**
	 * @return the remaining time in milliseconds for the level
	 */
	public long getRemainingTimeMillis() {
		return Math.max(0, duration - elapsed);
	}

	/**
	 * @return the duration of the current game
	 */
	public long getDuration() {
		return duration;
	}

	@Override
	public String toString() {
		long millis = this.getRemainingTimeMillis();
		int remainingSeconds = (int) (millis / 1000);
		return String.valueOf(remainingSeconds);
	}

	/**
	 * @return true, if there is no time left
	 */
	public boolean timeIsUp() {
		return getRemainingTimeMillis() == 0;
	}

	/**
	 * @return the elapsed time in milliseconds
	 */
	public long getElapsed() {
		return elapsed;
	}

	/**
	 * set the elapsed time
	 * @param elapsed the new elapsed time
	 */
	public void setElapsed(long elapsed) {
		this.elapsed = elapsed;
	}

}

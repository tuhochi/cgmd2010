package at.ac.tuwien.cg.cgmd.bifth2010.level44;

/**
 * Interruptable countdown timer manager
 * 
 * This class abstracts the game countdown / duration and
 * provides ways to interrupt the timeout, restore the
 * timeout and determine if the time is already up.
 * 
 * @author Matthias Tretter
 * @author Thomas Perl
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
	 * Get the remaining milliseconds in the current game
	 * 
	 * @return the remaining time in milliseconds for the level
	 */
	public long getRemainingTimeMillis() {
		return Math.max(0, duration - elapsed);
	}

	/**
	 * Get the total duration of the game (in milliseconds)
	 * 
	 * @return the duration of the current game in milliseconds
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * String representation of the remaining time in seconds
	 * 
	 * @return A user-readable string of the remaining seconds
	 */
	@Override
	public String toString() {
		long millis = this.getRemainingTimeMillis();
		int remainingSeconds = (int) (millis / 1000);
		return String.valueOf(remainingSeconds);
	}

	/**
	 * Check if the game is already over
	 * 
	 * @return true if there is no time left, false otherwise
	 */
	public boolean timeIsUp() {
		return getRemainingTimeMillis() == 0;
	}

	/**
	 * Get the elapsed time in the current game (in milliseconds)
	 * 
	 * @return The elapsed time in milliseconds
	 */
	public long getElapsed() {
		return elapsed;
	}

	/**
	 * Set the elapsed time in the current game (in milliseconds)
	 * 
	 * @param elapsed The new elapsed time in milliseconds
	 */
	public void setElapsed(long elapsed) {
		this.elapsed = elapsed;
	}

}

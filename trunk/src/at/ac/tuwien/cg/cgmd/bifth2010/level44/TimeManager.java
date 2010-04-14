package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.observer.Subject;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.observer.TimeEvent;

/**
 * Class for managing the Time
 * 
 * @author Matthias
 *
 */
public class TimeManager extends Subject {
	/** Time for the Level */
	public static final int GAME_TIME_MILLIS = 90000; // 90 seconds
	/** Singleton-Instance */
	private static TimeManager singleton = null;
	/** timestamp since start */
	private long timestamp = -1L;
	/** temporary value for storing elapsed time when paused */
	private long timeSoFar = 0L;
	/** is the timer paused? */
	private boolean paused = true;
	/** Thread for sending Time-Events */
	private Thread updateTime = null;
	
	private TimeManager() {
		updateTime = new Thread() {
			public void run() {
				while (true) {
					TimeManager.this.notifyAll(new TimeEvent());
				
					// update time every 300ms
					try {
					Thread.sleep(300L);
					} catch(Exception ex) { }
				}
			}
		};
	}
	
	
	public static TimeManager getInstance() {
		if (singleton == null) {
			singleton = new TimeManager();
		}
		
		return singleton;
	}
	
	/**
	 * resets the time
	 */
	public void reset() {
		timeSoFar = 0L;
		paused = true;
		
		updateTime.stop();
	}
	
	/**
	 * starts the timer
	 */
	public void start() {
		paused = false;
		timestamp = System.currentTimeMillis();
		timeSoFar = 0L;
		
		updateTime.start();
		
	}
	
	/**
	 * pauses the timer
	 */
	public void pause() {
		timeSoFar += System.currentTimeMillis() - timestamp;
		paused = true;
	}
	
	/**
	 * @return the remaining time in milliseconds for the level
	 */
	public long getRemainingTimeMillis() {
		long time = timeSoFar;
		
		if (!paused) {
			time += System.currentTimeMillis() - timestamp;
		}
		
		time = GAME_TIME_MILLIS - time;
		
		return time > 0L ? time : 0L;
	}
	
	/**
	 * string-representation of the remaining time
	 */
	public String toString() {
		long millis = this.getRemainingTimeMillis();
		int remainingSeconds = (int)(millis/1000);
		
		return String.valueOf(remainingSeconds);
	}
	
	/**
	 * @return string-representation of the initial time
	 */
	public static String getFullTimeString() {
		return String.valueOf(GAME_TIME_MILLIS/1000);
	}
}

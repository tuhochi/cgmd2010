package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import android.os.Bundle;

/**
 * 
 * @author group13
 * 
 * class for managing the game time
 *
 */
public class GameTimer implements IPersistence {
	/** total game time is 2 minutes */
	private static final long TOTALGAMETIME = 2 * 60 * 1000;

	/** start time of timer */
	private long startTime;

	/** remaining game time */
	private long remainingTime;

	/** singleton object of this class */
	private static GameTimer instance;


	/**
	 * constructor initializes members
	 */
	public GameTimer() {
		this.startTime = System.currentTimeMillis();
		this.remainingTime = TOTALGAMETIME;
	}


	/**
	 * resets singleton object
	 */
	public static void reset() {
		instance = null;
	}


	/**
	 * creates/returns the singleton object of this class
	 * 
	 * @return singleton object of this class
	 */
	public static GameTimer getInstance() {
		if(instance == null) {
			instance = new GameTimer();
		}

		return instance;
	}


	/**
	 * update remaining game time
	 * called every frame
	 * 
	 */
	public void update() {
		long currentTime = System.currentTimeMillis();
		long passedTime = currentTime - startTime;
		remainingTime -= passedTime;
		startTime = currentTime;
	}


	/**
	 * get the remaining time formatted as mm:ss
	 * 
	 * @return string representation of remaining game time
	 */
	public String getRemainingTimeString() {
		//convert ms to mm:ss
		long minutes = (remainingTime / 1000) / 60;
		long seconds = (remainingTime / 1000) - minutes * 60;
		if(seconds < 10) {
			return minutes + ":0" + seconds;
		}
		else {
			return minutes + ":" + seconds;
		}
	}


	/**
	 * checks if game time is over
	 * 
	 * @return true if game time is over
	 */
	public boolean isOver() {
		if(remainingTime <= 0) {
			return true;
		}
		return false;
	}


	/**
	 * @see IPersistence#restore(Bundle)
	 */
	@Override
	public void restore(Bundle savedInstanceState) {
		//restore values
		this.remainingTime = savedInstanceState.getLong("l13_gameTimer_remainingTime");
		this.startTime = System.currentTimeMillis();
	}


	/**
	 * @see IPersistence#save(Bundle)
	 */
	@Override
	public void save(Bundle outState) {
		outState.putLong("l13_gameTimer_remainingTime", this.remainingTime);
	}	
}

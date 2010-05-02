package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import android.util.Log;

public class GameTimer {
	private static final long TOTALGAMETIME = 1 * 60 * 1000;
	private static GameTimer instance;
	
	private long startTime;
	private long remainingTime;
	
	public GameTimer() {
		this.startTime = System.currentTimeMillis();
		this.remainingTime = TOTALGAMETIME;
		
		Log.d("df", "start: " + startTime + " remainingTime:" + remainingTime);
	}
	
	public static GameTimer getInstance() {
		if(instance == null)
			instance = new GameTimer();
	
		return instance;
	}
	
	public void reset() {
		startTime = System.currentTimeMillis();
		remainingTime = TOTALGAMETIME;
	}
	
	public void update() {
		long currentTime = System.currentTimeMillis();
		long passedTime = currentTime - startTime;
		remainingTime -= passedTime;
		startTime = currentTime;
		
		Log.d("df", "passed: " + passedTime + " remaining: " + remainingTime);
	}
	
	public String getRemainingTimeString() {
		//convert ms to 1:34
		long minutes = (remainingTime / 1000) / 60;
		long seconds = (remainingTime / 1000) - minutes * 60;
		if(seconds < 10) {
			return minutes + ":0" + seconds;
		}
		else {
			return minutes + ":" + seconds;
		}
	}
	
	public boolean isOver() {
		if(remainingTime <= 0) {
			return true;
		}
		return false;
	}
	
}

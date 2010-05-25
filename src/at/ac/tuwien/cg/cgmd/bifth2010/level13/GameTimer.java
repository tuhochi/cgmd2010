package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import android.util.Log;

public class GameTimer {
	private static final long TOTALGAMETIME = 2 * 60 * 1000;
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
	
	public static void reset() {
		instance = null;
	}
	
	public void update() {
		long currentTime = System.currentTimeMillis();
		long passedTime = currentTime - startTime;
		remainingTime -= passedTime;
		startTime = currentTime;
	}
	
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
	
	public boolean isOver() {
		if(remainingTime <= 0) {
			return true;
		}
		return false;
	}

	public long getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(long remainingTime) {
		this.remainingTime = remainingTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	
	
}

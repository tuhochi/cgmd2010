package at.ac.tuwien.cg.cgmd.bifth2010.level33.tools;

import android.util.Log;

/**
 * the Class StopTimer 
 * @author roman hochstoger & christoph fuchs
 */
public class StopTimer {
	
	private long stopTime;
	private int counter =0;
	
	/**
	 * initialize a new StopTimer and set the stopTime to the current System Time
	 */
	public StopTimer() {
		reset();
	}
	
	/**
	 * log the time delta-Time == difference from (init or reset) to the actual System Time
	 * @param name
	 */
	public void logTime(String name){
		counter++;
		float deltaTime = (System.nanoTime()-stopTime) / 1000000000.0f;
		Log.d("TIMER: "+name, String.valueOf(deltaTime));
		reset();
	}

	/**
	 * set the stopTime to the actual System Time
	 */
	public void reset() {
		stopTime = System.nanoTime();
	}
	
}

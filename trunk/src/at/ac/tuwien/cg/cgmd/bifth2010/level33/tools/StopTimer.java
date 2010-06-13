package at.ac.tuwien.cg.cgmd.bifth2010.level33.tools;

import android.util.Log;

/**
 * the Class StopTimer 
 * @author roman hochstoger & christoph fuchs
 */
public class StopTimer {
	
	private long stopTime;
	private int counter =0;
	
	public StopTimer() {
		stopTime = System.nanoTime();
	}
	
	public void logTime(String name){
		counter++;
		float deltaTime = (System.nanoTime()-stopTime) / 1000000000.0f;
		Log.d("TIMER: "+name, String.valueOf(deltaTime));

		reset();
			
	}

	public void reset() {
		stopTime = System.nanoTime();
	}
	
}

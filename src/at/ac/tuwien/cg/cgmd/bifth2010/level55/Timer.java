package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import android.util.Log;

public class Timer {
	static long lastTime=0;
	static long dT=0;
	
	static int debug_count=0;
	static long debug_accdT=0;
	static long debug_refreshrate=60;	// alle 60 frames fps ausgeben
	
	static public void update() {
		long currentTime = System.currentTimeMillis();
		
		if (lastTime!=0) {
			dT=(currentTime-lastTime);
			debug();
		}
		
		lastTime=currentTime;
	}
	
	static void debug() {
		debug_count++;
		debug_accdT+=dT;
		if (debug_count>=debug_refreshrate) {
			//Log.d("fps",Long.toString(1000/(debug_accdT/debug_count)));
			Log.d("ms",Long.toString((debug_accdT/debug_count)));
			debug_count=0;
			debug_accdT=0;
		}
	}
}

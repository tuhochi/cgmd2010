package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import android.util.Log;

public class Timing {

    private static final String LOG_TAG = Timing.class.getSimpleName();
	private float startTime, currTime, currFPS, lastFrameTime, deltaFrameTime, pauseTimeStamp, pausedTime;
	
	public Timing() {
		startTime = System.nanoTime()/1000000000.0f;
		currTime = 0.0f;
		lastFrameTime = 0.0f;
		deltaFrameTime = 0.0f;
		currFPS = 0.0f;
		pauseTimeStamp = 0.0f;
		pausedTime = 0.0f;
	}
	
	public void update() {
		lastFrameTime = currTime;
		currTime = (System.nanoTime()/1000000000.0f) - startTime - pausedTime;
		deltaFrameTime = currTime - lastFrameTime;
		currFPS = (Math.round((1.0f/deltaFrameTime)*100.0f))/100.0f;

		//Log.i(LOG_TAG, "          "+Float.toString(currFPS));

		
	}
	
	public float getCurrTime() {
		
		return currTime;
	}
	
	public float getFPS() {
		
		return currFPS;
	}
	
	public void pause(){
		pauseTimeStamp = (System.nanoTime()/1000000000.0f);
	}
	public void resume(){
		pausedTime += (System.nanoTime()/1000000000.0f)-pauseTimeStamp;
	}
	
}

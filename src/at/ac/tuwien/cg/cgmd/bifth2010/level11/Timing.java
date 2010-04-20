package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import android.util.Log;

/**
 * class for gametime
 */
public class Timing {

    private static final String LOG_TAG = Timing.class.getSimpleName();
	private float startTime, currTime, currFPS, lastFrameTime, deltaFrameTime, pauseTimeStamp, pausedTime;
	/**
	 * time is set to 0 and already starts timer
	 */
	public Timing() {
		startTime = System.nanoTime()/1000000000.0f;
		currTime = 0.0f;
		lastFrameTime = 0.0f;
		deltaFrameTime = 0.0f;
		currFPS = 0.0f;
		pauseTimeStamp = 0.0f;
		pausedTime = 0.0f;
	}
	/**
	 * refreshes time
	 */
	public void update() {
		lastFrameTime = currTime;
		currTime = (System.nanoTime()/1000000000.0f) - startTime - pausedTime;
		deltaFrameTime = currTime - lastFrameTime;
		currFPS = (Math.round((1.0f/deltaFrameTime)*100.0f))/100.0f;

		//Log.i(LOG_TAG, "          "+Float.toString(currFPS));

		
	}
	/**
	 * returns the current time. call first update() to refresh time
	 * @return time in seconds
	 */
	public float getCurrTime() {
		
		return currTime;
	}
	/**
	 * fps
	 * @return fps
	 */
	public float getFPS() {
		
		return currFPS;
	}
	/**
	 * pauses timer
	 */
	public void pause(){
		pauseTimeStamp = (System.nanoTime()/1000000000.0f);
	}
	/**
	 * resumes timer. pause() must be called previously
	 */
	public void resume(){
		pausedTime += (System.nanoTime()/1000000000.0f)-pauseTimeStamp;
	}
	
}

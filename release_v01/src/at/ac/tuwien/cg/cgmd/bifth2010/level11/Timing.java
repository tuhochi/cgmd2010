package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import android.util.Log;

/**
 * class for game time
 * @author g11
 */
public class Timing {

    private static final String LOG_TAG = Timing.class.getSimpleName();
    /**
     * variables, to perform timing, starting , pausing,
     *  resuming and temporarily saving passed time and FPS
     */
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
	public void start(){
		//System.out.println("start timer");
		startTime = System.nanoTime()/1000000000.0f;
		currTime = 0.0f;
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
		//System.out.println("pause timer at "+currTime);
		if(pauseTimeStamp == 0.0f)
			pauseTimeStamp = (System.nanoTime()/1000000000.0f);
		else{
			this.resume();
			pauseTimeStamp = (System.nanoTime()/1000000000.0f);
		}
	}
	/**
	 * resumes timer. pause() must be called previously
	 */
	public void resume(){
		if(pauseTimeStamp != 0.0f){
			pausedTime += (System.nanoTime()/1000000000.0f) - pauseTimeStamp;
			currTime = (System.nanoTime()/1000000000.0f) - startTime - pausedTime;
			pauseTimeStamp = 0.0f;
			//System.out.println("resume in timer from paused; currTime: "+currTime);
		}
		//System.out.println("resume timer at "+currTime+" and paused time is "+pausedTime);
	}
	/**
	 * returns the time to the last update call
	 * @return
	 */
	public float getDeltaTime(){
		return this.deltaFrameTime;
	}
	public float getStartTime() {
		return startTime;
	}
	public void setStartTime(float startTime) {
		this.startTime = startTime;
	}
	public float getPauseTimeStamp() {
		return pauseTimeStamp;
	}
	public void setPauseTimeStamp(float pauseTimeStamp) {
		this.pauseTimeStamp = pauseTimeStamp;
	}
	public float getPausedTime() {
		return pausedTime;
	}
	public void setPausedTime(float pausedTime) {
		this.pausedTime = pausedTime;
	}
	
}

package at.ac.tuwien.cg.cgmd.bifth2010.level20;

//import java.util.ArrayList;
//
//import android.os.Bundle;

/**
 * The Class TimeUtil takes care of all tasks belonging to time.
 * Thanks to level23 for the first implementation of this class
 * 
 * @author Markus Ernst
 * @author Florian Felberbauer
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class TimeUtil {
		
	/** The instance of TimeUtil to pass around. */
	public static TimeUtil instance = new TimeUtil();
	
//	/**
//	 * Indicates if the TimeUtil was restored from Bundle
//	 */
//	private boolean wasRestored=false;
	
	/** The Time between to frames */
	private long dt;
	
	/** The total time, the game has been running so far */
	private long totalTime;
	
	/** The accumulated frame time. */
	private long accFrameTime;
	
	/** The number of rendered frames. */
	private int nrRenderedFrames;
	
	/** The last frame time. */
	private long lastFrameTime;
	
	/** The fps refresh interval in seconds. */
	private int fpsRefreshInterval = 3;
	
	/** The fps. */
	private float fps;
	
//	/** The timer. */
//	private ArrayList<TimingTask> scheduledTimers;
		
	/**
	 * Instantiates a new time util.
	 */
	private TimeUtil() {
//		scheduledTimers = new ArrayList<TimingTask>();
		instance = this;
	}
		
	//cause lvl20 is using it
//	public static TimeUtil getInstance()
//	{
//		return instance;
//	}
	
//	/**
//	 * Writing to Bundle 
//	 * @param bundle Bundle to write to
//	 */
//	public void writeToBundle(Bundle bundle) 
//	{
//		bundle.putSerializable("timers", scheduledTimers);
//	}
//	
//	/**
//	 * Reading from Bundle 
//	 * @param bundle Bundle to read from
//	 */
//	@SuppressWarnings("unchecked")
//	public void readFromBundle(Bundle bundle) 
//	{
//		scheduledTimers = (ArrayList<TimingTask>)bundle.getSerializable("timers");
//		wasRestored = true;
//	}
//	
//	/**
//	 * Schedules timer.
//	 *
//	 * @param task the TimerTask to schedule
//	 */
//	public void scheduleTimer(TimingTask task)
//	{
//		scheduledTimers.add(task);
//	}
	
	/**
	 * Gets the dt.
	 *
	 * @return The dt
	 */
	public long getDt() {
		return dt;
	}
	
	/**
	 * Gets the total time.
	 *
	 * @return The total time
	 */
	public long getTotalTime() {
		return totalTime;
	}
	
	/**
	 * Gets the total time in seconds.
	 *
	 * @return The total time in seconds
	 */
	public float getTotalTimeInSeconds() {
		return totalTime * 0.001f;
	}
	
//	/**
//	 * Sets the dt.
//	 *
//	 * @param dt the dt to set
//	 */
//	public void setDt(float dt) {
//		this.dt = dt;
//	}

//	/**
//	 * Gets the accumulated frame times.
//	 *
//	 * @return the accFrameTimes
//	 */
//	public long getAccFrameTimes() {
//		return accFrameTime;
//	}
//	
//	/**
//	 * Sets the accumulated frame time.
//	 *
//	 * @param accFrameTimes the accFrameTimes to set
//	 */
//	public void setAccFrameTime(float accFrameTimes) {
//		this.accFrameTime = accFrameTimes;
//	}
//	
//	/**
//	 * Gets the acc frame time in sec.
//	 *
//	 * @return the accFrameTimesInSec
//	 */
//	public float getAccFrameTimeInSec() {
//		return accFrameTime/1000.0f;
//	}
	
//	/**
//	 * Gets the number of rendered frames.
//	 *
//	 * @return the nrRenderedFrames
//	 */
//	public float getNrRenderedFrames() {
//		return nrRenderedFrames;
//	}
	
//	/**
//	 * Sets the number of rendered frames.
//	 *
//	 * @param nrRenderedFrames the nrRenderedFrames to set
//	 */
//	public void setNrRenderedFrames(float nrRenderedFrames) {
//		this.nrRenderedFrames = nrRenderedFrames;
//	}
		
	/**
	 * Gets the fps.
	 *
	 * @return The fps
	 */
	public float getFPS() {
		return fps;
	}
	
	/**
	 * Updates the time.
	 */
	public void update() {
		
		long currentTime = System.currentTimeMillis();		
		
			
		//first frame
		if (lastFrameTime == 0) {
			lastFrameTime = currentTime;
		} else {		
			dt = currentTime - lastFrameTime;
			totalTime += dt;
			accFrameTime += dt;
			nrRenderedFrames++;
			lastFrameTime = currentTime;
		}
		
		if (accFrameTime >= fpsRefreshInterval * 1000) {
			fps = ((float)nrRenderedFrames / accFrameTime) * 1000;
			accFrameTime = 0;
			nrRenderedFrames = 0;
		}
		
//		for(int i=0;i<scheduledTimers.size();i++)
//		{
//			TimingTask tempTask = scheduledTimers.get(i);
//			if(tempTask.isDead)
//			{
//				scheduledTimers.remove(i);
//				tempTask.isDead = false;
//			}
//			else
//				scheduledTimers.get(i).update(dt);
//		}
	}
	
//	/**
//	 * Pauses the TimeUtil
//	 */
//	public void pause()
//	{
//		lastFrameTime = 0;
//	}
	
//	/**
//	 * Resumes the TimeUtil
//	 */
//	public void resume()
//	{
//		lastFrameTime = 0;
//	}
	
	
	/**
	 * Resets the TimeUtil
	 */
	public void reset()
	{
		lastFrameTime = 0;
		dt = 0;
	}
	
//	/**
//	 * Resets the Timers holden by the TimeUtil
//	 */
//	public void resetTimers()
//	{
//		if(!wasRestored)
//		{
//			scheduledTimers.clear();
//		}
//		else
//			wasRestored=false;
//	}
}

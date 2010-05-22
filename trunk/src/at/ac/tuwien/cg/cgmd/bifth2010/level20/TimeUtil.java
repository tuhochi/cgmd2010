package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.ArrayList;

import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TimingTask;

/**
 * The Class TimeUtil takes care of all tasks belonging to time.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class TimeUtil {
		
	/** The instance of TimeUtil to pass around. */
	public static TimeUtil instance = new TimeUtil();;
	
	/**
	 * Indicates if the TimeUtil was restored from Bundle
	 */
	private boolean wasRestored=false;
	
	/** The Time between to frames */
	private float dt;
	
	/** The accumulated frame time. */
	private float accFrameTime;
	
	/** The number of rendered frames. */
	private float nrRenderedFrames;
	
	/** The last frame time. */
	private long lastFrameTime;
	//in seconds
	/** The fps refresh interval. */
	private int fpsRefreshInterval = 3;
	
	/** The fps. */
	private float fps;
	
	/** The timer. */
	private ArrayList<TimingTask> scheduledTimers;
		
	/**
	 * Instantiates a new time util.
	 */
	private TimeUtil()
	{
		scheduledTimers = new ArrayList<TimingTask>();
		instance = this;
	}
		
	//cause lvl20 is using it
	public static TimeUtil getInstance()
	{
		return instance;
	}
	
	/**
	 * Writing to Bundle 
	 * @param bundle Bundle to write to
	 */
	public void writeToBundle(Bundle bundle) 
	{
		bundle.putSerializable("timers", scheduledTimers);
	}
	
	/**
	 * Reading from Bundle 
	 * @param bundle Bundle to read from
	 */
	@SuppressWarnings("unchecked")
	public void readFromBundle(Bundle bundle) 
	{
		scheduledTimers = (ArrayList<TimingTask>)bundle.getSerializable("timers");
		wasRestored = true;
	}
	
	/**
	 * Schedules timer.
	 *
	 * @param task the TimerTask to schedule
	 */
	public void scheduleTimer(TimingTask task)
	{
		scheduledTimers.add(task);
	}
	
	/**
	 * Gets the dt.
	 *
	 * @return the dt
	 */
	public float getDt() {
		return dt;
	}
	
	/**
	 * Sets the dt.
	 *
	 * @param dt the dt to set
	 */
	public void setDt(float dt) {
		this.dt = dt;
	}

	/**
	 * Gets the accumulated frame times.
	 *
	 * @return the accFrameTimes
	 */
	public float getAccFrameTimes() {
		return accFrameTime;
	}
	
	/**
	 * Sets the accumulated frame time.
	 *
	 * @param accFrameTimes the accFrameTimes to set
	 */
	public void setAccFrameTime(float accFrameTimes) {
		this.accFrameTime = accFrameTimes;
	}
	
	/**
	 * Gets the acc frame time in sec.
	 *
	 * @return the accFrameTimesInSec
	 */
	public float getAccFrameTimeInSec() {
		return accFrameTime/1000;
	}
	
	/**
	 * Gets the number of rendered frames.
	 *
	 * @return the nrRenderedFrames
	 */
	public float getNrRenderedFrames() {
		return nrRenderedFrames;
	}
	
	/**
	 * Sets the number of rendered frames.
	 *
	 * @param nrRenderedFrames the nrRenderedFrames to set
	 */
	public void setNrRenderedFrames(float nrRenderedFrames) {
		this.nrRenderedFrames = nrRenderedFrames;
	}
		
	/**
	 * Gets the fPS.
	 *
	 * @return the fPS
	 */
	public float getFPS()
	{
		return fps;
	}
	
	/**
	 * Updates fps.
	 */
	public void update()
	{
		long currentTime = System.currentTimeMillis();
		
		
		
		if(accFrameTime >= fpsRefreshInterval*1000)
		{
			fps = (nrRenderedFrames/accFrameTime)*1000;
			accFrameTime=0;
			nrRenderedFrames=0;
		}
			
		//first frame
		if(lastFrameTime == 0)
			lastFrameTime=currentTime;
		else
		{		
			dt = currentTime - lastFrameTime;
			accFrameTime += dt;
			nrRenderedFrames++;
			lastFrameTime = currentTime;
		}
		
		for(int i=0;i<scheduledTimers.size();i++)
		{
			TimingTask tempTask = scheduledTimers.get(i);
			if(tempTask.isDead)
			{
				scheduledTimers.remove(i);
				tempTask.isDead = false;
			}
			else
				scheduledTimers.get(i).update(dt);
		}
	}
	
	/**
	 * Resets the TimeUtil
	 */
	public void reset()
	{
		lastFrameTime = 0;
	}
	
	/**
	 * Resets the Timers holden by the TimeUtil
	 */
	public void resetTimers()
	{
		if(!wasRestored)
		{
			scheduledTimers.clear();
		}
		else
			wasRestored=false;
	}
}

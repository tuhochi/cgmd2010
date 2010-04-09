package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The Class TimeUtil takes care of all tasks belonging to time.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class TimeUtil {
	
	/** The instance of TimeUtil to pass around. */
	private static TimeUtil instance;
	
	/** The dt. */
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
	private Timer timer;
		
	/**
	 * Instantiates a new time util.
	 */
	private TimeUtil()
	{
		timer = new Timer();
		instance = this;
	}
	
	/**
	 * Gets the singleton of TimeUtil.
	 *
	 * @return singleton of TimeUtil
	 */
	public static TimeUtil getInstance()
	{
		if(instance == null)
			instance = new TimeUtil();
		
		return instance;
	}
	
	/**
	 * Cancels the timer.
	 */
	public void cancelTimer() {
		timer.cancel();
	}
	
	/**
	 * Schedules timer.
	 *
	 * @param task the TimerTask to schedule
	 * @param delay the delay
	 */
	public void scheduleTimer(TimerTask task, long delay)
	{
		timer = new Timer(); 
		timer.schedule(task, delay);
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
	}
}

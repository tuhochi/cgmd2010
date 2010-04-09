package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.util.Timer;
import java.util.TimerTask;

public class TimeUtil {
	
	private static TimeUtil instance;
	
	private float dt;
	private float accFrameTime;
	private float nrRenderedFrames;
	private long lastFrameTime;
	//in seconds
	private int fpsRefreshInterval = 3;
	
	private float fps;
	
	private Timer timer;
		
	private TimeUtil()
	{
		timer = new Timer();
		instance = this;
	}
	
	public static TimeUtil getInstance()
	{
		if(instance == null)
			instance = new TimeUtil();
		
		return instance;
	}
	
	public void cancelTimer() {
		timer.cancel();
	}
	
	public void scheduleTimer(TimerTask task, long delay)
	{
		timer = new Timer(); 
		timer.schedule(task, delay);
	}
	
	/**
	 * @return the dt
	 */
	public float getDt() {
		return dt;
	}
	/**
	 * @param dt the dt to set
	 */
	public void setDt(float dt) {
		this.dt = dt;
	}

	/**
	 * @return the accFrameTimes
	 */
	public float getAccFrameTimes() {
		return accFrameTime;
	}
	/**
	 * @param accFrameTimes the accFrameTimes to set
	 */
	public void setAccFrameTime(float accFrameTimes) {
		this.accFrameTime = accFrameTimes;
	}
	
	/**
	 * @return the accFrameTimesInSec
	 */
	public float getAccFrameTimeInSec() {
		return accFrameTime/1000;
	}
	
	/**
	 * @return the nrRenderedFrames
	 */
	public float getNrRenderedFrames() {
		return nrRenderedFrames;
	}
	/**
	 * @param nrRenderedFrames the nrRenderedFrames to set
	 */
	public void setNrRenderedFrames(float nrRenderedFrames) {
		this.nrRenderedFrames = nrRenderedFrames;
	}
		
	public float getFPS()
	{
		return fps;
	}
	
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

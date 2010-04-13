package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

public class TimeManager {
	long t;
	long t0;
	long dt;
	int frames;
	float fps;
	long fpsT0;
	boolean fpsChanged;
	
	public static final TimeManager instance = new TimeManager();
	
	private TimeManager()
	{
		reset();
	}
	
	public synchronized void reset()
	{
		t0 = System.currentTimeMillis();
		t = t0;
		dt = 0;
		fpsT0 = t0;
		frames = 0;
		fps = 0;
		fpsChanged = false;
	}
	
	public synchronized void update()
	{
		t = System.currentTimeMillis();
		
		// delta time since last frame
		dt = t-t0;

		// calc fps if FPS_UPDATE_INTERVAL seconds have passed since the last calculation
		if((t-fpsT0) >= (Config.FPS_UPDATE_INTERVAL*1000.0f))
		{
			// round to xxxx.xx
			fps = (float)((Math.ceil((((float)frames) / (((float)(t-fpsT0)) / 1000.0f))*100.0f)) / 100.0);
			frames = 0;
			fpsT0 = t;
			fpsChanged = true;
		}
		else
			fpsChanged = false;

		// increment frame counter (for fps calculation)
		frames++;
		
		// set the current time as the last time for the next frame
		t0 = t;
	}
	
	
	public synchronized long getDeltaTmillis()
	{
		return dt;
	}
	
	public synchronized float getDeltaTsec()
	{
		return ((float)dt)/1000.0f;
	}
	
	public synchronized float getFPS()
	{
		return fps;
	}
	
	public synchronized boolean haveFPSchanged()
	{
		return fpsChanged;
	}

	public synchronized long getTimeOfLastFrame()
	{
		return t;
	}
		
}

package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;

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
		t0 = System.currentTimeMillis();
		t = t0;
		dt = 0;
		fpsT0 = t0;
		frames = 0;
		fps = 0;
		fpsChanged = false;
	}
	
	public void update()
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
			Log.i(LevelActivity.TAG + "_FPS",fps + "");
		}
		else
			fpsChanged = false;

		// increment frame counter (for fps calculation)
		frames++;
		
		// set the current time as the last time for the next frame
		t0 = t;
	}
	
	
	public long getDeltaTmillis()
	{
		return dt;
	}
	public float getDeltaTsec()
	{
		return ((float)dt)/1000.0f;
	}
	public float getFPS()
	{
		return fps;
	}
	public boolean haveFPSchanged()
	{
		return fpsChanged;
	}

	public long getTimeOfLastFrame()
	{
		return t;
	}
		
}

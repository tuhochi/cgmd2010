package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import android.util.Log;


/**
 * Keeps the time running and updates the world in it's own thread
 * @author MaMa
 *
 */
public class GameThread extends Thread {

	private World mWorld;
	private boolean mRunning = true;
	
	/**
	 * Creates a new World
	 * @param world The world to work in
	 */
	public GameThread(World world)
	{
		mWorld = world;
	}
	
	/**
	 * Start the update thread
	 */
    public void run() 
    {
    	while(mRunning)	
    	{
    		mWorld.update(); 
    		try {
				sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
    	}
    	Log.d("GAMETHREAD", "Thread terminated");
    }

    public void runOut()
	{
    	mRunning = false;
	}

}

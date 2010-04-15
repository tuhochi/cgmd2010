package at.ac.tuwien.cg.cgmd.bifth2010.level17;


/**
 * Keeps the time running and updates the world in it's own thread
 * @author MaMa
 *
 */
public class GameThread extends Thread {

	private World mWorld;
	
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
    	while(true)	
    	{
    		mWorld.update(); 
    		try {
				sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
    	}
    }

}

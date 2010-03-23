package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLView;


public class GameThread extends Thread {

	private World mWorld;
	
	public GameThread(GLView view, World world)
	{
		mWorld = world;
	}
	
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

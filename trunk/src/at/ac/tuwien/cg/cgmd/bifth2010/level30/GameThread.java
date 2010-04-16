package at.ac.tuwien.cg.cgmd.bifth2010.level30;


public class GameThread extends Thread {

	private GameWorld gameWorld;
	
	public GameThread(GameWorld world)
	{
		gameWorld = world;
	}
	
    public void run() 
    {
    	while(true)	
    	{
    		gameWorld.Framemove(); 
    		try {
				sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
    	}
    }

}

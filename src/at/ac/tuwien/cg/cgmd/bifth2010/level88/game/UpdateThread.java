package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

import at.ac.tuwien.cg.cgmd.bifth2010.level88.GLView;

public class UpdateThread extends Thread {
	private Game game;
	
	public UpdateThread(GLView view, Game _game)
	{
		game = _game;
	}
	
    public void run() 
    {
    	while(true)	
    	{
    		game.update(); 
    		try {
				sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
    	}
    }
}

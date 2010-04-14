package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

import at.ac.tuwien.cg.cgmd.bifth2010.level88.GLView;


/**
 * @author Asperger, Radax
 *
 */
public class UpdateThread extends Thread {
	private Game game;
	
	
	/**
	 * @param view
	 * @param _game
	 */
	public UpdateThread(GLView view, Game _game)
	{
		game = _game;
	}
	
	
    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    public void run() 
    {
    	while(true)	
    	{
    		game.update(); 
    		try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
    	}
    }
}

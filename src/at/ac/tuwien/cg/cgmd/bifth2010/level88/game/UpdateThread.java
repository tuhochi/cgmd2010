package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

import at.ac.tuwien.cg.cgmd.bifth2010.level88.GLView;


/**
 * Class for updating the threads
 * @author Asperger, Radax
 */
public class UpdateThread extends Thread {
	private Game game;
	
	
	/**
	 * Constructor
	 * @param view view of the android context
	 * @param _game game context
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

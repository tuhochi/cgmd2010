package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class ProgressManager extends SessionState {

	private int gamepoints = 0;
	private int progress = 0; // must be between 0-100
	
	// the more difficult the figure of the gem is ...
	// ... the more points you can lose ?!
	
	public void addPoints(int value)
	{
		gamepoints += value;
	}
	
	public void losePoints(int value)
	{
		gamepoints -= value;
	}
	
	public int getPoints()
	{
		return gamepoints;
	}
	
}

package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.Hud;

public class PermaBoostTimer extends TimingTask {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The time when the timer should trigger. */
	public float time = Settings.GOLD_BOOST_TIME;
	/** The remaining time till the timer triggers. */
	public float remainingTime = Settings.GOLD_BOOST_TIME;
	
	@Override
	public void run() 
	{
		Hud.instance.setGoldButtonActive(true);
		isDead = true;
		remainingTime = time;	
	}

	@Override
	public void update(float dt) 
	{
		remainingTime -= dt;
		if (remainingTime <= 0)
			run();
		
	}

}

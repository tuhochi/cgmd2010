package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.Hud;

public class PermaBoostTimer extends TimingTask {

	/**
	 * This class extends the TimingTask and is used to time the boost action
	 * @author Markus Ernst
	 * @author Florian Felberbauer
	 */
	private static final long serialVersionUID = 1L;

	/** The time when the timer should trigger. */
	public float time = Settings.GOLD_BOOST_TIME;
	/** The remaining time till the timer triggers. */
	public float remainingTime = Settings.GOLD_BOOST_TIME;
	
	/**
	 * overrides the run method
	 */
	@Override
	public void run() 
	{
		Hud.instance.setGoldButtonActive(true);
		Hud.instance.setMoneyButtonActive(true);
		isDead = true;
		remainingTime = time;	
		Hud.instance.renderScreenCrack = false;
	}

	/**
	 * Overrides the update method and decreases the remaining time
	 */
	@Override
	public void update(float dt) 
	{
		remainingTime -= dt;
		if (remainingTime <= 0)
			run();
		
	}

}

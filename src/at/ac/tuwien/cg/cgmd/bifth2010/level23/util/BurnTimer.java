package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.Hud;

/**
	 * Th Class BurnTimer handles all tasks needed for the money bonus 
	 */
public class BurnTimer extends TimingTask 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4717448091564254143L;

	public float time = Settings.BURN_BOOST_TIME;
	public float remainingTime = Settings.BURN_BOOST_TIME;
	private int audioPlayerId;
	
	/**
	 * Default Constructor
	 * @param audioPlayerId the id of the MediaPlayer which will be stopped by the timer
	 */
	public BurnTimer(int audioPlayerId)
	{
		this.audioPlayerId = audioPlayerId;
	}
	
	/**
	 * sets the balloon speed to the old value after the boost time ran up
	 */
	@Override
	public void run() {
		Settings.BALLOON_SPEED -= Settings.BURN_BOOST;
		Hud.instance.setMoneyButtonActive(true);
		isDead = true;
		remainingTime = time;
		SoundManager.instance.pausePlayer(audioPlayerId);
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TimingTask#update(float)
	 */
	@Override
	public void update(float dt) {
		remainingTime -= dt;
		Hud.instance.burnProgress = remainingTime/time;
		if (remainingTime <= 0)
			run();
	}
}
package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.Hud;

/**
 * The Class BurnTimer handles all tasks needed for the money bonus 
 *
 * @author Markus Ernst
 * @author Florian Felberbauer
 *
 */
public class BurnTimer extends TimingTask 
{
	/** The serialization id. */
	private static final long serialVersionUID = 4717448091564254143L;
	/** The time when the timer should trigger. */
	public float time = Settings.BURN_BOOST_TIME;
	/** The remaining time till the timer triggers. */
	public float remainingTime = Settings.BURN_BOOST_TIME;
	/** The id of the associated audio player. */
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
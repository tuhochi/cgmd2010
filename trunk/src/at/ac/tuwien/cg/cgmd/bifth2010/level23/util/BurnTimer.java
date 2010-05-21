package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import android.widget.ProgressBar;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;
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
	class ProgressHandle implements Runnable{
		  
    	@Override
        public void run() {
            LevelActivity.instance.boostProgress.setProgress(((int)(remainingTime*100/time)));
        }
    };
    
	/** The serialization id. */
	private static final long serialVersionUID = 4717448091564254143L;
	/** The time when the timer should trigger. */
	public float time = Settings.BURN_BOOST_TIME;
	/** The remaining time till the timer triggers. */
	public float remainingTime = Settings.BURN_BOOST_TIME;
	/** The id of the associated audio player. */
	private int audioPlayerId;
	
	private ProgressHandle progressHandle;
	private ProgressVisibilityHandle progressVisibilityHandle;
	
	/**
	 * Default Constructor
	 * @param audioPlayerId the id of the MediaPlayer which will be stopped by the timer
	 */
	public BurnTimer(int audioPlayerId)
	{
		this.audioPlayerId = audioPlayerId;
		progressHandle = new ProgressHandle();
		progressVisibilityHandle = new ProgressVisibilityHandle();
		progressVisibilityHandle.visibility = ProgressBar.INVISIBLE;
	}
	
	/**
	 * sets the balloon speed to the old value after the boost time ran up
	 */
	@Override
	public void run() {
		Settings.BALLOON_SPEED /= Settings.BURN_BOOST;
		Hud.instance.setMoneyButtonActive(true);
		isDead = true;
		remainingTime = time;
		SoundManager.instance.pausePlayer(audioPlayerId);
		LevelActivity.handler.post(progressVisibilityHandle);
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TimingTask#update(float)
	 */
	@Override
	public void update(float dt) {
		remainingTime -= dt;
		LevelActivity.handler.post(progressHandle);
		if (remainingTime <= 0)
			run();
	}
}
package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import java.io.Serializable;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.Crosshairs;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalRabbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.CoinBucketSprite;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.RabbitSprite;

/**
 * Serializable representation of the MireRabbit game state
 * 
 * This is a simplified representation of the game state that can
 * be used to serialize and restore important state information
 * of a running game, and is used to implement the Android life-cycle
 * management for the MireRabbit level.
 * 
 * @author Thomas Perl
 */
public class GameState implements Serializable {
	/** id for serialization */
	private static final long serialVersionUID = 1709132339289272769L;
	/** id for saving the game state */
	public static final String KEY = "level44_game_state";
	/** elapsed time since start of the game */
	private long elapsedTime = 0;
	/** x-position of the crosshairs */
	private float crosshairsX = 0;
	/** y-position of the crosshairs */
	private float crosshairsY = 0;
	/** number of coins left */
	private int coinCount = 0;
	/** x-position of the rabbit */
	private float rabbitX = 0;
	/** y-position of the rabbit */
	private float rabbitY = 0;
	/** current state of game (intro/playing/extro) */
	private GameScene.CurrentState currentState = GameScene.CurrentState.INTRO;

	/**
	 * set the elapsed time
	 * 
	 * @param elapsedTime
	 *            the elapsed time to set
	 */
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	/**
	 * @return the elapsed time of the game
	 */
	public long getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * restore the position of the crosshairs
	 * 
	 * @param crosshairs
	 *            the crosshairs, which position is set
	 */
	public void restoreCrosshairs(Crosshairs crosshairs) {
		crosshairs.setPosition(crosshairsX, crosshairsY);
	}

	/**
	 * save the position of the crosshairs
	 * 
	 * @param crosshairs
	 *            the crosshairs, which position is saved
	 */
	public void saveCrosshairs(Crosshairs crosshairs) {
		if (crosshairs != null) {
			crosshairsX = crosshairs.getX();
			crosshairsY = crosshairs.getY();
		}
	}

	/**
	 * restore important values of the rabbit
	 * 
	 * @param rabbit
	 *            the rabbit, which values are set
	 */
	public void restoreRabbit(PhysicalRabbit rabbit) {
		RabbitSprite sprite = rabbit.getSprite();
		sprite.setPosition(rabbitX, rabbitY);
		CoinBucketSprite coinBucket = sprite.getCoinBucket();
		coinBucket.setCoinCount(coinCount);
	}

	/**
	 * save important values of the rabbit
	 * 
	 * @param rabbit
	 *            the rabbit, which values are saved
	 */
	public void saveRabbit(PhysicalRabbit rabbit) {
		if (rabbit != null) {
			RabbitSprite sprite = rabbit.getSprite();
			rabbitX = sprite.getX();
			rabbitY = sprite.getY();
			CoinBucketSprite coinBucket = sprite.getCoinBucket();
			coinCount = coinBucket.getCoinCount();
		}
	}

	/**
	 * restore important values of the TimeManager
	 * 
	 * @param timeManager
	 *            the TimeManager, which values are set
	 */
	public void restoreTimeManger(TimeManager timeManager) {
		if (timeManager != null) {
			timeManager.setElapsed(elapsedTime);
		}
	}

	/**
	 * save important values of the TimeManager
	 * 
	 * @param timeManager
	 *            the TimeManager, which values are saved
	 */
	public void saveTimeManger(TimeManager timeManager) {
		if (timeManager != null) {
			elapsedTime = timeManager.getElapsed();
		}
	}

	/**
	 * restore important values of the GameState
	 * 
	 * @param scene
	 *            the scene, which gameState are set
	 */
	public void restoreCurrentState(GameScene scene) {
		if (scene != null) {
			scene.setCurrentState(currentState);
		}
	}

	/**
	 * save important values of the GameState
	 * 
	 * @param scene
	 *            the GameScene, which GameState is saved
	 */
	public void saveCurrentState(GameScene scene) {
		if (scene != null) {
			currentState = scene.getCurrentState();
		}
	}
}

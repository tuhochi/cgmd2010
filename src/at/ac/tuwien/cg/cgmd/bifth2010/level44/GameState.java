package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import java.io.Serializable;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.Crosshairs;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalRabbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.CoinBucketSprite;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.RabbitSprite;

public class GameState implements Serializable {
	public static final String KEY = "level44_game_state";

	private long elapsedTime = 0;
	private float crosshairsX = 0;
	private float crosshairsY = 0;
	private int coinCount = 0;
	private float rabbitX = 0;
	private float rabbitY = 0;
	private GameScene.CurrentState currentState = GameScene.CurrentState.INTRO;
	
	public GameState() {}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}
	
	public void restoreCrosshairs(Crosshairs crosshairs) {
		crosshairs.setPosition(crosshairsX, crosshairsY);
	}
	
	public void saveCrosshairs(Crosshairs crosshairs) {
		crosshairsX = crosshairs.getX();
		crosshairsY = crosshairs.getY();
	}
	
	public void restoreRabbit(PhysicalRabbit rabbit) {
		RabbitSprite sprite = rabbit.getSprite();
		sprite.setPosition(rabbitX, rabbitY);
		CoinBucketSprite coinBucket = sprite.getCoinBucket();
		coinBucket.setCoinCount(coinCount);
	}
	
	public void saveRabbit(PhysicalRabbit rabbit) {
		RabbitSprite sprite = rabbit.getSprite();
		rabbitX = sprite.getX();
		rabbitY = sprite.getY();
		CoinBucketSprite coinBucket = sprite.getCoinBucket();
		coinCount = coinBucket.getCoinCount();
	}
	
	public void restoreTimeManger(TimeManager timeManager) {
		if (timeManager != null) {
			timeManager.setElapsed(elapsedTime);
		}
	}

	public void saveTimeManger(TimeManager timeManager) {
		if (timeManager != null) {
			elapsedTime = timeManager.getElapsed();
		}
	}
	
	public void restoreCurrentState(GameScene scene) {
		if (scene != null) {
			scene.setCurrentState(currentState);
		}
	}
	
	public void saveCurrentState(GameScene scene) {
		if (scene != null) {
			currentState = scene.getCurrentState();
		}
	}
}

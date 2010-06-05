package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.DoubleTap;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.SingleTap;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.Swipe;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.Crosshairs;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalRabbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.sound.SoundPlayer;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Landscape;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.VirtualFinger;

public class GameThread extends Thread {
	private GameScene scene;
	private PhysicalRabbit rabbit;
	private Crosshairs crosshairs;
	private Landscape landscape;
	private TimeManager timeManager;
	private VirtualFinger virtualFinger;
	/** is the thread stopped? */
	private boolean quit;
	private InputGesture gesture = null;

	public GameThread(GameScene scene, PhysicalObject rabbit, Landscape landscape, Crosshairs crosshairs, TimeManager timeManager, VirtualFinger virtualFinger) {
		this.scene = scene;
		this.rabbit = (PhysicalRabbit) rabbit;
		this.crosshairs = crosshairs;
		this.landscape = landscape;
		this.timeManager = timeManager;
		this.virtualFinger = virtualFinger;
		this.quit = false;
	}

	/**
	 * Determine if the level has been finished. The level is finished when
	 * there are no coins left or when the time is up.
	 * 
	 * @return True if the level has been finished by the user
	 */
	public boolean levelFinished() {
		return rabbit.getCoinCount() == 0 || timeManager.timeIsUp();
	}

	public void run() {
		while (!quit) {
			scene.queueEvent(new Runnable() {
				public void run() {
					// Currently in Intro-Mode
					if (scene.getCurrentState().equals(GameScene.CurrentState.INTRO)) {
						virtualFinger.step();
						rabbit.setPosition((float) (scene.getWidth() / 2 + scene.getWidth() / 6 * Math.sin((double) (System.currentTimeMillis() / 10000.))),
								(float) (scene.getHeight() / 3));
						rabbit.getSprite().setScale((float) (.5 + .5 * Math.abs(Math.sin((double) (System.currentTimeMillis() / 5000.)))));
						rabbit.getSprite().setWingAngle((float) (Math.sin((double) (System.currentTimeMillis() / 100.)) * 45));
						rabbit.getSprite().setRotation((float) (10 - Math.sin((double) (System.currentTimeMillis() / 1000.)) * 20));

						// continue with single-tap
						if (scene.getNextInputGesture() instanceof SingleTap) {
							scene.setCurrentState(GameScene.CurrentState.RUNNING);

							// start time for physical movement is resetted
							rabbit.resetStartTime(0);
							// reset moving variables
							rabbit.resetMovement();
							// position center
							rabbit.setPosition(scene.getWidth() / 2, scene.getHeight() / 2);
						}
					}

					// currently in Playing-Mode
					else if (scene.getCurrentState().equals(GameScene.CurrentState.RUNNING)) {
						if (levelFinished()) {
							rabbit.setBoundaryCheck(false);
							rabbit.resetStartTime(0);

							scene.setCurrentState(GameScene.CurrentState.EXTRO);
						}

						// process input gesture
						gesture = scene.getNextInputGesture();
						if (gesture instanceof SingleTap)
							gesture = null;

						rabbit.processGesture(gesture);

						// perform movement of rabbit
						rabbit.move();
						// move crosshairs
						crosshairs.ai();
						// play sound effects
						playSoundEffects(gesture);

						// reset start time if rabbit sits at the bottom of the
						// screen
						if (rabbit.hasLanded()) {
							rabbit.resetStartTime(0);
							rabbit.setVelocity(0.f);
						}

						/* stars animation */
						rabbit.getSprite().tick();

						// perform movement of landscape
						landscape.step();
						// update game-time
						timeManager.update();
					}

					// currently in Extro-Mode
					else if (scene.getCurrentState().equals(GameScene.CurrentState.EXTRO)) {
						// movement depends on current rotation
						if (rabbit.getSprite().getRotation() < -5.f) {
							rabbit.processGesture(new Swipe(5, 0, scene.getHeight(), 5, Swipe.MAX_VELOCITY, InputGesture.Position.LEFT));
						} else if (rabbit.getSprite().getRotation() > 5.f) {
							rabbit.processGesture(new Swipe(scene.getWidth() - 5, scene.getHeight(), scene.getWidth() - 5, 0,Swipe.MAX_VELOCITY, InputGesture.Position.RIGHT));
						} else {
							rabbit.processGesture(new DoubleTap(4.f, 4.f));
						}

						rabbit.move();
						landscape.step();
						
						/* stars animation */
						rabbit.getSprite().tick();

						// shrink rabbit
						if (System.currentTimeMillis() % 4 == 0)
							rabbit.getSprite().setScale(rabbit.getSprite().getScale() * 0.997f);

						// rabbit isn't visible anymore -> finish level
						if (rabbit.getY() + rabbit.getSprite().getHeight() < -40) {
							scene.finishLevel();
						}
					}
				}
			});

			try {
				sleep(10);
			} catch (InterruptedException ie) {
			}
		}

	}

	public void doQuit() {
		this.quit = true;
	}

	/**
	 * Plays a SoundEffect when the wings are flapped
	 * 
	 * @param gesture
	 */
	public void playSoundEffects(InputGesture gesture) {
		float soundPosition = 0.5f;

		if (gesture != null) {
			if (gesture instanceof Swipe) {
				Swipe swipe = (Swipe) gesture;

				// if only left wing is flapped, play the sound
				// from the left channel
				if (swipe.isLeft()) {
					soundPosition = 0.2f;
				} else if (swipe.isRight()) {
					soundPosition = 0.8f;
				}
			}

			SoundPlayer.getInstance().play(SoundPlayer.SoundEffect.FLAP, soundPosition);
		}

		// reload after a delay?
		if (crosshairs.changeLoadingState()) {
			(new Thread() {
				public void run() {
					try {
						Thread.sleep(400L);
					} catch (Exception ex) {
					}

					SoundPlayer.getInstance().play(SoundPlayer.SoundEffect.LOAD, 0.5f);
				}
			}).start();
		}
	}
}

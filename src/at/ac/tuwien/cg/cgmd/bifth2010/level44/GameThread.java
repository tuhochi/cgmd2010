package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.Swipe;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.Crosshairs;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalRabbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.sound.SoundPlayer;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Landscape;

public class GameThread extends Thread {
	private GameScene scene;
	private PhysicalRabbit rabbit;
	private Crosshairs crosshairs;
	private Landscape landscape;
	private TimeManager timeManager;
	private boolean quit;
	private InputGesture gesture = null;

	public GameThread(GameScene scene, PhysicalObject rabbit, Landscape landscape, Crosshairs crosshairs, TimeManager timeManager) {
		this.scene = scene;
		this.rabbit = (PhysicalRabbit) rabbit;
		this.crosshairs = crosshairs;
		this.landscape = landscape;
		this.timeManager = timeManager;
		this.quit = false;
	}
	
	/**
	 * Determine if the level has been finished. The level is
	 * finished when there are no coins left or when the time
	 * is up.
	 * 
	 * @return True if the level has been finished by the user
	 */
	public boolean levelFinished() {
		return rabbit.getCoinCount() == 0 || timeManager.timeIsUp();
	}

	public void run() {
		rabbit.resetStartTime(0);
		
		while (!quit) {
			scene.queueEvent(new Runnable() {
				public void run() {
					if (levelFinished()) {
						scene.finishLevel();
					}
					
					// process input gesture 
					gesture = scene.getNextInputGesture();
					rabbit.processGesture(gesture); 
					// perform movement of rabbit
					rabbit.move();
					// move crosshairs
					crosshairs.ai();
					// play sound effects
					playSoundEffects(gesture);
					
					
					// reset start time if rabbit sits at the bottom of the screen
					if (rabbit.hasLanded()) {
						rabbit.resetStartTime(0);
						rabbit.setVelocity(0.f);
						scene.clearInputQueue();
					}
					
					landscape.step();
					timeManager.update();

					/*if (TimeManager.getInstance().getRemainingTimeMillis() == 0) {
						doQuit();
					}*/

					 /* old moving, maybe useful as intro
					  * rabbit.setPosition((float)(scene.getWidth()/2 +
					 scene.getWidth()/5*Math.sin((double)(System.currentTimeMillis()/10000.))),
					 (float)(scene.getHeight()/3));
					 rabbit.getRabbit().setScale((float)(.5+.5*Math.abs(Math.sin((double)(System.currentTimeMillis()/5000.)))));
					 rabbit.getRabbit().setWingAngle((float)(Math.sin((double)(System.currentTimeMillis()/100.))*45));
					 rabbit.getRabbit().setRotation((float)(10-Math.sin((double)(System.currentTimeMillis()/1000.))*20));*/

					//frameRate--;
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
	
	public void playSoundEffects(InputGesture gesture) {
		float soundPosition = 0.5f;
		
		if (gesture != null) {
			if (gesture instanceof Swipe) {
				Swipe swipe = (Swipe)gesture;
				
				if (swipe.isLeft()) {
					soundPosition = 0.2f;
				} else if (swipe.isRight()) {
					soundPosition = 0.8f;
				}
			}
			
			scene.getSoundPlayer().play(SoundPlayer.SoundEffect.FLAP, soundPosition);
		}
		
		if (crosshairs.changeLoadingState()) {
			(new Thread() {
				public void run() {
					try {
						Thread.sleep(400L);
					} catch(Exception ex) {}
					
					scene.getSoundPlayer().play(SoundPlayer.SoundEffect.LOAD, 0.5f);
				}
			}).start();
		}
	}
}

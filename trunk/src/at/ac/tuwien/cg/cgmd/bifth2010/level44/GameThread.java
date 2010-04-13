package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.Swipe;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.Crosshairs;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalRabbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.sound.SoundPlayer;

public class GameThread extends Thread {
	private static final int FRAME_RATE = 200;

	private GameScene scene;
	private PhysicalRabbit rabbit;
	private Crosshairs crosshairs;
	private boolean quit;
	private InputGesture gesture = null;
	private int frameRate = FRAME_RATE;

	public GameThread(GameScene scene, PhysicalObject rabbit, Crosshairs crosshairs) {
		this.scene = scene;
		this.rabbit = (PhysicalRabbit) rabbit;
		this.crosshairs = crosshairs;
		this.quit = false;

		// TODO Remove and make textures smaller
		this.rabbit.getSprite().setScale(0.4f);
	}

	public void run() {
		rabbit.resetStartTime(0);
		
		while (!quit) {
			scene.queueEvent(new Runnable() {
				public void run() {
				/*	if (gesture == null || frameRate == 0) {
						gesture = scene.getNextInputGesture();

						if (frameRate == 0)
							frameRate = FRAME_RATE;
					}*/

					// process input gesture 
					gesture = scene.getNextInputGesture();
					rabbit.processGesture(gesture); 
					// perform movement of rabbit
					rabbit.move();
					// play sound effect if wings are moving
					playFlapSound(gesture);
					// move crosshairs
					crosshairs.ai();
					
					
					// reset start time if rabbit sits at the bottom of the screen
					if (rabbit.getSprite().isUnder(scene.getHeight() - 5)) {
						rabbit.resetStartTime(0);
						rabbit.setVelocity(0.f);
					}
					

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
	
	public void playFlapSound(InputGesture gesture) {
		float soundPosition = 0.5f;
		
		if (gesture != null) {
			if (gesture instanceof Swipe) {
				Swipe swipe = (Swipe)gesture;
				
				if (swipe.isLeft()) {
					soundPosition = 0.f;
				} else if (swipe.isRight()) {
					soundPosition = 1.0f;
				}
			}
			
			scene.getSoundPlayer().play(SoundPlayer.SoundEffect.FLAP, soundPosition);
		}
	}
}

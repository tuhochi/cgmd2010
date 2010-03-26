package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.Swipe;

public class GameThread extends Thread {
	private static final int FRAME_RATE = 100;
	
	private GameScene scene;
	private Rabbit rabbit;
	private boolean quit;
	private InputGesture gesture = null;
	private int frameRate = FRAME_RATE;
	
	public GameThread(GameScene scene, Rabbit rabbit) {
		this.scene = scene;
		this.rabbit = rabbit;
		this.quit = false;
		
		//TODO Remove
		this.rabbit.setScale(0.5f);
	}
	
	public void run() {
		while (!quit) {
			scene.queueEvent(new Runnable() {
				public void run() {
					if (gesture == null || frameRate == 0) {
						gesture = scene.getNextInputGesture();
						
						if (frameRate == 0)
							frameRate = FRAME_RATE;
					}
					
					if (gesture != null) {
						if (gesture instanceof Swipe) {
							Swipe swipe = (Swipe)gesture;
							
							if (swipe.isLeftHalf()) {
								rabbit.setLeftWingAngle((float)(Math.sin((double)(System.currentTimeMillis()/100.))*45));
							} else {
								rabbit.setRightWingAngle((float)(Math.sin((double)(System.currentTimeMillis()/100.))*45));
							}
						}
						//rabbit.setPosition((float)(scene.getWidth()/2 + scene.getWidth()/5*Math.sin((double)(System.currentTimeMillis()/10000.))), (float)(scene.getHeight()/3));
						//rabbit.setScale((float)(.5+.5*Math.abs(Math.sin((double)(System.currentTimeMillis()/5000.)))));
						//rabbit.setWingAngle((float)(Math.sin((double)(System.currentTimeMillis()/100.))*45));
						//rabbit.setRotation((float)(10-Math.sin((double)(System.currentTimeMillis()/1000.))*20));
					}
					
					frameRate--;
				}
			});
			try {
				sleep(10);
			} catch (InterruptedException ie) {}
		}
	}
	
	public void doQuit() {
		this.quit = true;
	}
}

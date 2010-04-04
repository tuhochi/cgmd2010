package at.ac.tuwien.cg.cgmd.bifth2010.level44.io;

import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.GameScene;

public class InputListener extends SimpleOnGestureListener {
	private static final int SWIPE_MAX_OFF_PATH = 70;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	
	private int width;
	private int height;
	private GameScene scene;

	
	public InputListener(GameScene scene, int width, int height) {
		this.scene = scene;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		InputGesture.DisplayHalf displayHalf;
		
		// check that swipe is straight
		if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH)
            return false;
		
		// check if swipe is long enough to ge a gesture
		if (e2.getY() - e1.getY() > Swipe.MIN_LENGTH && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
			// left half of screen
			if (e1.getX() < width/2) {
				System.out.println("Swipe Gesture on Left Half");
				displayHalf = InputGesture.DisplayHalf.LEFT;
			} 
			// right half of screen
			else {
				System.out.println("Swipe Gesture on Right Half");
				displayHalf = InputGesture.DisplayHalf.RIGHT;
			}
			
			// add Gesture to InputQueue
			scene.addInputGesture(new Swipe(e1.getX(), e1.getY(), e2.getX(), e2.getY(), displayHalf));
		}
		
		return true;
	}
	
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		scene.addInputGesture(new DoubleTap(e.getX(), e.getY()));
		
		return true;
	}
}

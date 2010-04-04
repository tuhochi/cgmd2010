package at.ac.tuwien.cg.cgmd.bifth2010.level44.io;

/**
 * This Class wrappes a Swipe-Gesture
 * 
 * @author Matthias
 *
 */

public class Swipe implements InputGesture {
	/** minimum length of a swipe */
	public static final float MIN_LENGTH = 70.f;
	/** maximum length of a swipe */
	public static final float MAX_LENGTH = 270.f;
	/** difference between max and min swipe */
	public static final float MAX_MIN_DELTA = MAX_LENGTH - MIN_LENGTH;
	
	/** start-x of swipe */
	private float startX = 0;
	/** start-y of swipe */
	private float startY = 0;
	/** end-x of swipe */
	private float endX = 0;
	/** end-y of swipe */
	private float endY = 0;
	/** length of swipe */
	private float length = 0;
	/** screenhalf of the swipe */
	private InputGesture.ScreenHalf screenHalf;
	
	public Swipe(float x1, float y1, float x2, float y2, InputGesture.ScreenHalf screenHalf) {
		this.startX = x1;
		this.startY = y1;
		this.endX = x2;
		this.endY = y2;
		
		this.screenHalf = screenHalf;
		// compute length of swipe, pythagoras
		this.length = Math.min(MAX_LENGTH, Math.round(Math.sqrt((endX - startX) + (endX - startX) + (endY - startY) * (endY - startY))));
	}
	
	public float getStrength() {
		return length;
	}
	
	public InputGesture.ScreenHalf getScreenHalf() {
		return screenHalf;
	}
	
	public boolean isLeftHalf() {
		return screenHalf.equals(InputGesture.ScreenHalf.LEFT);
	}
	
	public boolean isRightHalf() {
		return !isLeftHalf();
	}

	@Override
	public float getEndX() {
		return endX;
	}

	@Override
	public float getEndY() {
		return endY;
	}

	@Override
	public float getStartX() {
		return startX;
	}

	@Override
	public float getStartY() {
		return startY;
	}
}

package at.ac.tuwien.cg.cgmd.bifth2010.level44.io;

/**
 * This Class wrappes a Swipe-Gesture
 * 
 * @author Matthias Tretter
 * 
 */

public class Swipe implements InputGesture {
	/** minimum length of a swipe */
	public static final float MIN_LENGTH = 100.f;
	/** maximum length of a swipe */
	public static final float MAX_LENGTH = 220.f;
	/** minimum velocity */
	public static final float MIN_VELOCITY = 750.f;
	/** maximum velocity */
	public static final float MAX_VELOCITY = 2500.f;
	/** difference between max and min swipe */
	public static final float MAX_MIN_DELTA_LENGTH = MAX_LENGTH - MIN_LENGTH;
	/** difference between max and min swipe */
	public static final float MAX_MIN_DELTA_VELOCITY = MAX_VELOCITY - MIN_VELOCITY;

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
	/** vertical velocity */
	private float velocity = 0.f;
	/** position of the swipe */
	private InputGesture.Position position;

	/**
	 * creates a Swipe
	 * 
	 * @param x1
	 *            start-x
	 * @param y1
	 *            start-y
	 * @param x2
	 *            end-x
	 * @param y2
	 *            end-y
	 * @param velocity
	 *            vertical velocity
	 * @param position
	 *            position of the swipe
	 */
	public Swipe(float x1, float y1, float x2, float y2, float velocity, InputGesture.Position position) {
		this.startX = x1;
		this.startY = y1;
		this.endX = x2;
		this.endY = y2;
		this.velocity = velocity;
		this.position = position;

		// compute length of swipe, pythagoras
		this.length = Math.min(MAX_LENGTH, Math.round(Math.sqrt((endX - startX) * (endX - startX) + (endY - startY) * (endY - startY))));
	}

	/**
	 * @return the power of the swipe
	 */
	public float getPower() {
		return Math.max(MIN_LENGTH, Math.min(MAX_LENGTH, velocity * MAX_LENGTH / MAX_VELOCITY));
	}

	/**
	 * @return the vertical velocity of the swipe
	 */
	public float getVelocity() {
		return velocity;
	}

	/**
	 * @return the length of the swipe-gesture
	 */
	public float getLength() {
		return length;
	}

	/**
	 * @return the position of the swipe
	 */
	public InputGesture.Position getPosition() {
		return position;
	}

	/**
	 * @return true, if the swipe is in the left third
	 */
	public boolean isLeft() {
		return position.equals(InputGesture.Position.LEFT);
	}

	/**
	 * @return true, if the swipe is in the middle third
	 */
	public boolean isMiddle() {
		return position.equals(InputGesture.Position.MIDDLE);
	}

	/**
	 * @return true, if the swipe is in the right third
	 */
	public boolean isRight() {
		return position.equals(InputGesture.Position.RIGHT);
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

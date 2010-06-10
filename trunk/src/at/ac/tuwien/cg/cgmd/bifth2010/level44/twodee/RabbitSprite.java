package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.Swipe;

/**
 * This class is the visual representation of the Rabbit
 * 
 * @author Matthias
 * 
 */

public class RabbitSprite extends SpriteContainer {
	/** maximum angle the wings can have (+/-) */
	private static final float ANGLE_MAX = 45.f;
	/** step of the angle of the wing if it is flapping */
	private static final float FLAP_DELTA = 3.5f;
	/** maximum rotation the rabbit itself can have */
	private static final float MAX_ROTATION = 30.f;
	/**
	 * quotient to map a maximum swipe to ANGLE_MAX and every shorter swipe
	 * proportionally
	 */
	private static final float ANGLE_QUOTIENT = ANGLE_MAX / Swipe.MAX_MIN_DELTA_LENGTH;
	/** horizontal offset for center */
	private static final int CENTER_X = 45;
	/** vertical offset for center */
	private static final int CENTER_Y = 42;
	/** the stars that appear when the rabbit is hit */
	private Stars stars;
	/** sprite for the left wing */
	private Sprite leftWing;
	/** sprite for the right wing */
	private Sprite rightWing;
	/** sprite for the CoinBucket */
	private CoinBucketSprite coinBucket;

	/** left wing currently flapping up? */
	private boolean leftFlapUp = false;
	/** right wing currently flapping up? */
	private boolean rightFlapUp = false;
	/**
	 * current minimum angle of left wing (depends on the length of a swipe
	 * gesture, longer swipe -> longer flap on wing)
	 */
	private float currentLeftAngleMin = -ANGLE_MAX;
	/**
	 * current maximum angle of right wing (depends on the length of a swipe
	 * gesture, longer swipe -> longer flap on wing)
	 */
	private float currentRightAngleMax = ANGLE_MAX;

	/**
	 * Creates the whole Sprite for the Rabbit
	 * @param texture the texture for creating the Sprite
	 */
	public RabbitSprite(Texture texture) {
		super(TextureParts.makeRabbitHead(texture));
		setCenter(CENTER_X, CENTER_Y);

		leftWing = new Sprite(TextureParts.makeWing(texture));
		leftWing.setCenter(leftWing.getWidth(), leftWing.getHeight() / 2);
		leftWing.setPosition(0, -5);
		leftWing.setRotation(ANGLE_MAX);

		rightWing = new Sprite(TextureParts.makeWing(texture).setMirror(Mirror.HORIZONTAL));
		rightWing.setCenter(0, rightWing.getHeight() / 2);
		rightWing.setPosition(0, -5);
		rightWing.setRotation(-ANGLE_MAX);

		coinBucket = new CoinBucketSprite(texture);
		coinBucket.setPosition(0, 20);

		addChild(leftWing);
		addChild(rightWing);
		addChild(coinBucket);

		stars = new Stars(texture);
	}

	/**
	 * @return Sprite for the CoinBucket
	 */
	public CoinBucketSprite getCoinBucket() {
		return coinBucket;
	}

	/**
	 * resets wings to initial (non-flying) state: both wings on top
	 */
	public void resetWings() {
		setWingAngle(-ANGLE_MAX);
		currentLeftAngleMin = -ANGLE_MAX;
		currentRightAngleMax = ANGLE_MAX;
		leftFlapUp = rightFlapUp = false;
	}

	/**
	 * @return true, if both wings are on top, otherwise false
	 */
	public boolean bothWingsOnTop() {
		return leftWing.getRotation() >= ANGLE_MAX && rightWing.getRotation() <= -ANGLE_MAX && !leftFlapUp && !rightFlapUp;
	}

	/**
	 * @return true, if the rabbit is flying (one or both wing(s) not in
	 *         topright position, otherwise false
	 */
	public boolean isFlying() {
		return leftWing.getRotation() < ANGLE_MAX || rightWing.getRotation() > -ANGLE_MAX;
	}

	/**
	 * @return true, if one of the two wings is moving down currently
	 */
	public boolean wingsMovingDown() {
		return (leftWing.getRotation() < ANGLE_MAX && !leftFlapUp) || (rightWing.getRotation() > -ANGLE_MAX && !rightFlapUp);
	}
	
	/**
	 * @return true, if one of the two wings is moving up currently
	 */
	public boolean wingsMovingUp() {
		return (leftWing.getRotation() < ANGLE_MAX && leftFlapUp) || (rightWing.getRotation() > -ANGLE_MAX && rightFlapUp);
	}

	/**
	 * sets the angle of both wings simultaneously
	 * 
	 * @param angle
	 *            the new angle
	 */
	public void setWingAngle(float angle) {
		leftWing.setRotation(-angle);
		rightWing.setRotation(angle);
	}

	/**
	 * sets the current maximum angle of a wing depending on a swipe
	 * 
	 * @param swipe
	 *            if the swipe is in the left half, the max angle of the left
	 *            wing is set, otherwise the one of the right wing
	 */
	public void setCurrentAngleMax(Swipe swipe) {
		if (swipe.isLeft()) {
			this.setCurrentLeftAngleMax(swipe.getLength());
		} else if (swipe.isRight()) {
			this.setCurrentRightAngleMax(swipe.getLength());
		} else {
			this.setCurrentLeftAngleMax(swipe.getLength());
			this.setCurrentRightAngleMax(swipe.getLength());
		}
	}

	/**
	 * sets the current maximum angle for the left wing
	 * @param swipeLength the length of the swipe is used for setting the angle max
	 */
	private void setCurrentLeftAngleMax(float swipeLength) {
		float max = (swipeLength - Swipe.MIN_LENGTH) * ANGLE_QUOTIENT;

		this.currentLeftAngleMin = -max;
	}

	/**
	 * sets the current maximum angle for the right wing
	 * @param swipeLength the length of the swipe is used for setting the angle max
	 */
	private void setCurrentRightAngleMax(float swipeLength) {
		float max = (swipeLength - Swipe.MIN_LENGTH) * ANGLE_QUOTIENT;

		this.currentRightAngleMax = max;
	}

	/**
	 * performs one step of flapping the left wing
	 * 
	 * @param swipeLength
	 *            the length of the swipe (longer swipe -> longer flap)
	 * @return true, if the flap is finished (wing in top-position again (45 degrees)
	 */
	public boolean flapLeftWing(float swipeLength) {
		float newAngle = leftWing.getRotation();

		// currently flapping up or down?
		if (leftFlapUp) {
			newAngle += FLAP_DELTA;
		} else {
			newAngle -= FLAP_DELTA;
		}

		leftWing.setRotation(newAngle);

		// changed direction of flap?
		if (newAngle < currentLeftAngleMin) {
			leftFlapUp = !leftFlapUp;
		} else if (newAngle > ANGLE_MAX) {
			leftFlapUp = !leftFlapUp;
			// flap is finished -> not flying anymore
			return true;
		}

		// still flying
		return false;
	}

	/**
	 * performs one step of flapping the right wing
	 * 
	 * @param swipeLength
	 *            the length of the swipe (longer swipe -> longer flap)
	 * @return true, if the flap is finished (wing in top-position again (45 degrees)
	 */
	public boolean flapRightWing(float swipeLength) {
		float newAngle = rightWing.getRotation();

		if (rightFlapUp) {
			newAngle -= FLAP_DELTA;
		} else {
			newAngle += FLAP_DELTA;
		}

		rightWing.setRotation(newAngle);

		if (newAngle > currentRightAngleMax) {
			rightFlapUp = !rightFlapUp;
		} else if (newAngle < -ANGLE_MAX) {
			rightFlapUp = !rightFlapUp;
			return true;
		}

		return false;
	}

	@Override
	public void setRotation(float angle) {
		super.setRotation(angle);

		/* Simulate gravity for the coin bucket */
		coinBucket.setRotation(-angle / 2);
	}

	/**
	 * rotate the rabbit depending on current swipe gesture
	 * 
	 * @param swipe
	 */
	public void rotate(Swipe swipe) {
		float angleDelta = (swipe.getPower() - Swipe.MIN_LENGTH) / 90.f;

		angleDelta *= MAX_ROTATION / Swipe.MAX_MIN_DELTA_LENGTH;

		if (swipe.isRight()) {
			angleDelta *= -1.f;
		}

		if (Math.abs(this.getRotation() + angleDelta) < MAX_ROTATION) {
			setRotation(this.getRotation() + angleDelta);
		}
	}

	/**
	 * @return the width of the Rabbit-Sprite
	 */
	public float getWidth() {
		return 50.f;
	}

	/**
	 * @return the height of the Rabbit-Sprite
	 */
	public float getHeight() {
		return 60.f;
	}

	/**
	 * check if the rabbit is under a specific position
	 * @param y the position to check against
	 * @return true, if the lower bound of the rabbit is under the specific position
	 */
	public boolean isUnder(float y) {
		return this.getY() + this.getHeight() > y;
	}

	/**
	 * loose a coin of the rabbit
	 */
	public boolean looseCoin() {
		boolean result = coinBucket.looseCoin();

		// change the texture according to number of hits
		switch (coinBucket.getCoinCount()) {
		case 8:
			setTexturePart(TextureParts.makeDamagedRabbitHead(getTexture(), 0));
			setCenter(CENTER_X, CENTER_Y);
			break;
		case 6:
			setTexturePart(TextureParts.makeDamagedRabbitHead(getTexture(), 1));
			setCenter(CENTER_X, CENTER_Y);
			break;
		case 4:
			setTexturePart(TextureParts.makeDamagedRabbitHead(getTexture(), 2));
			setCenter(CENTER_X, CENTER_Y);
			break;
		case 2:
			setTexturePart(TextureParts.makeDamagedRabbitHead(getTexture(), 3));
			setCenter(CENTER_X, CENTER_Y);
			break;
		default:
			/* do nothing */
			break;
		}

		// show the stars when he was hit
		stars.show();

		return result;
	}

	@Override
	protected void onAfterDraw(GL10 gl) {
		super.onAfterDraw(gl);
		stars.draw(gl);
	}

	/**
	 * move the stars by one position
	 */
	public void tick() {
		stars.tick();
	}

	/**
	 * @return the number of left coins
	 */
	public int getCoinCount() {
		return coinBucket.getCoinCount();
	}
}

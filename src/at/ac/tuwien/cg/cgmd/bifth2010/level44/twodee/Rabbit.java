package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.Swipe;

public class Rabbit extends Container {
	public static final float ANGLE_MAX = 45.f;
	
	private static final float FLAP_DELTA = 3.5f;
	private static final float MAX_ROTATION = 35.f;
	private static final float INPUT_DELTA = Swipe.MAX_LENGTH - Swipe.MIN_LENGTH;
	private static final float ANGLE_QUOTIENT = ANGLE_MAX / INPUT_DELTA;

	private Item leftWing;
	private Item rightWing;
	private Item coinBucket;

	private boolean leftFlapUp = false;
	private boolean rightFlapUp = false;

	private float currentLeftAngleMin = -ANGLE_MAX;
	private float currentRightAngleMax = ANGLE_MAX;
	
	private boolean flying = false;

	public Rabbit(Texture texture) {
		super(TextureParts.makeRabbitHead(texture));
		setCenter(84, 64);

		leftWing = new Item(TextureParts.makeWing(texture));
		leftWing.setCenter(128, 64);
		leftWing.setPosition(0, -10);
		leftWing.setRotation(ANGLE_MAX);

		rightWing = new Item(TextureParts.makeWing(texture).setMirror(
				Mirror.HORIZONTAL));
		rightWing.setCenter(0, 64);
		rightWing.setPosition(0, -10);
		rightWing.setRotation(-ANGLE_MAX);

		coinBucket = new CoinBucket(texture);
		coinBucket.setPosition(0, 50);

		addChild(leftWing);
		addChild(rightWing);
		addChild(coinBucket);
	}

	public void resetWings() {
		setWingAngle(-45.1f);
		currentLeftAngleMin = -ANGLE_MAX;
		currentRightAngleMax = ANGLE_MAX;
		leftFlapUp = rightFlapUp = false;
	}
	public void setWingAngle(float angle) {
		leftWing.setRotation(-angle);
		rightWing.setRotation(angle);
	}

	public void setCurrentAngleMax(Swipe swipe) {
		if (swipe.isLeftHalf()) {
			this.setCurrentLeftAngleMax(swipe.getLength());
		} else {
			this.setCurrentRightAngleMax(swipe.getLength());
		}
	}

	private void setCurrentLeftAngleMax(float swipeLength) {
		float max = (swipeLength - Swipe.MIN_LENGTH) * ANGLE_QUOTIENT;

		this.currentLeftAngleMin = -max;
	}

	private void setCurrentRightAngleMax(float swipeLength) {
		float max = (swipeLength - Swipe.MIN_LENGTH) * ANGLE_QUOTIENT;

		this.currentRightAngleMax = max;
	}

	public boolean flapLeftWing(float swipeLength) {
		float newAngle = leftWing.getRotation();

		if (leftFlapUp) {
			newAngle += FLAP_DELTA;
		} else {
			newAngle -= FLAP_DELTA;
		}

		leftWing.setRotation(newAngle);

		if (newAngle < currentLeftAngleMin) {
			leftFlapUp = !leftFlapUp;
		} else if (newAngle > ANGLE_MAX) {
			leftFlapUp = !leftFlapUp;
			setFlying(false);
			return true;
		}

		return false;
	}

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
			setFlying(false);
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

	public void rotate(Swipe swipe) {
			float angleDelta = (swipe.getLength() - Swipe.MIN_LENGTH) / 150.f;

			angleDelta *= MAX_ROTATION / INPUT_DELTA;

			if (swipe.isRightHalf()) {
				angleDelta *= -1.f;
			} 

			if (Math.abs(this.getRotation() + angleDelta) < MAX_ROTATION) {
				setRotation(this.getRotation() + angleDelta);
			}
	}
	
	public float getHeight() {
		return 70.f;
	}

	public void setFlying(boolean flying) {
		this.flying = flying;
	}

	public boolean isFlying() {
		return flying;
	}
	
	public boolean isUnder(float y) {
		return this.getY() + this.getHeight() > y;
	}
}

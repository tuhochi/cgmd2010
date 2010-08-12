package at.ac.tuwien.cg.cgmd.bifth2010.level44.physics;

import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.DoubleTap;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.Swipe;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.sound.SoundPlayer;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.RabbitSprite;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Sprite;

/**
 * This class represents the flying rabbit head
 * 
 * The movement of the rabbit as well as the bucket and
 * the dropping coins are modeled by this object.
 * 
 * @author Matthias Tretter
 * @author Thomas Perl
 * 
 */

public class PhysicalRabbit implements PhysicalObject {
	/** the acceleration of a full wing-flap (vgl. PhysicalObject.GRAVITY) */
	private static final float MAX_FLAP_ACCELERATION = 22.5f;
	/** factor for slowing down movement */
	private static final float VELOCITY_FACTOR = 6000.f;

	/** the sprite showing the rabbit */
	private RabbitSprite sprite = null;
	/** the velocity of the rabbit */
	private float velocity = 0.f;
	/** the time since last reset */
	private long startTime = 0L;
	/** the start time for coin drop */
	private long coinStartTime = 0L;
	/** the time since begin of last wing-flap */
	private long lastWingTime = 0L;
	/** queue of gestures to perform */
	private Queue<InputGesture> inputQueue = new LinkedList<InputGesture>();
	/** width of the screen */
	private int screenWidth;
	/** height of the screen */
	private int screenHeight;
	/** is there currently a coin dropping? */
	private boolean coinDrops = false;
	/** the currently dropping coin */
	private Sprite coinSprite;
	/** check screen boundaries during movement */
	private boolean boundaryCheck = true;;

	/**
	 * Creates the physical object for the Rabbit
	 * 
	 * @param rabbit
	 *            sprite for the rabbit
	 * @param coinSprite
	 *            sprite for a dropping coin
	 * @param screenWidth
	 *            width of the phone's screen
	 * @param screenHeight
	 *            height of the phone's screen
	 */
	public PhysicalRabbit(RabbitSprite rabbit, Sprite coinSprite, int screenWidth, int screenHeight) {
		this.sprite = rabbit;
		this.coinSprite = coinSprite;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	/**
	 * @return the sprite of the rabbit
	 */
	public RabbitSprite getSprite() {
		return sprite;
	}

	/**
	 * let the rabbit fly perform gravitation, movement of flapping wings
	 * 
	 * @param time
	 *            milliseconds bygone since the last flap of wings
	 */
	@Override
	public void move() {
		// time since last reset
		float time = (System.currentTimeMillis() - startTime) / VELOCITY_FACTOR * 2;
		// time since beginning of last wing-flap
		float deltaTimeWingFlap = (System.currentTimeMillis() - this.lastWingTime) / VELOCITY_FACTOR;
		// currently performed input gesture
		InputGesture currentGesture = inputQueue.peek();
		// acceleration of current flap
		float flapAcceleration = MAX_FLAP_ACCELERATION;
		// transform rotation to a real-world angle
		float angle = (float) ((90.f - sprite.getRotation()) * Math.PI / 180.);

		if (currentGesture != null) {
			// staerke des Auftriebs proportional zur laenge des Swipes
			// (Doppeltap = Maximale L�nge)
			float p = currentGesture.getPower() / Swipe.MAX_LENGTH;
			flapAcceleration *= p;
		}

		// if wings moving down, accelerate
		if (sprite.wingsMovingDown()) {
			// v = a * delta t
			this.setVelocity(flapAcceleration * deltaTimeWingFlap);
		} // if wings moving up, inhibit speed
		else if (sprite.wingsMovingUp()) {
			this.setVelocity(this.getVelocity() - 0.05f);
		}

		// s = 1/2 * g * t^2
		float sGravity = (1 / 2.f * PhysicalObject.GRAVITY * time * time);
		// s = v0 * t
		float sMovement = this.getVelocity() * time;
		// sy = s * sin
		float sMovementY = sMovement * (float) Math.sin(Math.abs(angle));
		// sx = s * cos
		float sMovementX = sMovement * (float) Math.cos(angle);
		// new positions
		float newY = sprite.getY() + sGravity - sMovementY;
		float newX = sprite.getX() + sMovementX;

		// set new position, if the position is on the screen
		if (boundaryCheck) {
			newX = Math.min(screenWidth, Math.max(0, newX));
			newY = Math.min(screenHeight - sprite.getHeight(), Math.max(-40, newY));
		}
		setPosition(newX, newY);

		// currently dropping coin?
		if (coinDrops) {
			moveCoin();
		}
		// move coin as the rabbit
		else {
			coinSprite.setPosition(newX, newY + 17);
		}
	}

	/**
	 * drop a coin to the bottom if the rabbit was hit
	 */
	private void moveCoin() {
		if (coinSprite.getY() < screenHeight) {
			// angle is 35 or -35 degrees
			float angle = sprite.getRotation() < 0 ? 35 : -35;
			// compute rad of angle
			angle = (float) ((90.f - angle) * Math.PI / 180.);
			// time since beginning of coindrop
			float time = (System.currentTimeMillis() - coinStartTime) / VELOCITY_FACTOR * 2;
			// s = 1/2 * g * t^2
			float sGravity = (1 / 2.f * PhysicalObject.GRAVITY * time * time);
			float sMovement = 0.25f;
			// sy = s * sin
			float sMovementY = sMovement * (float) Math.sin(Math.abs(angle));
			// sx = s * cos
			float sMovementX = sMovement * (float) Math.cos(angle);
			// new positions
			float newY = coinSprite.getY() + sGravity - sMovementY;
			float newX = coinSprite.getX() + sMovementX;

			coinSprite.setX(newX);
			coinSprite.setY(newY);
		}
		// bottom reached -> stop dropping coin
		else {
			coinDrops = false;
			SoundPlayer.play(SoundPlayer.SoundEffect.DROP, 0.5f);
		}
	}

	/**
	 * after a shot that hit jiggle the rabbit for a short period of time
	 */
	private void jiggle() {
		(new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < 30; i++) {
					if (i % 2 == 0) {
						PhysicalRabbit.this.setPosition(PhysicalRabbit.this.getX() + 6, PhysicalRabbit.this.getY());
					} else {
						PhysicalRabbit.this.setPosition(PhysicalRabbit.this.getX() - 6, PhysicalRabbit.this.getY());
					}

					try {
						Thread.sleep(20L);
					} catch (Exception ex) {
					}
				}
			}
		}).start();
	}

	@Override
	/**
	 * Process the next input gesture
	 * 
	 * @param gesture The Gesture that the user inputs
	 */
	public void processGesture(InputGesture gesture) {
		InputGesture currentGestureToPerform = null;
		// is the current flap of wings finished?
		boolean finished = false;

		if (gesture != null) {
			// append to inputQueue, store only 2 input events
			if (inputQueue.size() < 2)
				inputQueue.add(gesture);
		}

		// get newest input to process
		currentGestureToPerform = inputQueue.peek();

		// is there a input to process?
		if (currentGestureToPerform != null) {
			// if we are at the beginning of a wing-flap, reset the time
			if (sprite.bothWingsOnTop()) {
				// begin 1 second in the past, to accelerate the movement in the
				// beginning (larger time -> larger speed)
				this.resetStartTime(1000);
				this.rememberWingTime();
			}

			if (currentGestureToPerform instanceof Swipe) {
				Swipe swipe = (Swipe) currentGestureToPerform;

				// set the maximum angle for the wings depending on length of
				// swipe
				// longer swipe means longer angle-flip
				sprite.setCurrentAngleMax(swipe);
				// rotate the rabbit depending on which wing is flapped
				if (swipe.isLeft() || swipe.isRight())
					sprite.rotate(swipe);

				// check in which half of the screen the input was detected
				if (swipe.isLeft()) {
					// perform one step of the flap and check if the flap is
					// finished
					// finshed = at top position again (-45/45 �)
					finished = sprite.flapLeftWing(swipe.getPower());

					// current flap finished -> remove from input queue
					if (finished) {
						sprite.resetWings();
						inputQueue.remove();
					}
				} else if (swipe.isRight()) {
					finished = sprite.flapRightWing(swipe.getPower());

					if (finished) {
						sprite.resetWings();
						inputQueue.remove();
					}
				} else {
					// both wings are flapped, finished if one of them has
					// finished
					boolean finishedLeft = sprite.flapLeftWing(Swipe.MAX_LENGTH);
					boolean finishedRight = sprite.flapRightWing(Swipe.MAX_LENGTH);

					finished = finishedLeft || finishedRight;

					if (finished) {
						// set wings back to top position
						sprite.resetWings();
						inputQueue.remove();
					}
				}
			} else if (currentGestureToPerform instanceof DoubleTap) {
				// double tap is equal to max-length middle-swipe
				boolean finishedLeft = sprite.flapLeftWing(Swipe.MAX_LENGTH);
				boolean finishedRight = sprite.flapRightWing(Swipe.MAX_LENGTH);

				finished = finishedLeft || finishedRight;

				if (finished) {
					// set wings back to top position
					sprite.resetWings();
					inputQueue.remove();
				}
			}
		}
	}

	/**
	 * clear all saved input gestures
	 */
	public void clearInputQueue() {
		inputQueue.clear();
	}

	/**
	 * Draw the rabbit onto an OpenGL ES context
	 * 
	 * @param gl The OpenGL ES context on which to draw
	 */
	@Override
	public void draw(GL10 gl) {
		// draw the rabbit-sprite
		if (sprite != null)
			sprite.draw(gl);

		// if a coin is currently dropping, draw it
		if (coinDrops)
			coinSprite.draw(gl);
	}

	/**
	 * set the position of the rabbit
	 * 
	 * @param x The X coordinate of the new position
	 * @param y The Y coordinate of the new position
	 */
	@Override
	public void setPosition(float x, float y) {
		if (sprite != null)
			sprite.setPosition(x, y);
	}

	/**
	 * @return the x-position of the rabbit
	 */
	public float getX() {
		return sprite.getX();
	}

	/**
	 * @return the y-position of the rabbit
	 */
	public float getY() {
		return sprite.getY();
	}

	/**
	 * @return the current velocity of the rabbit
	 */
	public float getVelocity() {
		return velocity;
	}

	/**
	 * only set positive velocity
	 * 
	 * @param velocity The new velocity for this rabbit
	 */
	public void setVelocity(float velocity) {
		if (velocity >= 0.f)
			this.velocity = velocity;
	}

	/**
	 * resets the start time of the movement
	 * 
	 * @param delta
	 *            milliseconds in the past
	 */
	public void resetStartTime(long delta) {
		this.startTime = System.currentTimeMillis() - delta;
	}

	/**
	 * stop user-initiated movement of rabbit, remeber time one second in the
	 * past
	 */
	public void rememberWingTime() {
		this.lastWingTime = System.currentTimeMillis() - 1000;
		this.setVelocity(0.f);
	}

	/**
	 * Count the coints in the bucket
	 * 
	 * @return number of coins left in the bucket
	 */
	public int getCoinCount() {
		return sprite.getCoinCount();
	}

	/**
	 * is called when the rabbit was shot
	 * 
	 * loose a coin, start drawing a dropping coin,
	 * jiggle for a short period of time.
	 */
	public void looseCoin() {
		sprite.looseCoin();

		coinDrops = true;
		coinStartTime = System.currentTimeMillis() - 500;

		jiggle();
	}

	/**
	 * Check if the rabbit is on the ground
	 * 
	 * @return true, if the rabbit has landed at the bottom of the screen
	 */
	public boolean hasLanded() {
		return sprite.isUnder(screenHeight - 5);
	}

	/**
	 * reset all movement-related variables to initial values
	 */
	@Override
	public void resetMovement() {
		setVelocity(0.f);
		getSprite().setRotation(0.f);
		getSprite().setScale(1.f);
		getSprite().resetWings();
		clearInputQueue();
	}

	/**
	 * changes the boundary-check of the rabbit
	 * 
	 * @param check
	 *            the new value for the boundary-check
	 */
	public void setBoundaryCheck(boolean check) {
		this.boundaryCheck = check;
	}
}

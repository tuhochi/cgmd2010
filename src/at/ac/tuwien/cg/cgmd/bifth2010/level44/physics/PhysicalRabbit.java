package at.ac.tuwien.cg.cgmd.bifth2010.level44.physics;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.DoubleTap;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.Swipe;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.sound.SoundPlayer;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.RabbitSprite;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Sprite;

/**
 * This class represents the flying rabbit
 * 
 * @author Matthias
 *
 */

public class PhysicalRabbit implements PhysicalObject {
	/** the acceleration of a full wing-flap (vgl. PhysicalObject.GRAVITY) */
	private static final float MAX_FLAP_ACCELERATION = 22.f;
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

	public PhysicalRabbit(RabbitSprite rabbit, Sprite coinSprite, int screenWidth, int screenHeight) {
		this.sprite = rabbit;
		this.coinSprite = coinSprite;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		coinSprite.setScale(0.4f); //TODO: remove
	}

	public RabbitSprite getSprite() {
		return sprite;
	}

	@Override
	/**
	 * let the rabbit fly
	 * perform gravitation, movement of flapping wings
	 * 
	 * @param time milliseconds bygone since the last flap of wings
	 */
	public void move() {
		// time since last reset
		float time = (System.currentTimeMillis() - startTime) / VELOCITY_FACTOR * 2;
		// time since beginning of last wing-flap
		float deltaTime = (System.currentTimeMillis() - this.lastWingTime) / VELOCITY_FACTOR;
		// currently performed input gesture
		InputGesture currentGesture = inputQueue.peek();
		// acceleration of current flap
		float flapAcceleration = MAX_FLAP_ACCELERATION;
		// transform rotation to a real-world angle
		float angle = (float)((90.f - sprite.getRotation()) * Math.PI/180.);
		
		if (currentGesture != null) {
			// stärke des Auftriebs proportional zur Länge des Swipes (Doppeltap = Maximale Länge)
			float p = currentGesture.getStrength() / Swipe.MAX_LENGTH;
			flapAcceleration *= p;
		}
		
		// if wings moving down, accelerate
		if (sprite.wingsMovingDown()) {
			// v = a * delta t
			this.setVelocity(flapAcceleration * deltaTime);
		} // if wings moving up, inhibit speed
		else if (sprite.wingsMovingUp()) {
			this.setVelocity(this.getVelocity() - 0.05f);	
		}
		
		// s = 1/2 * g * t^2
		float sGravity = (1/2.f * PhysicalObject.GRAVITY * time * time);
		// s = v0 * t
		float sMovement = this.getVelocity() * time;
		// sy = s * sin
		float sMovementY = sMovement * (float)Math.sin(Math.abs(angle));
		// sx = s * cos
		float sMovementX = sMovement * (float)Math.cos(angle);
		// new positions
		float newY = sprite.getY() + sGravity;
		float newX = sprite.getX();
		
		// add movements to position
		newY -= sMovementY;
		newX += sMovementX;
		
		// set new position, if the position is on the screen
		newX = Math.min(screenWidth, Math.max(0,newX));
		newY = Math.min(screenHeight - sprite.getHeight(), Math.max(-40,newY));
		setPosition(newX,newY);
		
		if (coinDrops) {
			moveCoin();
		} else {
			coinSprite.setPosition(newX, newY + 17);
		}
	}

	private void moveCoin() {
		if (coinSprite.getY() < screenHeight) {
			// angle is 35 or -35 degrees
			float angle = sprite.getRotation() < 0 ? 35 : -35;
			// compute rad of angle
			angle = (float)((90.f - angle) * Math.PI/180.);
			// time since last reset
			float time = (System.currentTimeMillis() - coinStartTime) / VELOCITY_FACTOR * 2;
			// s = 1/2 * g * t^2
			float sGravity = (1/2.f * PhysicalObject.GRAVITY * time * time);
			float sMovement = 0.25f;
			// sy = s * sin
			float sMovementY = sMovement * (float)Math.sin(Math.abs(angle));
			// sx = s * cos
			float sMovementX = sMovement * (float)Math.cos(angle);
			// new positions
			float newY = coinSprite.getY() + sGravity;
			float newX = coinSprite.getX();
			
			// add movements to position
			newY -= sMovementY;
			newX += sMovementX;
			
			coinSprite.setX(newX);
			coinSprite.setY(newY);
		} else {
			coinDrops = false;
			SoundPlayer.getInstance(null).play(SoundPlayer.SoundEffect.DROP, 0.5f);
		}
	}

	@Override
	/**
	 * Process the next input gesture
	 * 
	 * @gesture The Gesture that the user inputs
	 */
	public void processGesture(InputGesture gesture) {
		InputGesture currentGestureToPerform = null;
		// is the current flap of wings finished?
		boolean finished = false;

		if (gesture != null) {
			// append to inputQueue
			if (inputQueue.size() < 2)
				inputQueue.add(gesture);
		}

		// get newest input to process
		currentGestureToPerform = inputQueue.peek();

		// is there a input to process, is it a swipe?
		if (currentGestureToPerform != null) {
			// if we are at the beginning of a wing-flap, reset the time
			if (sprite.bothWingsOnTop()) {
				this.resetStartTime(1000);
				this.rememberWingTime();
			}
			
			if (currentGestureToPerform instanceof Swipe) {
				Swipe swipe = (Swipe) currentGestureToPerform;

				// set the maximum angle for the wings depending on length of swipe
				// longer swipe means longer angle-flip
				sprite.setCurrentAngleMax(swipe);
				// rotate the rabbit depending on which wing is flapped
				if (swipe.isLeft() || swipe.isRight()) 
					sprite.rotate(swipe);

				// check in which half of the screen the input was detected
				if (swipe.isLeft()) {
					// perform one step of the flap and check if the flap is finished
					// finshed = at top position again (-45/45 °)
					 finished = sprite.flapLeftWing(swipe.getStrength());

					// current flap finished -> remove from input queue
					if (finished) {
						sprite.resetWings();
						inputQueue.remove();
					}
				} else if (swipe.isRight()) {
					finished = sprite.flapRightWing(swipe.getStrength());

					if (finished) {
						sprite.resetWings();
						inputQueue.remove();
					}
				} else {
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
				//DoubleTap tap = (DoubleTap)currentGestureToPerform;
				
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

	@Override
	public void draw(GL10 gl) {
		if (sprite != null)
			sprite.draw(gl);
		
		if (coinDrops)
			coinSprite.draw(gl);
	}

	@Override
	public void setPosition(float x, float y) {
		if (sprite != null)
			sprite.setPosition(x, y);
	}
	
	public float getX() {
		return sprite.getX();
	}
	
	public float getY() {
		return sprite.getY();
	}
	
	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		if (velocity >= 0.f)
			this.velocity = velocity;
	}

	public void resetStartTime(long delta) {
		this.startTime = System.currentTimeMillis() - delta;
	}
	
	public void rememberWingTime() {
		this.lastWingTime = System.currentTimeMillis() - 1000;
		this.setVelocity(0.f);
	}
	
	public int getCoinCount() {
		return sprite.getCoinCount();
	}
	
	public void looseCoin() {
		sprite.looseCoin();
		
		coinDrops = true;
		coinStartTime = System.currentTimeMillis() - 500;
	}
	
	public boolean hasLanded() {
		return sprite.isUnder(screenHeight - 5);
	}
}

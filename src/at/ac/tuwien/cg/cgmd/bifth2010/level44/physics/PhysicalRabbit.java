package at.ac.tuwien.cg.cgmd.bifth2010.level44.physics;

import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.DoubleTap;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.Swipe;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.RabbitSprite;

/**
 * This class represents the flying rabbit
 * 
 * @author Matthias
 *
 */

public class PhysicalRabbit implements PhysicalObject {
	/** the acceleration of a full wing-flap (vgl. PhysicalObject.GRAVITY) */
	private static final float MAX_FLAP_ACCELERATION = 22.f;
	/** the sprite showing the rabbit */
	private RabbitSprite sprite = null;
	/** queue of gestures to perform */
	private Queue<InputGesture> inputQueue = new LinkedList<InputGesture>();
	/** width of the screen */
	private int screenWidth;
	/** height of the screen */
	private int screenHeight;

	public PhysicalRabbit(RabbitSprite rabbit, int screenWidth, int screenHeight) {
		this.sprite = rabbit;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
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
	public void move(long time) {
		// currently performed input gesture
		InputGesture currentGesture = inputQueue.peek();
		// factor to slow down movement
		float factor = 5000.f;
		// acceleration of current flap
		float flapAcceleration = MAX_FLAP_ACCELERATION;
		
		if (currentGesture != null) {
			// stŠrke des Auftriebs proportional zur LŠnge des Swipes (Doppeltap = Maximale LŠnge)
			float p = currentGesture.getStrength() / Swipe.MAX_LENGTH;
			
			flapAcceleration *= p;
		}
	
		// transform rotation to a real-world angle
		float angle = (float)((90.f - sprite.getRotation()) * Math.PI/180.);
		// s = 1/2 * g * t^2
		float sGravity = (1/2.f * PhysicalObject.GRAVITY * time * time) / factor;
		// s = 1/2 * a * t^2
		float sMovement = (1/2.f * flapAcceleration * time * time) / factor;
		// sy = s * sin
		float sMovementY = sMovement * (float)Math.sin(Math.abs(angle));
		// sx = s * cos
		float sMovementX = sMovement * (float)Math.cos(angle);
		// new positions
		float newY = sprite.getY() + sGravity;
		float newX = sprite.getX();
		
		// currently flying (flapping a wing) ? -> add movements to new Position
		if (sprite.isFlying()) {
			newY -= sMovementY;
			newX += sMovementX;
		}
		
		// set new position, if the position is on the screen
		sprite.setPosition(Math.min(screenWidth - sprite.getWidth(),Math.max(0,newX)), Math.min(newY, screenHeight - sprite.getHeight()));
	}

	@Override
	/**
	 * Process the next input gesture
	 * 
	 * @gesture The Gesture that the user inputs
	 */
	public boolean processGesture(InputGesture gesture) {
		InputGesture currentGestureToPerform = null;
		// is the current flap of wings finished?
		boolean finished = false;

		if (gesture != null) {
			// append to inputQueue
			inputQueue.add(gesture);
		}

		// get newest input to process
		currentGestureToPerform = inputQueue.peek();

		// is there a input to process, is it a swipe?
		if (currentGestureToPerform != null) {
			if (currentGestureToPerform instanceof Swipe) {
				Swipe swipe = (Swipe) currentGestureToPerform;

				// set the maximum angle for the wings depending on length of swipe
				// longer swipe means longer angle-flip
				sprite.setCurrentAngleMax(swipe);
				// rotate the rabbit depending on which wing is flapped
				sprite.rotate(swipe);
				// if current gesture is a swipe, the rabbit is flying
				sprite.setFlying(true);

				// check in which half of the screen the input was detected
				if (swipe.isLeftHalf()) {
					// perform one step of the flap and check if the flap is finished
					// finshed = at top position again (-45/45 ¡)
					 finished = sprite.flapLeftWing(swipe.getStrength());

					// current flap finished -> remove from input queue
					if (finished) {
						inputQueue.remove();
					}
				} else {
					finished = sprite.flapRightWing(swipe.getStrength());

					if (finished) {
						inputQueue.remove();
					}
				}
			} else if (currentGestureToPerform instanceof DoubleTap) {
				DoubleTap tap = (DoubleTap)currentGestureToPerform;
				
				// current gesture is a doubleTap, so the rabbit is flying
				sprite.setFlying(true);
				
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
		
		return finished;
	}

	@Override
	public void draw(GL10 gl) {
		if (sprite != null)
			sprite.draw(gl);
	}

	@Override
	public void setPosition(float x, float y) {
		if (sprite != null)
			sprite.setPosition(x, y);
	}
}

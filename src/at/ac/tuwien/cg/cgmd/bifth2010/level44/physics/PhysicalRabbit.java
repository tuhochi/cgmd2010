package at.ac.tuwien.cg.cgmd.bifth2010.level44.physics;

import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.DoubleTap;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.Swipe;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Rabbit;

public class PhysicalRabbit implements PhysicalObject {
	private Rabbit rabbit = null;
	private Queue<InputGesture> inputQueue = new LinkedList<InputGesture>();
	private int screenWidth;
	private int screenHeight;

	public PhysicalRabbit(Rabbit rabbit, int screenWidth, int screenHeight) {
		this.rabbit = rabbit;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	public Rabbit getRabbit() {
		return rabbit;
	}

	@Override
	public void move(long time) {
		// v(t) - gt 
		// s = v * t
		float factor = 2000.f;
		float sGravity = (1/2.f * PhysicalObject.GRAVITY * time * time) / factor;
		float sMovement = (1/2.f * 15.f * time * time) / factor; //5000.f;
		float newY = rabbit.getY() + sGravity;
		
		if (rabbit.isFlying()) {
			newY -= sMovement;
		}
		
		
		if ((rabbit.getY() + rabbit.getHeight()) <= screenHeight) {
			rabbit.setPosition(rabbit.getX(), newY);
		}
	}

	@Override
	/**
	 * Process the next input gesture
	 * 
	 * @gesture The Gesture that the user inputs, normally a Swipe (Flick)
	 */
	public boolean processGesture(InputGesture gesture) {
		InputGesture currentGestureToPerform = null;
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
				rabbit.setCurrentAngleMax(swipe);
				rabbit.rotate(swipe);
				rabbit.setFlying(true);

				// check in which half of the screen the input was detected
				if (swipe.isLeftHalf()) {
					// perform one step of the flap and check if the flap is finished
					// finshed = at top position again (-45/45 ¡)
					 finished = rabbit.flapLeftWing(swipe.getLength());

					// current flap finished -> remove from input queue
					if (finished) {
						inputQueue.remove();
					}
				} else {
					finished = rabbit.flapRightWing(swipe.getLength());

					if (finished) {
						inputQueue.remove();
					}
				}
			} else if (currentGestureToPerform instanceof DoubleTap) {
				DoubleTap tap = (DoubleTap)currentGestureToPerform;
				
				rabbit.setFlying(true);
				
				boolean finishedLeft = rabbit.flapLeftWing(Swipe.MAX_LENGTH);
				boolean finishedRight = rabbit.flapRightWing(Swipe.MAX_LENGTH);
				
				finished = finishedLeft || finishedRight;
				
				if (finished) {
					rabbit.resetWings();
					inputQueue.remove();
				}
			}
		}
		
		return finished;
	}

	@Override
	public void draw(GL10 gl) {
		if (rabbit != null)
			rabbit.draw(gl);
	}

	@Override
	public void setPosition(float x, float y) {
		rabbit.setPosition(x, y);
	}
}

package at.ac.tuwien.cg.cgmd.bifth2010.level44.physics;

import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.Swipe;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Rabbit;

public class PhysicalRabbit implements PhysicalObject {
	private Rabbit rabbit = null;
	private Queue<InputGesture> inputQueue = new LinkedList<InputGesture>();

	public PhysicalRabbit(Rabbit rabbit) {
		this.rabbit = rabbit;
	}

	public Rabbit getRabbit() {
		return rabbit;
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
	}

	@Override
	/**
	 * Process the next input gesture
	 * 
	 * @gesture The Gesture that the user inputs, normally a Swipe (Flick)
	 */
	public void processGesture(InputGesture gesture) {
		InputGesture currentGestureToPerform = null;

		if (gesture != null) {
			// append to inputQueue
			inputQueue.add(gesture);
		}

		// get newest input to process
		currentGestureToPerform = inputQueue.peek();

		// is there a input to process, is it a swipe?
		if (currentGestureToPerform != null && currentGestureToPerform instanceof Swipe) {
			Swipe swipe = (Swipe) currentGestureToPerform;
			
			// set the maximum angle for the wings depending on length of swipe
			// longer swipe means longer angle-flip
			rabbit.setCurrentAngleMax(swipe);

			// check in which half of the screen the input was detected
			if (swipe.isLeftHalf()) {
				// perform one step of the flap and check if the flap is finished
				// finshed = at top position again (-45/45 ¡)
				boolean finished = rabbit.flapLeftWing(swipe.getLength());

				// current flap finished -> remove from input queue
				if (finished) {
					inputQueue.remove();
				}
			} else {
				boolean finished = rabbit.flapRightWing(swipe.getLength());

				if (finished) {
					inputQueue.remove();
				}
			}
		}
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

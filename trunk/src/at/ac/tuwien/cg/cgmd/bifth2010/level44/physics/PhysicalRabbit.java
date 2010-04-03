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
	public void processGesture(InputGesture gesture) {
		InputGesture currentGestureToPerform = null;

		if (gesture != null) {
			inputQueue.add(gesture);
		}

		currentGestureToPerform = inputQueue.peek();

		if (currentGestureToPerform != null && currentGestureToPerform instanceof Swipe) {
			Swipe swipe = (Swipe) currentGestureToPerform;

			if (swipe.isLeftHalf()) {
				boolean finished = rabbit.flapLeftWing(swipe.getLength());

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

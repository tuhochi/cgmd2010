package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import android.util.Log;
import android.view.KeyEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.Geometry;

public class UpdateTask implements Runnable {

	// ----------------------------------------------------------------------------------
	// -- Static ----
	
	private static final int FRAMES_PER_SECOND = 10;
	private static final int FRAME_DT   = 1000 / FRAMES_PER_SECOND;
	
	
	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	private LinkedList<KeyEvent> inputs; //< all input events
	private boolean isRunning;   //< true if the game is running
	private GameScene scene;     //< Game scene
	private boolean isLeft;      //< Left key is pressed
	private boolean isRight;     //< Right key is pressed
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Ctor.
	 * @param The game scene.
	 */
	public UpdateTask(GameScene scene) {
		this.scene = scene;
		inputs = new LinkedList<KeyEvent>();
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Main update loop.
	 */
	@Override
	public void run() {
		isRunning = true;
		
		float dt = 0.0f;
		long  milTime = 0;
		long  endTime = 0;
		long  begTime = System.nanoTime();
		while (isRunning) {
			
			synchronized(inputs) {
				doInput();
				inputs.notify();
			}
			
			synchronized(scene) {
				update(dt);
				scene.notify();
			}
			
			endTime = System.nanoTime();
			milTime = (endTime - begTime);
			milTime /= 1000000;
			begTime = endTime;
			dt      = milTime / 1000.0f;
			try {
				if (FRAME_DT > milTime) {
					Log.i("UpdateTask", "wait second");
					Thread.sleep(FRAME_DT - milTime);
				}
			}
			catch (InterruptedException e) {
				Log.e("UpdateTask", e.getMessage());
			}
		}
	}
	
	
	/**
	 * Return list with all input events.
	 * @return List with input events.
	 */
	public LinkedList<KeyEvent> getInputs() {
		return inputs;
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Private methods ----

	/**
	 * Process input.
	 */
	private void doInput() {
		while (!inputs.isEmpty()) {
			KeyEvent key = inputs.remove();
			int keycode       = key.getKeyCode();
			boolean isPressed = key.getAction() == KeyEvent.ACTION_DOWN;
			if (keycode == KeyEvent.KEYCODE_A) {
				isLeft = isPressed;
			}
			else if (keycode == KeyEvent.KEYCODE_D) {
				isRight = isPressed;
			}
		}
	}
	
	
	/**
	 * Update.
	 * @param dt Delta time
	 */
	private void update(float dt) {
		
		ArrayList<Geometry> geoms = scene.getGeometry();
		for (Geometry it : geoms) {
			if (isLeft) {
				it.pos[0] -= 0.1;
			}
			if (isRight) {
				it.pos[0] += 0.1;
			}
			//it.pos[1] += 0.1;
			if (it.pos[1] > 1.0) {
				it.pos[1] = -1.0f;
			}
		}
	}
}

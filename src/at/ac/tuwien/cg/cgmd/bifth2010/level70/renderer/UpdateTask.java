package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import java.util.LinkedList;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Update task.
 */
public class UpdateTask implements Runnable {

	// ----------------------------------------------------------------------------------
	// -- Static ----
	
	private static final int FRAMES_PER_SECOND = 10;
	private static final int FRAME_DT   = 1000 / FRAMES_PER_SECOND;
	
	
	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	private LinkedList<KeyEvent>    inputKeys; //< all key input events
	private LinkedList<MotionEvent> inputMotions; //< all motion input events
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
		inputKeys = new LinkedList<KeyEvent>();
		inputMotions = new LinkedList<MotionEvent>();
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Main update loop.
	 */
	@Override
	public void run() {
		try {
			synchronized(scene) {
				Log.i("UpdateTask", "before wait");
				scene.wait();
			}
		}
		catch(InterruptedException e) {
			
		}
		Log.i("UpdateTask", "after wait");
		isRunning = true;
		
		float dt = 0.0f;
		long  milTime = 0;
		long  endTime = 0;
		long  begTime = System.nanoTime();
		while (isRunning) {
			
			synchronized(scene) {
				doInput();
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
	 * Return list with all key input events.
	 * @return List with input events.
	 */
	public LinkedList<KeyEvent> getInputKeys() {
		return inputKeys;
	}
	
	
	/**
	 * Return list with all motion input events.
	 * @return List with input events.
	 */
	public LinkedList<MotionEvent> getInputMotions() {
		return inputMotions;
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Private methods ----

	/**
	 * Process input.
	 */
	private void doInput() {
		while (!inputKeys.isEmpty()) {
			KeyEvent key = inputKeys.remove();
			int keycode       = key.getKeyCode();
			boolean isPressed = key.getAction() == KeyEvent.ACTION_DOWN;
			if (keycode == KeyEvent.KEYCODE_A) {
				isLeft = isPressed;
			}
			else if (keycode == KeyEvent.KEYCODE_D) {
				isRight = isPressed;
			}
		}
		
		while (!inputMotions.isEmpty()) {
			MotionEvent inp = inputMotions.remove();
			scene.onClick(inp);
		}
	}
	
	
	/**
	 * Update.
	 * @param dt Delta time
	 */
	private void update(float dt) {
		
		scene.update(dt);
	}
}

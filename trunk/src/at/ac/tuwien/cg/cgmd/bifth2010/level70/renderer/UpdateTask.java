package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import java.util.LinkedList;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.traingame.TrainGame;

/**
 * Update task.
 */
public class UpdateTask extends Thread {

	// ----------------------------------------------------------------------------------
	// -- Static ----
	
	private static final int FRAMES_PER_SECOND = 60;
	private static final int FRAME_DT   = 1000 / FRAMES_PER_SECOND;
	
	
	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	private LinkedList<KeyEvent>    inputKeys; //< all key input events
	private LinkedList<MotionEvent> inputMotions; //< all motion input events
	public boolean isRunning; //< True if the main loop is running
	public boolean isPause;   //< True if the game is paused
	private TrainGame game;  //< Game scene
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Ctor.
	 * @param The game scene.
	 */
	public UpdateTask(TrainGame game) {
		this.game = game;
		inputKeys = new LinkedList<KeyEvent>();
		inputMotions = new LinkedList<MotionEvent>();
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Main update loop. Is called FRAME_PER_SECONDS time in one second.
	 * The RenderTask is synchronized with the UpdateTaks over the GameScene instance.
	 */
	@Override
	public void run() {
		
		isRunning = true;
		float dt = 0.0f;
		float totalDt = 0.0f;
		int   fpsCnt  = 0;
		long  milTime = 0;
		long  endTime = 0;
		long  begTime = System.nanoTime();
		while (isRunning) {
			
			synchronized(game) {
				if (!isPause) {
					doInput();
					update(dt);
					game.notify();
				}
			}
			
			endTime = System.nanoTime();
			milTime = (endTime - begTime);
			milTime /= 1000000;
			begTime = endTime;
			dt      = milTime / 1000.0f;
			totalDt += dt;
			fpsCnt++;
			if (totalDt > 1.0) {
				Log.i("FPS: ", Integer.toString(fpsCnt));
				totalDt = 0;
				fpsCnt  = 0;
			}

			try {
				if (FRAME_DT > milTime) {
					Thread.sleep(FRAME_DT - milTime);
				}
			}
			catch (InterruptedException e) {
				Log.e("UpdateTask", e.getMessage());
			}
		}
	}
	
	/**
	 * Set the running flag of the main update loop.
	 * @param flag true to run the main loop, false to quit the main loop.
	 */
	public void setRunningFlag(boolean flag) {
		this.isRunning = flag;
	}
	
	
	/**
	 * Set the pause flag of the main update loop. No game scene update will happen.
	 * @param flag true to pause the game, otherwise false.
	 */
	public void setPauseFlag(boolean flag) {
		this.isPause = flag;
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
//		while (!inputKeys.isEmpty()) {
//			KeyEvent key = inputKeys.remove();
//			int keycode       = key.getKeyCode();
//			boolean isPressed = key.getAction() == KeyEvent.ACTION_DOWN;
//			if (keycode == KeyEvent.KEYCODE_A) {
//				
//			}
//			else if (keycode == KeyEvent.KEYCODE_D) {
//				
//			}
//		}
		
		while (!inputMotions.isEmpty()) {
			MotionEvent inp = inputMotions.remove();
			game.onMotionEvent(inp);
		}
	}
	
	
	/**
	 * Update.
	 * @param dt Delta time
	 */
	private void update(float dt) {
		//Log.i("UpdateTask", "update");
		game.update(dt);
	}
}

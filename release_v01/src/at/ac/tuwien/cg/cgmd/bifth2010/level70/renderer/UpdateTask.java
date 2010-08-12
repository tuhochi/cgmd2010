package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import java.util.LinkedList;

import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.game.TrainGame;

/**
 * Update task.
 * 
 * @author Christoph Winklhofer
 */
public class UpdateTask extends Thread {

	// ----------------------------------------------------------------------------------
	// -- Static ----
	
    /** Update rate per seconds */
	private static final int FRAMES_PER_SECOND = 60;
	
	/** Frame time */
	private static final int FRAME_DT   = 1000 / FRAMES_PER_SECOND;
	
	
	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	/** List with user input - motion events */
	private LinkedList<MotionEvent> inputMotions;
	
	/** Is the main loop running */
	public boolean isRunning;
	
	/** Is the game in pause state */
	public boolean isPause;
	
	/** Instance to the train game */
	private TrainGame game;
		
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Ctor.
	 * @param game The train game.
	 */
	public UpdateTask(TrainGame game) {
		this.game = game;
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
		long  milTime = 0;
		long  endTime = 0;
		long  begTime = System.nanoTime();
		while (isRunning) {
			
			synchronized(game) {
			    doInput();
				if (!isPause) {
					update(dt);
				}
				game.notify();
			}
			
			endTime = System.nanoTime();
			milTime = (endTime - begTime);
			milTime /= 1000000;
			begTime = endTime;
			dt      = milTime / 1000.0f;
			
			try {
				if (FRAME_DT > milTime) {
					Thread.sleep(FRAME_DT - milTime);
				}
			}
			catch (InterruptedException e) {
				
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
	 * Return list with all motion input events.
	 * @return List with input events.
	 */
	public LinkedList<MotionEvent> getInputMotions() {
		return inputMotions;
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Private methods ----

	/**
	 * Process ever user input event. Inform the game train instance.
	 */
	private void doInput() {
		
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
		game.update(dt);
	}
}

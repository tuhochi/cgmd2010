package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * LevelSurfaceView class for level 60. An implementation of SurfaceView that uses the
 * dedicated surface for displaying OpenGL rendering. This class extends GLSurfaceView.
 * 
 * @author      Martin Schenk
 * @author      Tiare Feuchtner
 */
public class LevelSurfaceView extends GLSurfaceView {
	LevelRenderer lr;
	private int score = 0;
	private SessionState s;
	
	/**
	 * Constructor for LevelSurfaceView which initializes the renderer and 
	 * enables input by touchscreen.
	 * 
	 * @param context Interface to global information about the application environment.
	 * @param msavedstate State of the Activity
	 */
	public LevelSurfaceView(Context context, Bundle msavedstate) {
		super(context);
		lr = new LevelRenderer(context, msavedstate, this);
        setRenderer(lr);
        setFocusableInTouchMode(true);
	}
	
	/**
	 * Handles input from touch events and passes them on to the renderer.
	 * 
	 * @return true on touch event
	 */
	public boolean onTouchEvent(MotionEvent event) {
		lr.onTouch(event);
		return true;
	}
	
	/**
	 * Returns the current game state
	 * @return score
	 */
	public SessionState getState() {
		score = lr.getScore();
		s = new SessionState();
		s.setProgress(100-score);
		return s;
	}
	
	/**
	 * Called when the game is interrupted and its state needs to be saved. The actual process takes
	 * place in the LevelRenderer class.
	 * 
	 * @param outState Bundle containing the current game state
	 */
	public void onSaveInstanceState(Bundle outState) {
		lr.onSaveInstanceState(outState);
	}
}

package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class LevelSurfaceView extends GLSurfaceView {
	LevelRenderer lr;
	private int score = 0;
	private SessionState s;
	
	public LevelSurfaceView(Context context, Bundle msavedstate) {
		super(context);
		lr = new LevelRenderer(context, msavedstate, this);
        setRenderer(lr);
        setFocusableInTouchMode(true);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("LevelSurfaceView", "In onTouchEvent");
		lr.onTouch(event);
		return true;
	}
	
	public SessionState getState() {
		score = lr.getScore();
		s = new SessionState();
		s.setProgress(100-score);
		return s;
	}
	
	public void onSaveInstanceState(Bundle outState) {
		lr.onSaveInstanceState(outState);
	}
}

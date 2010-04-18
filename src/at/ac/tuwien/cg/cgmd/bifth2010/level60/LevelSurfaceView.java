package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class LevelSurfaceView extends GLSurfaceView {
	public LevelSurfaceView(Context context) {
		super(context);
		lr = new LevelRenderer(context);
        setRenderer(lr);
        setFocusableInTouchMode(true);
	}
	
	LevelRenderer lr;
	private int score = 10;
	private SessionState s;
	
	public boolean onTouchEvent(MotionEvent event) {
		s = getState();
		s.setProgress(score);  //set progress to score!
		//setResult(Activity.RESULT_OK, s.asIntent());
		//finish();
		return true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			lr.moveObject(-5, 0);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			lr.moveObject(5, 0);
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			lr.moveObject(0, 5);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			lr.moveObject(0, -5);
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public SessionState pause () {
		return getState();
	}
	
	public void resume () {
		
	}
	
	public SessionState getState() {
		s = new SessionState();
		s.setProgress(score);
		return s;
	}
}

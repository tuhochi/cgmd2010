package at.ac.tuwien.cg.cgmd.bifth2010.level60;

//import android.app.Activity;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class LevelSurfaceView extends GLSurfaceView {
	public LevelSurfaceView(Context context) {
		super(context);
		lr = new LevelRenderer(context);
		//String strExtensions = gl.glGetString(GL10.GL_EXTENSIONS);
//		Log.d(CLASS_TAG, "made level renderer");
        setRenderer(lr);
//        Log.d(CLASS_TAG, "set level renderer");
        setFocusableInTouchMode(true);
	}
	
	LevelRenderer lr;
	private int score = 10;
	private SessionState s;
	private static final String CLASS_TAG = LevelSurfaceView.class.getName();
	
	public boolean onTouchEvent(MotionEvent event) {
		s = getState();
		s.setProgress(score);  //set progress to score!
		//setResult(Activity.RESULT_OK, s.asIntent());
		//finish();
		return true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
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

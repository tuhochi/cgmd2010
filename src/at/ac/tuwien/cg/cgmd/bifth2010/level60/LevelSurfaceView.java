package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class LevelSurfaceView extends GLSurfaceView {
	LevelRenderer lr;
	private int score = 10;
	private SessionState s;
	
	public LevelSurfaceView(Context context, Bundle msavedstate) {
		super(context);
		lr = new LevelRenderer(context, msavedstate);
        setRenderer(lr);
        setFocusableInTouchMode(true);
	}
	
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
	
	/*public SessionState saveData (SharedPreferences prefs) {
		//save bunny pos & action map
        SharedPreferences.Editor prefEditor = prefs.edit();
        //prefEditor.putInt("l60_score", score);
		//lr.saveLevel(prefEditor);
		prefEditor.commit();
		return getState();
	}
	
	public void updateData (SharedPreferences prefs) {
		//lr.loadLevel(prefs);
		score = prefs.getInt("l60_score", 100);
	}
	*/
	public SessionState getState() {
		s = new SessionState();
		s.setProgress(100-score);
		return s;
	}
	
	public void onSaveInstanceState(Bundle outState) {
		lr.onSaveInstanceState(outState);
	}
}

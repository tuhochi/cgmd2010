package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class LevelSurfaceView extends GLSurfaceView {
	LevelRenderer lr;
	private int score = 0;
	private SessionState s;
	
	public LevelSurfaceView(Context context, Bundle msavedstate) {
		super(context);
		lr = new LevelRenderer(context, msavedstate);
        setRenderer(lr);
        setFocusableInTouchMode(true);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
		Log.d("LevelSurfaceView", "In onTouchEvent");
		lr.onTouch(event);
//		score = lr.getScore();
//		s = getState();
//		s.setProgress(score);  //set progress to score!
		//setResult(Activity.RESULT_OK, s.asIntent());
		//finish();
		return true;
	}
	
//	public boolean onKeyDown(int keyCode, KeyEvent event) { 
//		if (event.getAction() == KeyEvent.ACTION_DOWN) {
//			switch (keyCode) {
//			case KeyEvent.KEYCODE_DPAD_LEFT:
//				lr.setKey(0);
//				break;
//			case KeyEvent.KEYCODE_DPAD_RIGHT:
//				lr.setKey(1);
//				break;
//			case KeyEvent.KEYCODE_DPAD_UP:
//				lr.setKey(2);
//				break;
//			case KeyEvent.KEYCODE_DPAD_DOWN:
//				lr.setKey(3);
//				break;
//			case KeyEvent.KEYCODE_DPAD_CENTER:
//				lr.performAction();
//				break;
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				lr.releaseKey(0);
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				lr.releaseKey(1);
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				lr.releaseKey(2);
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				lr.releaseKey(3);
				break;
			}
		}
		return super.onKeyDown(keyCode, event);
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

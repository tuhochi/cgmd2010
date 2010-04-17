package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.MyRenderer;

public class LevelActivity extends Activity {
	GLSurfaceView glv;
	LevelRenderer lr;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		glv = new GLSurfaceView(this);
		lr = new LevelRenderer(this);
		glv.setRenderer(lr);
		setContentView(glv);
		return;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		SessionState s = new SessionState();
		s.setProgress(1);  //set progress to score!
		setResult(Activity.RESULT_OK, s.asIntent());
		finish();
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
}

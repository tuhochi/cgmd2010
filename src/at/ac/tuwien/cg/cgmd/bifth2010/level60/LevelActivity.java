package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class LevelActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		GLSurfaceView glv = new GLSurfaceView(this);
		glv.setRenderer(new LevelRenderer(this));
		setContentView(glv);
		return;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		SessionState s = new SessionState();
		s.setProgress(1);
		setResult(Activity.RESULT_OK, s.asIntent());
		finish();
		return true;
	}
}

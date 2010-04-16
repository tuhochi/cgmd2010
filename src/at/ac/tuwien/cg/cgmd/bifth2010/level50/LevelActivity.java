package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class LevelActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)

        mGLView = new LevelSurfaceView(this);
   		setContentView(mGLView);
    }
	
/*	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (true)//(new GestureDetector()).onTouchEvent(event))
		{
		    SessionState s = new SessionState();
			s.setProgress(30);
			setResult(Activity.RESULT_OK, s.asIntent());
			finish();
			return true;
		} else
	    	return false;
	}*/
	
	

    @Override
    public void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    private LevelSurfaceView mGLView;

}

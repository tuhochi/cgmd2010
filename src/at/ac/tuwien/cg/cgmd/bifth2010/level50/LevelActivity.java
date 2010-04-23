package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import android.app.Activity;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class LevelActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        SessionState s = new SessionState();
		s.setProgress(0);
		setResult(Activity.RESULT_OK, s.asIntent());

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
				

    @Override
    public void onPause() {
        super.onPause();
        mGLView.onPause();
        int score = mGLView.getScore();
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("L50_SCORE", score);
        ed.putFloat("L50_POSX", mGLView.getPositionX());
        ed.putFloat("L50_POSY", mGLView.getPositionY());
        ed.commit();
        
        SessionState s = new SessionState();
		s.setProgress(score);
		setResult(Activity.RESULT_OK, s.asIntent());
    }

    @Override
    public void onResume() {
        super.onResume();
        mGLView.onResume();
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        mGLView.setScore(mPrefs.getInt("L50_SCORE", 0));
        mGLView.setPosition(mPrefs.getFloat("L50_POSX", 0.0f), mPrefs.getFloat("L50_POSY", 0.0f));
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.clear();
        ed.commit();
        
        SessionState s = new SessionState();
		s.setProgress(30);
		setResult(Activity.RESULT_OK, s.asIntent());
        
    }

    private LevelSurfaceView mGLView;

}

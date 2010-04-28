package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level00.TestLevelActivity;

public class LevelActivity extends Activity{
	private Display mDisplay;
	private GLView mGLView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();    
	    mGLView = new GLView( this, mDisplay.getWidth(), mDisplay.getHeight() );
        setContentView( mGLView );
    }
    
    @Override
    public boolean onTouchEvent (MotionEvent event)
    {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			mGLView.setXYpos(event.getX(), (mDisplay.getHeight() - event.getY()));
			//Log.d(LOG_TAG, "width: " + d.getWidth() + " height: " + d.getHeight() + " standard y: " + event.getY());
            //Log.d(LOG_TAG, "screen --> x: " + (event.getX()) + "y: " + (d.getHeight() - event.getY()));
        }
    	return super.onTouchEvent(event);
    }
    
    
    /*
    @Override
    protected void onResume() {
    	glview.resumeLevel();
        super.onResume();
    }
*/
    @Override
    protected void onPause() {
    	mGLView.pauseLevel();
        super.onPause();
    }
    
    @Override
	protected void onStop() {		
		//we finish this activity
    	mGLView.stopLevel();
		this.finish();
		super.onStop();
    }
    
	@Override
	public void finish() {
    	SessionState s = new SessionState();
    	//we set the progress the user has made (must be between 0-100)
    	s.setProgress( GameMechanics.getGameMecanics().getMoney() );
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
		super.finish();
	}

    
}

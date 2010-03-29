package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class LevelActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	     requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mGLSurfaceView = new MyOpenGLView(this);
        mGLSurfaceView.setRenderer(new Renderer());        
        setContentView(mGLSurfaceView);
        
        
       SessionState s = new SessionState();
		//we set the progress the user has made (must be between 0-100)
		s.setProgress(10);
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
	}
	
	
	@Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mGLSurfaceView.onPause();
    }
	
	private MyOpenGLView mGLSurfaceView;
}

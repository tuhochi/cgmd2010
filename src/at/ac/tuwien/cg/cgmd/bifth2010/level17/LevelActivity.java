package at.ac.tuwien.cg.cgmd.bifth2010.level17;


import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLView;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;

/**
 * Wrapper activity demonstrating the use of {@link GLSurfaceView}, a view
 * that uses OpenGL drawing into a dedicated surface.
 */
public class LevelActivity extends Activity {
	
	private Vector2 mWindowSize;
	private GLView mNormalModeView;
	private final Handler mHandler = new Handler();
	private NormalModeWorld mWorld;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mWorld = new NormalModeWorld(this, mHandler);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        mWindowSize = new Vector2((float)d.getWidth(), (float)d.getHeight());
        
        
        
        mNormalModeView = new GLView(this, mWindowSize, mWorld);
        mGLSurfaceView = mNormalModeView;
        setContentView(mGLSurfaceView);
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
    
    @Override
    public boolean onTouchEvent (MotionEvent event)
    {
    	if(event.getAction() == MotionEvent.ACTION_MOVE)
    	{
    		mNormalModeView.fingerMove(event.getX() / mWindowSize.x, event.getY() / mWindowSize.y);
    	}
    	else if(event.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		mNormalModeView.fingerDown(event.getX() / mWindowSize.x, event.getY() / mWindowSize.y);
    	}
    	else if(event.getAction() == MotionEvent.ACTION_UP)
    	{
    		mNormalModeView.fingerUp(event.getX() / mWindowSize.x, event.getY() / mWindowSize.y);
    	}
    	
    	return super.onTouchEvent(event);
    }

    private GLSurfaceView mGLSurfaceView;
}

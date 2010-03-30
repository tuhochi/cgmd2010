package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
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
	private TextView mHealthText;
	private Vibrator mVibrator;
	
	
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
        setContentView(mNormalModeView);
        
        LinearLayout llayout = new LinearLayout(this);
        llayout.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(llayout, params);
        
        mHealthText = new TextView(this);
        mHealthText.setText("");
        mHealthText.setGravity(Gravity.CENTER_HORIZONTAL);
        mHealthText.setTextSize(25);
        mHealthText.setTextColor(Color.BLACK);
        
        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        llparams.setMargins(0, 10, 0, 10);
        llayout.addView(mHealthText, llparams);
        
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  
    }

    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mNormalModeView.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mNormalModeView.onPause();
    }
    
    @Override
	protected void onStart() {
    	mNormalModeView.onStart();
		super.onStart();
	}

	@Override
	protected void onStop() {
		mNormalModeView.onStop();
		super.onStop();
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
    
    public void playerHPChanged(float hp)
    {
    	mHealthText.setText(Float.toString(hp));
    	
    	  
    	// 1. Vibrate for 1000 milliseconds  
    	long milliseconds = 100;  
    	mVibrator.vibrate(milliseconds);  
    }
}

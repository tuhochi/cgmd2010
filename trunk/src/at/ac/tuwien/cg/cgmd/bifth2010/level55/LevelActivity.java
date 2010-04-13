package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class LevelActivity extends Activity {
	
	//Player player=new Player();
	
	MyRenderer myRenderer;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("Activity", "onCreate");
		
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	     requestWindowFeature(Window.FEATURE_NO_TITLE);
	     
		mGLSurfaceView = new MyOpenGLView(this);
        //mGLSurfaceView.setRenderer(new MyRenderer(player));
		myRenderer=new MyRenderer();
		mGLSurfaceView.setRenderer(myRenderer);
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
		
		Log.d("Activity", "onResume");
		
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
    protected void onStop() {
    	Log.d("Activity", "onStop");
    	super.onStop();
    }
   
    public boolean onTouchEvent(MotionEvent me) {
		Log.d("onTouch", me.toString());
		
		Log.d("onTouch x",Float.toString(me.getX()));
    	
    	synchronized(this) {
	    	try {
				this.wait(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	return true;
	}
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (myRenderer.player!=null) {
	    	if (keyCode==KeyEvent.KEYCODE_A) {
	    		myRenderer.player.moveLeft(true);
	    		return true;
	    	}
	    	if (keyCode==KeyEvent.KEYCODE_D) {
	    		myRenderer.player.moveRight(true);
	    		return true;
	    	}
	    	if (keyCode==KeyEvent.KEYCODE_W) {
	    		myRenderer.player.jump(true);
	    		return true;
	    	}
    	}
		return super.onKeyDown(keyCode, event);
    }
    
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	if (keyCode==KeyEvent.KEYCODE_A) {
    		myRenderer.player.moveLeft(false);
    		return true;
    	}
    	if (keyCode==KeyEvent.KEYCODE_D) {
    		myRenderer.player.moveRight(false);
    		return true;
    	}
    	if (keyCode==KeyEvent.KEYCODE_W) {
    		myRenderer.player.jump(false);
    		return true;
    	}
    	return super.onKeyUp(keyCode, event);
    }
	
	private MyOpenGLView mGLSurfaceView;
}

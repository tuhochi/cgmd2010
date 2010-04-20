package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * The Level Activity
 * @author Wolfgang Knecht
 *
 */
public class LevelActivity extends Activity {
	
	//Player player=new Player();
	
	MyRenderer myRenderer;

	/**
	 * Creates the GLSurfaceView
	 */
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
		
        mGLSurfaceView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        mGLSurfaceView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onStop() {
    	Log.d("Activity", "onStop");
    	super.onStop();
    }
   
    /**
     * {@inheritDoc}
     */
    public boolean onTouchEvent(MotionEvent me) {	
    	float x=MyRenderer.numTilesHorizontal/MyRenderer.resX*me.getX();
    	float y=MyRenderer.numTilesVertical/MyRenderer.resY*me.getY();
    	
		if (myRenderer.player!=null && myRenderer.ui!=null) {
			if (me.getAction()==MotionEvent.ACTION_DOWN || me.getAction()==MotionEvent.ACTION_MOVE) {
				myRenderer.player.moveLeft(false);
				myRenderer.player.moveRight(false);
				myRenderer.player.jump(false);
				
				// Jump
				if (x>myRenderer.ui.gap && x<myRenderer.ui.gap+myRenderer.ui.fieldSize &&
						y>myRenderer.ui.screenHeight-myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						y<myRenderer.ui.screenHeight-myRenderer.ui.gap) {
					myRenderer.player.jump(true);
				}
				
				// Left
				if (x>myRenderer.ui.screenWidth-2.0f*myRenderer.ui.gap-2.0f*myRenderer.ui.fieldSize &&
						x<myRenderer.ui.screenWidth-2.0f*myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						y>myRenderer.ui.screenHeight-myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						y<myRenderer.ui.screenHeight-myRenderer.ui.gap) {
					myRenderer.player.moveLeft(true);
				}
				
				// Right
				if (x>myRenderer.ui.screenWidth-myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						x<myRenderer.ui.screenWidth-myRenderer.ui.gap &&
						y>myRenderer.ui.screenHeight-myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						y<myRenderer.ui.screenHeight-myRenderer.ui.gap) {
					myRenderer.player.moveRight(true);
				}
			}
			
			if (me.getAction()==MotionEvent.ACTION_CANCEL || me.getAction()==MotionEvent.ACTION_UP) {
				myRenderer.player.moveLeft(false);
				myRenderer.player.moveRight(false);
				myRenderer.player.jump(false);
			}
		}
    	
    	synchronized(this) {
	    	try {
				this.wait(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	return true;
	}
    
    /**
     * {@inheritDoc}
     */
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
    
    /**
     * {@inheritDoc}
     */
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

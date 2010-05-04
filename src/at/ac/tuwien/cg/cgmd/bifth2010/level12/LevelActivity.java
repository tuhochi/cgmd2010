package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level00.TestLevelActivity;

public class LevelActivity extends Activity{
	private Display mDisplay = null;
	private GLRenderer mRenderer = null;
	
	private int mTowerTypeSelectedByPlayer = Definitions.BASIC_TOWER;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	System.out.println("ON CREATE ACTIVITY");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if( mDisplay == null) mDisplay = wm.getDefaultDisplay(); 
    }
    
    @Override
    public void onStart(){   
       	System.out.println("ON START ACTIVITY");
        super.onStart();
    }
    
    @Override
    public boolean onTouchEvent (MotionEvent event)
    {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			GameWorld.getSingleton().setXYpos(event.getX(), (mDisplay.getHeight() - event.getY()));
			//Log.d(LOG_TAG, "width: " + d.getWidth() + " height: " + d.getHeight() + " standard y: " + event.getY());
            //Log.d(LOG_TAG, "screen --> x: " + (event.getX()) + "y: " + (d.getHeight() - event.getY()));
        }
    	return super.onTouchEvent(event);
    }
    
    
    
    @Override
    protected void onResume() {
    	TextureManager.getSingletonObject().initializeContext(this);	
    	TextureManager.getSingletonObject().add(R.drawable.l12_grass1);
    	TextureManager.getSingletonObject().add(R.drawable.l12_grass2);
    	TextureManager.getSingletonObject().add(R.drawable.l12_basic_tower);
    	TextureManager.getSingletonObject().add(R.drawable.l12_advanced_tower);
    	TextureManager.getSingletonObject().add(R.drawable.l12_hyper_tower);
    	TextureManager.getSingletonObject().add(R.drawable.l12_advanced_tower);
    	TextureManager.getSingletonObject().add(R.drawable.l12_basic_projectile);
    	TextureManager.getSingletonObject().add(R.drawable.l12_enemie_lvl0);
    	TextureManager.getSingletonObject().add(R.drawable.l12_enemie_lvl1);
    	TextureManager.getSingletonObject().add(R.drawable.l12_enemie_lvl2);
    	TextureManager.getSingletonObject().add(R.drawable.l12_enemie_lvl3);
    	TextureManager.getSingletonObject().add(R.drawable.l12_icon);	
    	
    	GameWorld.setDisplay( mDisplay.getHeight(), mDisplay.getWidth());
    	GameWorld.getSingleton().initVBOs();
	
		System.out.println("ON RESUME ACTIVITY!");
 	   	GLSurfaceView glview = new GLSurfaceView(this);
 	   	if( mRenderer == null ) mRenderer = new GLRenderer();
 	   	glview.setRenderer(mRenderer);
    	GameMechanics.getSingleton().unpause();
        super.onResume();
        setContentView( glview );
    }
    
    
    @Override
    protected void onPause() {
		System.out.println("ON PAUSE ACTIVITY!");
    	GameMechanics.getSingleton().pause();
        super.onPause();
    }
    
    @Override
	protected void onStop() {
		System.out.println("ON STOP ACTIVITY!");
		//we finish this activity;
		super.onStop();
    }
    
	@Override
	public void finish() {
		System.out.println("ON FINISHED ACTIVITY!");	
		super.finish();
		mDisplay = null;
	}
	
	@Override
	public void onDestroy(){
	   	System.out.println("ON DESTROY ACTIVITY");
	   	SessionState s = new SessionState();
    	//we set the progress the user has made (must be between 0-100)
    	s.setProgress( GameMechanics.getSingleton().getMoney() );
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
	   	super.onDestroy();
	   	mDisplay = null;
	}
		  
}

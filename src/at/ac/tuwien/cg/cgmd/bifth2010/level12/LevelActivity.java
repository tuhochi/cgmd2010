package at.ac.tuwien.cg.cgmd.bifth2010.level12;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class LevelActivity extends Activity{
	private Display mDisplay = null;
	private GLRenderer mRenderer = null;
	
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
			GameWorld.getSingleton().setXYpos((int)event.getX(), (int)(mDisplay.getHeight() - event.getY()));
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
    	TextureManager.getSingletonObject().add(R.drawable.l12_advanced_projectile);
    	TextureManager.getSingletonObject().add(R.drawable.l12_hyper_projectile);
    	TextureManager.getSingletonObject().add(R.drawable.l12_enemie_lvl0);
    	TextureManager.getSingletonObject().add(R.drawable.l12_enemie_lvl1);
    	TextureManager.getSingletonObject().add(R.drawable.l12_enemie_lvl2);
    	TextureManager.getSingletonObject().add(R.drawable.l12_enemie_lvl3);
    	TextureManager.getSingletonObject().add(R.drawable.l12_icon);	
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
    	
    	int fieldheight = (int)( mDisplay.getHeight() * 0.9 ) ;
    	int menuheight = mDisplay.getHeight() - fieldheight;
    	GameWorld.setDisplay( fieldheight, mDisplay.getWidth());
    	GameWorld.getSingleton().initVBOs();
	
 	   	GLSurfaceView glview = new GLSurfaceView(this);
 	   	if( mRenderer == null ) mRenderer = new GLRenderer();
 	   	glview.setRenderer(mRenderer);
    	GameMechanics.getSingleton().unpause();
    	GameMechanics.getSingleton().setGameContext(this);
    	    
        LinearLayout l = new LinearLayout( this );
        l.setLayoutParams( new LayoutParams( LayoutParams.FILL_PARENT,   LayoutParams.FILL_PARENT));
        l.setOrientation(LinearLayout.VERTICAL);
        GameUI.createSingleton( this, menuheight, mDisplay.getWidth() );
        l.addView( GameUI.getSingleton() );
        l.addView( glview );
        
        setContentView( l );
       
        super.onResume();
    }
    
    
    @Override
    protected void onPause() {
    	GameMechanics.getSingleton().pause();
        super.onPause();
    }
    
    @Override
	protected void onStop() {
		//we finish this activity;
		super.onStop();
    }
    
	@Override
	public void finish() {	
		SessionState s = new SessionState();
		setResult(Activity.RESULT_OK, s.asIntent());
		s.setProgress( GameMechanics.getSingleton().getMoney() );
		GameWorld.destroySingleton();
    	GameMechanics.destroySingleton();
		super.finish();
		mDisplay = null;
	}
	
	@Override
	public void onDestroy(){
	   	//SessionState s = new SessionState();
    	//we set the progress the user has made (must be between 0-100)
	   //	System.out.println("Result: "+GameMechanics.getSingleton().getMoney());
    	//s.setProgress( GameMechanics.getSingleton().getMoney() );
    	//GameWorld.destroySingleton();
    	//GameMechanics.destroySingleton();
		//we call the activity's setResult method 
		//setResult(Activity.RESULT_OK, s.asIntent());
	   	super.onDestroy();
	   	mDisplay = null;
	}
		  
}

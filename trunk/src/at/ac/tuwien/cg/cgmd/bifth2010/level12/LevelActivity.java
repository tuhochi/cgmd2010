package at.ac.tuwien.cg.cgmd.bifth2010.level12;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Debug;
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
    	//Debug.startMethodTracing("Tracefile");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if( mDisplay == null) mDisplay = wm.getDefaultDisplay(); 
    }
    
    @Override
    public void onStart(){   
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
    	SoundHandler.setContext(this);
    	
		Intent callingIntent = getIntent();
		SessionState state = new SessionState(callingIntent.getExtras());
		if(state!=null){
			boolean isMusicAndSoundOn = state.isMusicAndSoundOn(); 
			SoundHandler.getSingleton().setSound(isMusicAndSoundOn);
			System.out.println("SETTING SOUND TO: "+isMusicAndSoundOn);
		}
    	
    	//SoundHandler.getSingleton().addResource(R.raw.l12_music);
    	TextureManager.getSingletonObject().initializeContext(this);
    	TextureManager.getSingletonObject().add(R.drawable.l12_icon);			
 
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
    	int fieldheight = (int)( mDisplay.getHeight() * 0.9 ) ;
    	int menuheight = mDisplay.getHeight() - fieldheight;
    	GameWorld.setDisplay( fieldheight, mDisplay.getWidth());
    	GameWorld.getSingleton().initVBOs();
    	SoundHandler.getSingleton().reloadSamples();
	
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
    	//SoundHandler.getSingleton().playLoop(R.raw.l12_music);
        super.onResume();
    }
    
    
    @Override
    protected void onPause() {
    	GameMechanics.getSingleton().pause();
    	SoundHandler.getSingleton().stop();
        super.onPause();
    }
    
    @Override
	protected void onStop() {
		//we finish this activity;
		super.onStop();
    	//Debug.stopMethodTracing();
    }
    
	@Override
	public void finish() {	
		//the SessionState is a convenience class to set a result
		SessionState s = new SessionState();
		//we set the progress the user has made (must be between 0-100)
		int gainedMoney = GameMechanics.getSingleton().getMoney();
		int burnedMoney = GameMechanics.getSingleton().getBurnedMoney();
		float totalMoneyPercent = (gainedMoney + burnedMoney)*0.01f;
		s.setProgress((int)(burnedMoney/totalMoneyPercent));
		System.out.println("burned: " + burnedMoney + " |gained: " + gainedMoney + "score: " + (int)(burnedMoney/totalMoneyPercent));
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
		GameWorld.destroySingleton();
    	GameMechanics.destroySingleton();
    	SoundHandler.getSingleton().stop();
		super.finish();
		mDisplay = null;
	}
	
	@Override
	public void onDestroy(){
	   	super.onDestroy();
	   	mDisplay = null;
	}
		  
}

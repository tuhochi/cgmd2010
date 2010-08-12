package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * This class is the entry point for Level 83 - Lenny Must Die - Arcade.
 * @author Manuel Keglevic, Thomas Schulz
 */
public class LevelActivity extends Activity {
	
	StartDialog tutorial;
	GLSurfaceView surfaceView;
	MyRenderer renderer;
	Vibrator vibrator;
	TextView fps;
	/** Text for the death counter */
	public static TextView deaths;
	/** Text for the money counter */
	public static TextView coins;
	/** Text for the progress indicator */
	public static TextView progress;
	private static int deathcnt;
	private static int coincnt;
	String fpsString = "";
	Timer fpsUpdate;
	
	protected static Activity level;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);	
        
      //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        this.setContentView(R.layout.l83_level);
		
        surfaceView = (GLSurfaceView) findViewById(R.id.l83_surfaceview);	//GLSurfaceView erzeugen
        
        renderer = new MyRenderer(this);			//eigenen Renderer binden
        surfaceView.setRenderer(renderer);
        surfaceView.setOnTouchListener(renderer);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		renderer.setVibrator(vibrator);
        
        fps = (TextView) findViewById(R.id.l83_TextFPS);
        fps.setVisibility(View.INVISIBLE);
        deaths = (TextView) findViewById(R.id.l83_TextDeaths);
        coins = (TextView) findViewById(R.id.l83_TextCoins);
        
        progress = (TextView) findViewById(R.id.l83_TextProgress);
        
        fpsUpdate = new Timer(true);
       
        fpsUpdate.schedule(new TimerTask() {
			
			@Override
			public void run() {
				fpsString = "FPS: "+ renderer.fps;
				fpsUpdateHandler.sendEmptyMessage(0);
			}
		}, 1000, 1000);
        
        level = this;
        
        tutorial = new StartDialog(this);
        tutorial.setOnDismissListener(renderer);
        tutorial.show();
    }
      
    /**
     * Handler to update the FPS display.
     */
    private Handler fpsUpdateHandler = new Handler(){
    	
    	@Override
    	public void handleMessage(Message msg){
    		super.handleMessage(msg);
    		fps.setText(fpsString);
    	}
    };
    
    /**
     * Handler to update the deaths visualized on the screen.
     */
    public static Handler deathsUpdateHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
			deaths.setText(" x " + msg.what);
			deathcnt = msg.what;
    	}
    };
    
    /**
     * Handler to update the amount of money Lenny has gathered.
     */
    public static Handler coinsUpdateHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
			coins.setText(" x " + msg.what);
			coincnt = msg.what;
    	}
    };
    
    /**
     * Handler to update Lenny's progress in the level.
     */
    public static Handler progressUpdateHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		progress.setText(msg.what + " %");
    	}
    };
   
    /**
     * Handler to pass the user's points to the framework and finish the level 
     * by showing a {@link FinishDialog}.
     */
    public static Handler finishLevel = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
			final SessionState s = new SessionState();
			int progress = Math.min(Math
					.max(msg.arg1 * 20
							- ((msg.arg2 % 2 == 0) ? msg.arg2 : (msg.arg2 - 1))
							* 10, 0), 100);
			s.setProgress(progress);
			FinishDialog dialog = new FinishDialog(level, msg.arg1, msg.arg2, progress);
			//dialog.setMessage("END");
			
			dialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					//we call the activity's setResult method 
					level.setResult(Activity.RESULT_OK, s.asIntent());
					//we finish this activity
					level.finish();
				}
			});
			dialog.show();	
    	}
    };
    
    @Override
    protected void onStart(){
    	super.onStart();
    	
    }
    
    @Override
    protected void onPause() {
    	super.onPause();

    	surfaceView.onPause();
    	renderer.onPause();
    	SoundManager.singleton.dispose();
    	
    	Log.d("Main","OnPause");
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	renderer.onStop();
    	Log.d("Main","OnStop");
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	
    	renderer.onDestroy();
    	fpsUpdate.cancel();
    }
    @Override
    protected void onResume() {
    	Intent callingIntent = getIntent();
    	
    	boolean musicOn = true;
    	SessionState state = new SessionState(callingIntent.getExtras());
    	
    	if(state != null)
    		musicOn = state.isMusicAndSoundOn();
    	
    	new SoundManager(this,musicOn);
    	
    	super.onResume();
    	//MyTextureManager.singleton.reload(this, gl);
    	surfaceView.onResume();
    	renderer.onResume();
    	
    	
    	Log.d("Main","OnResume");
    }
    
    @Override
    public void finish() {
    	Log.d("l83LevelActivity", "finish()");
    	final SessionState s = new SessionState();
		int progress = Math.min(Math
				.max(deathcnt * 20
						- ((coincnt % 2 == 0) ? coincnt : (coincnt - 1))
						* 10, 0), 100);
		s.setProgress(progress);
		setResult(Activity.RESULT_OK, s.asIntent());
    	super.finish();
    }
}
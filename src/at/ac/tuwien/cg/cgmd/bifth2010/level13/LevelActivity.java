package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.GameObject;

/**
 * 
 * @author arthur (group 13)
 *
 */
public class LevelActivity extends Activity {
	//custom renderer
	private MyRenderer myRenderer;
	
	private TextView fpsTextView;
	private String fpsString;
	private TextView moneyTextView;
	private String moneyString;
	private TextView timeTextView;
	private String timeString;
	
	private Timer fpsUpdateTimer;
	private Timer gameTimeUpdateTimer;
	private Timer moneyUpdateTimer;
	
	
    /**
     *  called when the activity is first created
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("df", "called oncreate");
        
        boolean restart = false;
        if(getLastNonConfigurationInstance() != null) {
        	restart = (Boolean)getLastNonConfigurationInstance();
        }
        //restore state
        if(restart) {
        	Log.d("df", "RESTART");
        }
        else {
        	GameControl.reset();
        	GameTimer.reset();
        	GameObject.reset();
        	MyRenderer.reset();
    
        }
        
        //make window fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	Window window = getWindow();
	 	window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	 	
	 
	 	//setup layout with fps-overlay
	 	setContentView(R.layout.l13_level);
	 	myRenderer = (MyRenderer)findViewById(R.id.l13_MyRenderer);
	 	fpsTextView = (TextView)findViewById(R.id.l13_fpsTextView);
	 	timeTextView = (TextView)findViewById(R.id.l13_timeText);
	 	moneyTextView = (TextView)findViewById(R.id.l13_moneyText);

	 	//timer for fps display
	 	fpsUpdateTimer = new Timer();
	 	fpsUpdateTimer.schedule(new TimerTask() {
	 	
	 		@Override
	 		public void run() {
	 			FPSCounter counter = FPSCounter.getInstance();
	 			fpsString = "fps: " + counter.getFPS();
	 		
	 			handleUIChanges.sendEmptyMessage(0);
	 		}
	 	}, 0, 1000);
	 	
	 	//timer for game time
		gameTimeUpdateTimer = new Timer();
	 	gameTimeUpdateTimer.schedule(new TimerTask() {
	 	
	 		@Override
	 		public void run() {
	 			GameTimer gameTimer = GameTimer.getInstance();
	 			timeString = "Time: " + gameTimer.getRemainingTimeString();
	 			handleUIChanges.sendEmptyMessage(0);
	 			if(gameTimer.isOver()) {
	 				finish();
	 			}
	 		}
	 	}, 0, 500);
	 	
	 	//timer for money
		moneyUpdateTimer = new Timer();
	 	moneyUpdateTimer.schedule(new TimerTask() {
	 	
	 		@Override
	 		public void run() {
	 			moneyString = "Money: " + GameControl.getInstance().getMoney() + "$";
	 			handleUIChanges.sendEmptyMessage(0);
	 		}
	 	}, 0, 500);
    }
    
   private Handler handleUIChanges = new Handler() {
	 @Override
	 public void handleMessage(Message msg) {
		 super.handleMessage(msg);
		 fpsTextView.setText(fpsString);
		 timeTextView.setText(timeString);
		 moneyTextView.setText(moneyString);
	 }
   };
   
   @Override
   protected void onStart() {
	   super.onStart();
	   Log.d("df", "called onstart");
   }
   
   @Override
   protected void onRestart() {
	   super.onRestart();
	   Log.d("df", "called onrestart");
   }
   
	/**
	 * Remember to resume the glSurface
	 */
	@Override
	protected void onResume() {
		super.onResume();
		myRenderer.onResume();
		
		Log.d("df", "called onresume");

	}

	/**
	 * Also pause the glSurface
	 */
	@Override
	protected void onPause() {
		super.onPause();
		myRenderer.onPause();
		SoundManager.stopMusic();
		
		Log.d("df", "called onpause");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d("df", "called onstop");
	}
	
	@Override
	protected void onDestroy() {
		//release resources
	    super.onDestroy();
	    SoundManager.stopMusic();
	    
	    //stop timers
	    fpsUpdateTimer.cancel();
	    gameTimeUpdateTimer.cancel();
	    moneyUpdateTimer.cancel();
	    Log.d("df", "called ondestroy");
	}
	
	@Override
	public void finish() {
	    SessionState s = new SessionState();
		s.setProgress(-GameControl.getInstance().getMoney()); 
		setResult(Activity.RESULT_OK, s.asIntent());
		super.finish();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("df", "called onsaveinstancestate");
	
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return true;
	}
	
	
}
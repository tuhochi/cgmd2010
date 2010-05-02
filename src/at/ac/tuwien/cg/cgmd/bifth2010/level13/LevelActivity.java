package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

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
	
	
    /**
     *  called when the activity is first created
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //make window fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	Window window = getWindow();
	 	window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	 	
	 
	 	//setup layout with fps-overlay
	 	setContentView(R.layout.l13_level);
	 	//FrameLayout frameLayout = (FrameLayout)findViewById(R.id.l13_levelLayout);
	 	myRenderer = (MyRenderer)findViewById(R.id.l13_MyRenderer);
	 	fpsTextView = (TextView)findViewById(R.id.l13_fpsTextView);
	 	timeTextView = (TextView)findViewById(R.id.l13_timeText);
	 	moneyTextView = (TextView)findViewById(R.id.l13_moneyText);
	 	//frameLayout.removeView(fpsTextView);
	 	//frameLayout.addView(myRenderer);
	 	//frameLayout.addView(fpsTextView);
	

	 	

	 	//timer for fps display
	 	Timer fpsUpdateTimer = new Timer();
	 	fpsUpdateTimer.schedule(new TimerTask() {
	 	
	 		@Override
	 		public void run() {
	 			FPSCounter counter = FPSCounter.getInstance();
	 			fpsString = "fps: " + counter.getFPS();
	 		
	 			handleUIChanges.sendEmptyMessage(0);
	 		}
	 	}, 0, 1000);
	 	
	 	//timer for game time
		Timer gameTimeUpdateTimer = new Timer();
	 	gameTimeUpdateTimer.schedule(new TimerTask() {
	 	
	 		@Override
	 		public void run() {
	 			GameTimer gameTimer = GameTimer.getInstance();
	 			timeString = "Time: " + gameTimer.getRemainingTimeString();
	 			handleUIChanges.sendEmptyMessage(0);
	 			if(gameTimer.isOver()) {
	 				SessionState s = new SessionState();
	 				s.setProgress(-GameControl.money); 
	 				setResult(Activity.RESULT_OK, s.asIntent());
	 				finish();
	 			}
	 		}
	 	}, 0, 500);
	 	
	 	//timer for money
		Timer moneyUpdateTimer = new Timer();
	 	moneyUpdateTimer.schedule(new TimerTask() {
	 	
	 		@Override
	 		public void run() {
	 			moneyString = "Money: " + GameControl.money + "$";
	 			handleUIChanges.sendEmptyMessage(0);
	 		}
	 	}, 0, 500);
	 	
	 	SessionState s = new SessionState();
		s.setProgress(0); 
		setResult(Activity.RESULT_OK, s.asIntent());
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
   
	/**
	 * Remember to resume the glSurface
	 */
	@Override
	protected void onResume() {
		super.onResume();
		myRenderer.onResume();

	}

	/**
	 * Also pause the glSurface
	 */
	@Override
	protected void onPause() {
		super.onPause();
		myRenderer.onPause();
		SoundManager.stopMusic();
	}
	
	 @Override
	    protected void onDestroy() {
	    	// TODO Auto-generated method stub
	    	super.onDestroy();
	    	SoundManager.stopMusic();
	    	
	    }
	
}
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

/**
 * 
 * @author arthur (group 13)
 *
 */
public class LevelActivity extends Activity {
	//custom renderer
	private MyRenderer myRenderer;
	
	private TextView fpsTextView,guiTextView;
	private String fpsString,guiString;
	
	
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
	 	FrameLayout frameLayout = (FrameLayout)findViewById(R.id.l13_levelLayout);
	 	fpsTextView = (TextView)findViewById(R.id.l13_fpsTextView);
	 	frameLayout.removeView(fpsTextView);
	 	myRenderer = new MyRenderer(this);
	 	frameLayout.addView(myRenderer);
	 	frameLayout.addView(fpsTextView);
	   // guiTextView = (TextView)findViewById(R.id.l13_JailText);
	  
	 	//frameLayout.removeView(guiTextView);
	 //	frameLayout.addView(guiTextView);
	 	

	 	

	 	//timer for fps display
	 	Timer fpsUpdateTimer = new Timer();
	 	fpsUpdateTimer.schedule(new TimerTask() {
	 	
	 		@Override
	 		public void run() {
	 			FPSCounter counter = FPSCounter.getInstance();
	 			fpsString = "fps: " + counter.getFPS();
	 			/*
	 			if(GameControl.inJail)
	 				guiString = "YOU ARE IN ARREST ";
	 			else
	 				guiString = "";*/
	 			//guiString = "YOU ARE IN ARREST ";
	 			handleUIChanges.sendEmptyMessage(0);
	 		}
	 	}, 0, 1000);
    }
    
   private Handler handleUIChanges = new Handler() {
	 @Override
	 public void handleMessage(Message msg) {
		 super.handleMessage(msg);
		 fpsTextView.setText(fpsString);
	//	 guiTextView.setText(guiString);
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
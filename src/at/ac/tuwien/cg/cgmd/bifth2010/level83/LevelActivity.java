package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * This class is the entry point for Level 83 - Lenny Must Die - Arcade.
 */
public class LevelActivity extends Activity {
	
	GLSurfaceView surfaceView;
	MyRenderer renderer;
	TextView fps;
	/** Text for the death counter */
	public static TextView deaths;
	/** Text for the money counter */
	public static TextView coins;
	String fpsString = "";
	
	private int hdextension = 60;
	protected static Activity level;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        surfaceView = new GLSurfaceView(this);	//GLSurfaceView erzeugen
        
        renderer = new MyRenderer(this);			//eigenen Renderer binden
        surfaceView.setRenderer(renderer);
        surfaceView.setOnTouchListener(renderer);
        
        FrameLayout fl = new FrameLayout(this);	//SurfaceView anzeigen
        fl.addView(surfaceView);
        
        LinearLayout textl = new LinearLayout(this);
        fps = new TextView(this);
        fps.setText("FPS: ");
        fps.setTextColor(0xFF4DA3B7);
        deaths = new TextView(this);
        deaths.setText("      x ");
        deaths.setTextColor(0xFFFFFFFF);
        deaths.setWidth(80 + ((deaths.getTextSize() > 15) ? hdextension : 0));
        coins = new TextView(this);
        coins.setText("      x ");
        coins.setTextColor(0xFFFFFFFF);
        coins.setWidth(90 + ((coins.getTextSize() > 15) ? hdextension : 0));
        
        textl.addView(deaths);
        textl.addView(coins);
        textl.addView(fps);
        Log.d("LevelActivity", "Textsize: " + coins.getTextSize());
       
        fl.addView(textl);
        
        this.setContentView(fl);		
        
        Timer fpsUpdate = new Timer();
        
        fpsUpdate.schedule(new TimerTask() {
			
			@Override
			public void run() {
				fpsString = "FPS: "+ renderer.fps;
				fpsUpdateHandler.sendEmptyMessage(0);
			}
		}, 1000, 1000);
        
        level = this;
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
			deaths.setText("      x " + msg.what);
    	}
    };
    
    /**
     * Handler to update the amount of money Lenny has gathered.
     */
    public static Handler coinsUpdateHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
			coins.setText("      x " + msg.what);
    	}
    };
    
    public static Handler finishLevel = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		//the SessionState is a convenience class to set a result
			SessionState s = new SessionState();
			//we set the progress the user has made (must be between 0-100)
			s.setProgress(msg.what);
			//we call the activity's setResult method 
			level.setResult(Activity.RESULT_OK, s.asIntent());
			//we finish this activity
			level.finish();
    	}
    };
    
    @Override
    protected void onPause() {
    	super.onPause();
    	surfaceView.onPause();
    	renderer.onPause();
    	
    	Log.d("Main","OnPause");
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	renderer.onStop();
    	
    	Log.d("Main","OnStop");
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	//MyTextureManager.singleton.reload(this, gl);
    	surfaceView.onResume();
    	renderer.onResume();
    	Log.d("Main","OnResume");
    }
}
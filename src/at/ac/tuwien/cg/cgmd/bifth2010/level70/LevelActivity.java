package at.ac.tuwien.cg.cgmd.bifth2010.level70;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer.RendererView;

/**
 * Level Activity.
 */
public class LevelActivity extends Activity {

	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	private static LevelActivity instance; //< Level activity instance for global access
	private RendererView rendererView;     //< Renderer view
	public TextView tv;	
	public Handler handler;
	public Runnable fpsUpdateRunnable;
	
	// ----------------------------------------------------------------------------------
	// -- Static methods ----
	
	/**
	 * Return instance.
	 * @return LevelActivity instance
	 */
	public static LevelActivity getInstance() {
		return instance;
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Create level activity.
	 * @param Bundle.
	 */
	public void onCreate(Bundle savedInstanceState) {
		
		fpsUpdateRunnable = new Runnable() {
			public void run() {
				rendererView.onPause();
				tv.setVisibility(View.VISIBLE);
			}
		};
		
		// Set instance for global access
   		instance = this;
   		handler = new Handler();
   		
		super.onCreate(savedInstanceState);
		
		// Set fullscreen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       
        // Get display
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        
        // Create renderer view
   		rendererView = new RendererView(this, savedInstanceState, d.getWidth(), d.getHeight());
   		setContentView(rendererView);
   		
   		setStateLevelEnd(0);
	}
	
	
	/**
	 * Start the game.
	 */
	@Override
    protected void onStart() {
        Log.i("LevelActivity70", "onStart");
        super.onStart();
        rendererView.onStart();
    }
	
	/**
	 * Resume the game.
	 */
	@Override
    protected void onResume() {
        Log.i("LevelActivity70", "onResume");
        super.onResume();
        rendererView.onResume();
    }

	
	/**
	 * Pause the game.
	 */
    @Override
    protected void onPause() {
    	Log.i("LevelActivity70", "onPause");
        super.onPause();
        rendererView.onPause();
    }
    

    /** 
     * Stop the game.
     */
    @Override
	protected void onStop() {
    	Log.i("LevelActivity70", "onStop");
		super.onStop();
		rendererView.onStop();
    }

	
    /**
     * Destroy the game.
     */
    @Override
	public void onDestroy() {
		Log.i("LevelActivity70", "onDestroy");
		super.onDestroy();
		rendererView.onDestroy();
	}
    
    
    /**
     * Save game states.
     */
    @Override
	protected void onSaveInstanceState(Bundle outState) {
        Log.i("LevelActivity70", "onSaveInstanceState");
        rendererView.onPause();
    	rendererView.onSaveState(outState);
    }
    
    
    /**
     * Restore game states.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	rendererView.onRestoreState(savedInstanceState);
    }
    
   
    
    public void setStateLevelEnd(int progress) {
    	
    	LinearLayout llayout = new LinearLayout(this);
        llayout.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(llayout, params);
        
        tv = new TextView(this);
        tv.setTextSize(25);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.BLACK);  
        tv.setText(R.string.l70_GameEnd);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        
        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        
        llparams.weight = 0;
        llparams.setMargins(10, 10, 10, 10);
        llayout.addView(tv, llparams);
        tv.setVisibility(View.INVISIBLE);
        
    	//setContentView(tv);
    	//SessionState s = new SessionState();
		//s.setProgress(10);
		//setResult(Activity.RESULT_OK, s.asIntent());
		//finish();
		
    }
}

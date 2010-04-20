package at.ac.tuwien.cg.cgmd.bifth2010.level70;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer.RendererView;

/**
 * Level Activity.
 */
public class LevelActivity extends Activity {

	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	private static LevelActivity instance; //< Level activity instance for global access
	private RendererView rendererView;     //< Renderer view
		
	
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
   		
   		// Set instance for global access
   		instance = this;
	}
	
	
	/**
	 * Resume the game.
	 */
	@Override
    protected void onResume() {
        Log.i("LevelActivity70", "onResume");
        super.onResume();
        rendererView.onStart();
    }

	
	/**
	 * Pause the game.
	 */
    @Override
    protected void onPause() {
    	Log.i("LevelActivity70", "onPause");
        super.onPause();
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
    	rendererView.onSaveState(outState);
    }
    
    
    /**
     * Restore game states.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	rendererView.onRestoreState(savedInstanceState);
    }
}

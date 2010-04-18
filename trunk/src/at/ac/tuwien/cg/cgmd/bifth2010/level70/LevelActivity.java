package at.ac.tuwien.cg.cgmd.bifth2010.level70;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
   		rendererView = new RendererView(this,d.getWidth(), d.getHeight());
   		setContentView(rendererView);
   		
   		// Set instance for global access
   		instance = this;
	}
	
	
	
}
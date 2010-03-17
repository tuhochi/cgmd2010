package at.ac.tuwien.cg.cgmd.bifth2010.level23;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.Renderer;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.OrientationListener;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.OrientationManager;

public class LevelActivity extends Activity implements OrientationListener {

	private Renderer renderer; 
	private static Context CONTEXT; 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		renderer = new Renderer(this);
        setContentView(renderer);
        CONTEXT = this; 
        OrientationManager.registerListener(this);
	}
	
	public void onResume() {
		super.onResume(); 
	}
	
	public void onDestroy() {
		super.onDestroy();
		if (OrientationManager.isListening()) 
			OrientationManager.unregisterListener();
	}

	@Override
	public void onOrientationChanged(float azimuth, float pitch, float roll) {
		// handle notification that the orientation has changed (as for now, for roll only) 
		// maybe we could use it later?! 
	}

	@Override
	public void onRollLeft() {
		renderer.handleRollMovement(false); 
		
	}

	@Override
	public void onRollRight() {
		renderer.handleRollMovement(true);
		
	}
	
	public static Context getContext() {
		return CONTEXT;
	}
}
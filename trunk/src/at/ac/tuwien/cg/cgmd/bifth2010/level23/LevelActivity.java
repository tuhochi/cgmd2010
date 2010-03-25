package at.ac.tuwien.cg.cgmd.bifth2010.level23;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.MainChar;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.Renderer;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.OrientationListener;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.OrientationManager;

public class LevelActivity extends Activity implements OrientationListener {

	private Renderer renderer; 
	private static Context CONTEXT; 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// thx @ lvl 11
		/* Fullscreen window without title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	Window window = getWindow();
	 	window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		renderer = new Renderer(this);
		renderer.setOrientationListener(this);
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
			OrientationManager.unregisterListener(this);
	}

	@Override
	public void onOrientationChanged(float azimuth, float pitch, float roll) {
		// handle notification that the orientation has changed (as for now, for roll only) 
		// maybe we could use it later?! 
	}

	@Override
	public void onRollLeft() {
		renderer.handleRollMovement(MainChar.MOVE_LEFT); 		
	}

	@Override
	public void onRollRight() {
		renderer.handleRollMovement(MainChar.MOVE_RIGHT);		
	}
	
	@Override
	public void isInDeadZone() {
		renderer.handleRollMovement(MainChar.NO_MOVEMENT);		
	}
	
	public static Context getContext() {
		return CONTEXT;
	}
}
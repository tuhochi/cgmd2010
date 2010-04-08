package at.ac.tuwien.cg.cgmd.bifth2010.level23;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.MainChar;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.OrientationListener;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.OrientationManager;

public class LevelActivity extends Activity implements OrientationListener {

	private RenderView renderer; 
	private static Context CONTEXT; 
	private static LevelActivity instance;
	
	private static final int SENSOR_MENU_ITEM = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// thx @ lvl 11
		/* Fullscreen window without title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	Window window = getWindow();
	 	window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		renderer = new RenderView(this);
		renderer.setOrientationListener(this);
        setContentView(renderer);
        CONTEXT = this; 
        OrientationManager.registerListener(this);

        instance = this;
	}
	
	public static LevelActivity getInstance()
	{
		return instance;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// called when activity becomes visible
		// followed by onResume() or onStop()
	}

	@Override
	public void onRestart() {
		super.onRestart();
		// called when activity is being stopped
		// followed by onStart()
	}
	@Override
	public void onPause() {
		super.onPause(); 
		renderer.persistSceneEntities();
		// should be fast because could be called often 
		// end animation and other cpu consuming tasks
		// followed by onResume() or onStop()
		// persist data here to SharedPreferences object
	}
	@Override
	public void onResume() {
		super.onResume(); 
		renderer.restoreSceneEntities();
		// followed by onPause()
	}
	@Override
	public void onStop() {
		super.onStop();
		// called when activity is no longer visible 
		// followed by onRestart() or onDestroy() 
	
	}
	@Override
	public void onDestroy() {
		// called before acitivity is going to be destroyed
		super.onDestroy();
		if (OrientationManager.isListening()) 
			OrientationManager.unregisterListener(this);
	}
	

	@Override
	public void onOrientationChanged(float azimuth, float pitch, float roll) {
		// handle notification that the orientation has changed (as for now, for roll only) 
		// maybe we could use it later?! 
	}
	
	public void onSaveInstanceState(Bundle toSave) {
		// e.g. toSave.putInt("value", 1);
		// will be called before activity is moved to the background
		// but will not be called in every situation! Therefore, save persistent data in onPause() 
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		menu.add(0, SENSOR_MENU_ITEM, 0, "Sensor On/Off");
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) 
	    {
	    case SENSOR_MENU_ITEM:
	        renderer.switchSensor();
	        return true;

	    }
	    return false;
	}
}
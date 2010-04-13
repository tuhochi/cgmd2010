/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;


import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * @author Reinbert
 *
 */
public class LevelActivity extends Activity {

	private RenderView renderView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//Initiate our Lesson with this Activity Context handed over
		renderView = new RenderView(this);
		//Set the lesson as View to the Activity
		setContentView(renderView);
		//Create an Instance with this Activity
		
	}
	
	

	@Override
	protected void onPause() {
		super.onPause();
		renderView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		renderView.onResume();
	}
	
	
 
	@Override
	protected void onRestart() {
		
		super.onRestart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		
		
		super.onDestroy();
	}
	
	
}
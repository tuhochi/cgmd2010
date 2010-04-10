/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;


import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * @author Reinbert
 *
 */
public class LevelActivity extends Activity {

	private RenderView renderView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
	
	
}

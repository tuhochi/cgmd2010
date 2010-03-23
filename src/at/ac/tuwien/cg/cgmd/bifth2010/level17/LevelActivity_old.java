package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import javax.microedition.khronos.opengles.GL;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.*;
/**
 * This activity demonstrates the basic interaction with the framework. When the Finish button is pressed the a result is set and the activity is finished. 
 * @author Peter
 *
 */
public class LevelActivity_old extends Activity{

	/** The OpenGL View */
	private SimpleRenderer mSimpleRenderer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Create an Instance of SimpleRenderer
		mSimpleRenderer = new SimpleRenderer(this);
		mSimpleRenderer.setGLWrapper(new GLSurfaceView.GLWrapper() {
            public GL wrap(GL gl) {
                return new MatrixTrackingGL(gl);
            }});
		//Set the SimpleRenderer as View to this Activity
		setContentView(mSimpleRenderer);
		
		/*
		//get the button specified in the layout
		Button buttonFinish = (Button) findViewById(R.id.l00_ButtonFinish);
		//set a onClickListener to react to the user's click
		buttonFinish.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//the SessionState is a convenience class to set a result
				SessionState s = new SessionState();
				//we set the progress the user has made (must be between 0-100)
				s.setProgress(10);
				//we call the activity's setResult method 
				setResult(Activity.RESULT_OK, s.asIntent());
				//we finish this activity
				LevelActivity.this.finish();
			}
			
		});*/
	}

	/**
	 * Remember to resume the glSurface
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mSimpleRenderer.onResume();
	}

	/**
	 * Also pause the glSurface
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mSimpleRenderer.onPause();
	}
	
}

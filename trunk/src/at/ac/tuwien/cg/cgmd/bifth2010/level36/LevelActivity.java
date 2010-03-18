package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import android.app.Activity;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * 
 * @author Gruppe 36
 *
 */
public class LevelActivity extends Activity{
	private Game scene;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("LevelActivity onCreate");
		super.onCreate(savedInstanceState);
		scene = new Game(this);
		setContentView(scene);
	}
	
	/**
	 * 
	 * @param score
	 */
	private void finish(int score) {
		//the SessionState is a convenience class to set a result
		SessionState s = new SessionState();
		//we set the progress the user has made (must be between 0-100)
		s.setProgress(score);
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
		//we finish this activity
		finish();
	}
	
	/**
	 * Resume the scene
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (scene != null) {
			scene.onResume();
		}
	}

	/**
	 * Pause the scene
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (scene != null) {
			scene.onPause();
		}
	}
}

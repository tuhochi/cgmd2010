/**
 * Flight66 - a trip to hell
 * 
 * @author brm, dwi
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level66;

import android.app.Activity;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.OrientationManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.SoundManager;

/**
 * Wrapper activity demonstrating the use of {@link GLSurfaceView}, a view
 * that uses OpenGL drawing into a dedicated surface.
 */
public class LevelActivity extends Activity {

	private Level66View _level66View;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		_level66View = new Level66View(this);
        setContentView(_level66View);
	}
	
	/*
	 * After onPause
	 * @see android.app.Activity#onResume()
	 */
    @Override
    protected void onResume() {
        super.onResume();
        _level66View.onResume();
        Sound.resumeSound();
    }

    /*
     * After onResume or onStop
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        _level66View.onPause();
        Sound.pauseSound();
    }
    /*
    /*
     * Called after onResume or onStop
     * @see android.app.Activity#onStart()
     */
    @Override
	protected void onStart() {
		super.onStart();
	}
    
    /*
     * After Destroy or onRestart
     * @see android.app.Activity#onStop()
     */
    @Override
	protected void onStop() {
		super.onStop();
    }
    
    /*
     * Before Activity will be destroyed
     * @see android.app.Activity#onDestroy()
     */
	@Override
	public void onDestroy() {
		super.onDestroy();	
		Sound.destroySound();
	}
  
}

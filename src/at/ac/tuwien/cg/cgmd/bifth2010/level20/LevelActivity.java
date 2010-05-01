/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level20.RenderView;

/**
 * The top level class of the game. 
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class LevelActivity extends Activity {

	private RenderView renderView;
	private TouchListener touchListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.l20_level);
        renderView = (RenderView)findViewById(R.id.l20_RenderView);
		
        // Register our own TouchListener
		touchListener = new TouchListener();		
		renderView.setOnTouchListener(touchListener);      
		
		SessionState s = new SessionState();
		s.setProgress(0); 
		setResult(Activity.RESULT_OK, s.asIntent());
		
	}
	


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
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

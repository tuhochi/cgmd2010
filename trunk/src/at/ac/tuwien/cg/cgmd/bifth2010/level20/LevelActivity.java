/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

	protected static RenderView renderView;
	protected static GameManager gameManager;
	
	protected static LevelActivity instance;
//	protected static Handler handler; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		instance = this;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.l20_level);
        renderView = (RenderView)findViewById(R.id.l20_RenderView);
        
        // Create text view for display of money count.
        renderView.moneyText = (TextView)findViewById(R.id.l20_MoneyText);
		// Create text view for display of time left.		
        renderView.timeText = (TextView)findViewById(R.id.l20_TimeText);

        gameManager = new GameManager();
		renderView.setOnTouchListener(gameManager);
		
//		handler = new Handler();
                		
		SessionState s = new SessionState();
		s.setProgress(0); 
		setResult(Activity.RESULT_OK, s.asIntent());
		
		Log.d(getClass().getSimpleName(), "LevelActivity (re)created");
		
	}
	


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Called when the activity is paused. Should be slim, because it can be called often
	 * Followed by onResume() or onStop()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		renderView.onPause();
	}

	/**
	 * Called when the activity is resumed 
	 * Followed by onPause()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		gameManager.time.reset();
		renderView.onResume();
	}


	/**
	 * called when the activity is stopped. 
	 * Followed by onRestart() or onDestroy() 
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	/**
	 * Called before the activity is going to be destroyed
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();	
	}
	
	

	
}

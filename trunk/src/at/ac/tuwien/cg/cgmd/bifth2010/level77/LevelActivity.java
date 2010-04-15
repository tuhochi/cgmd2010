package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import android.app.Activity;

//import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

import android.content.Context;

/**
 * Entry point for the Level 77 BunnyBlock
 * @author Gerd Katzenbeisser
 *
 */
public class LevelActivity extends Activity
{
	protected static final String	TAG	= "LevelActivity";
	private GameView gameView;
	
	// for c++ code
    static {
        System.loadLibrary("l77fireblocks");
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Starting LevelActivity 77");

		setResult(RESULT_CANCELED);
		
		Callback<Integer> gameEnded = new Callback<Integer>()
		{
			
			@Override
			public void onSucces(Integer result)
			{
				SessionState s = new SessionState();
				s.setProgress(result / 100);		
				Log.i(TAG, "Lost Gold: " + result / 100);
				setResult(RESULT_OK, s.asIntent());	
				LevelActivity.this.finish();
			}
			
			@Override
			public void onFailure(Throwable caught)
			{
				caught.printStackTrace();
			}
		};
		
		gameView = new GameView(this, gameEnded);
		setContentView( gameView );		
	}
	


    @Override
	protected void onPause() {
		super.onPause();
		gameView.onPause();		
	}

	@Override
	protected void onResume() {
		super.onResume();
		gameView.onResume();
	}
	

}

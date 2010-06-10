package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import java.io.ObjectOutputStream.PutField;

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
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Entry point for the Level 77 BunnyBlock
 * @author Gerd Katzenbeisser
 *
 */
public class LevelActivity extends Activity
{
	protected static final String	TAG	= "LevelActivity";
	private GameView gameView;
	private boolean showsInitScreen = false;

	
	
	// for c++ code
    static {
        System.loadLibrary("l77fireblocks");
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		
		
		
//		TODO Show info Screen
		showsInitScreen = true;
//		Intent i = new Intent("at.ac.tuwien.cg.cgmd.bifth2010.level77.LAUNCH_INTRO");
//		this.startActivity(i);
//		Log.d(TAG, "Starting LevelActivity 77");
		showsInitScreen = false;
		
	

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
		
		gameView = new GameView(this, gameEnded, new SessionState(getIntent().getExtras()));		

		setContentView( gameView );				
	}

    @Override
	protected void onPause() {
		super.onPause();
		if (!showsInitScreen)
			gameView.onPause(this.getPreferences(MODE_PRIVATE));		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!showsInitScreen)
			gameView.onResume(this.getPreferences(MODE_PRIVATE));
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		setResult(RESULT_CANCELED);
		Log.i("l77_state", "**stop activity");
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		Log.i("l77_state", "**onstart");
	}

}

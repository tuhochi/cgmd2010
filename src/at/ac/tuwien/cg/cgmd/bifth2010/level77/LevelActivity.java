package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import android.app.Activity;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;

/**
 * Level 77 BunnyBlock
 * <p>
 * A stack of precious blocks moves up. Click between two blocks to swap them
 * horizontally. Three blocks in a row cancel each other out. Gold is lost to
 * reinstate the value of the blocks! Try to cancel as many blocks as possible
 * in one go and you will earn more points by having more Gold taken away.
 * <p>
 * If there are more items above the affected blocks they fall down. And if you
 * are lucky enough for them to fall next to similar items - you will get bonus
 * points! The more chained blocks are cancelled more money you will get.. or
 * loose!
 * <p>
 * New Blocks appear automatically at the bottom. You can get new blocks more
 * quickly by dragging up at the right beneath the blocks.
 * 
 * @author Gerd Katzenbeisser
 * 
 */
public class LevelActivity extends Activity
{
	protected static final String	TAG				= "LevelActivity";
	private GameView				gameView;
	private boolean					showsInitScreen	= false;

	// Loading native library
	static
	{
		System.loadLibrary("l77fireblocks");
	}

	/**
	 * Initializes the class and launches the
	 * at.ac.tuwien.cg.cgmd.bifth2010.level77.LAUNCH_INTRO activity to show the
	 * intro screen. It also sets the contentView to a new GameView.
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// TODO Show info Screen
//		showsInitScreen = true;


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

		gameView = new GameView(this, gameEnded, new SessionState(getIntent().getExtras()), this.getPreferences(MODE_PRIVATE));
		gameView.setClickable(true);
		gameView.setFocusable(true);

		setContentView(gameView);
		gameView.requestFocus();
		
		Intent i = new Intent("at.ac.tuwien.cg.cgmd.bifth2010.level77.LAUNCH_INTRO");
		this.startActivity(i);
		Log.d(TAG, "Starting LevelActivity 77");
		showsInitScreen = false;
	}

	/**
	 * Deletes all stored SharedPreferences.
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		//Editor ed = this.getPreferences(MODE_PRIVATE).edit();
		//ed.clear();
		//ed.commit();

		// free native DSs
		gameView.onDestroy(this.getPreferences(MODE_PRIVATE));
	}

	/**
	 * Calls the onPause in GameView
	 * 
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level77.GameView#onPause(android.content.SharedPreferences)
	 */
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d(TAG, "onPause");
		if (!showsInitScreen)
			gameView.onPause(this.getPreferences(MODE_PRIVATE));
	}

	/**
	 * Call the onResume in GameView
	 * 
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level77.GameView#onResume(android.content.SharedPreferences)
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.d(TAG, "onResume");
		if (!showsInitScreen)
			gameView.onResume(this.getPreferences(MODE_PRIVATE));
	}

	/**
	 * Sets result RESULT_CANCELED
	 * 
	 * @see android.app.Activity.RESULT_CANCELED
	 */
	@Override
	public void onStop()
	{
		super.onStop();
		setResult(RESULT_CANCELED);
		Log.i("l77_state", "**stop activity");
	}
}

package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;


/**
 * Entry point for Level 60.
 * 
 * @author      Martin Schenk
 * @author      Tiare Feuchtner
 */
public class LevelActivity extends Activity {
	private LevelSurfaceView glv;
	SharedPreferences prefs;
	private Bundle myState;
	MediaPlayer player;
	public boolean soundOn = false;

	private final static String BUNNY_X = "BUNNY_X";
	private final static String BUNNY_Y = "BUNNY_Y";
	private final static String MAP_OFFSET_X = "MAP_OFFSET_X";
	private final static String MAP_OFFSET_Y = "MAP_OFFSET_Y";


	/** 
	 * Called when the activity is starting. Sets the content view and initializes savedInstanceState.
	 * I addition  we check if sound is on or phone is muted.
	 *
	 * @param savedInstanceState	Contains the recent data which is saved in onInstanceState.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		glv = new LevelSurfaceView(this, savedInstanceState);
		setContentView(glv);

		myState = savedInstanceState;


		Intent callingIntent = getIntent();
		SessionState state = new SessionState(callingIntent.getExtras());
		if(state!=null){
			soundOn = state.isMusicAndSoundOn();
		}
		return;
	}

	SessionState sessionState;

	/**
	 * Called as part of the activity lifecycle when an activity is going into the background, 
	 * but has not (yet) been killed. The counterpart to onResume(). 
	 */
	@Override
	public void onPause() {
		super.onPause();
		glv.onPause();

		if (player!=null) {
			if (player.isPlaying())
				player.stop();
		}
	}


	/**
	 * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(), for your activity
	 * to start interacting with the user
	 */
	@Override
	public void onResume() {
		super.onResume();
		glv.onResume();

		if (soundOn) {
			if (player!=null)
				player.release();
			player = MediaPlayer.create(this, R.raw.l50_music);
			player.setLooping(true);
			player.start();
		}
	}

	/**
	 * Performs final cleanup before an activity is destroyed. 
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();


		if (player!=null) {
			if (player.isPlaying())
				player.stop();
			player.release();
		}
	}

	/**
	 * Called when a key was pressed down and not handled by any of the views inside of the activity. 
	 * 
	 * @param keyCode Code of the key that has been pressed.
	 * @param event Event triggered by keystroke
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK) { 
			sessionState = glv.getState();
			setResult(Activity.RESULT_OK, sessionState.asIntent());

			if (myState != null) {
				myState.putFloat(BUNNY_Y, 0.0f);
				myState.putFloat(BUNNY_X, 0.0f);
				myState.putFloat(MAP_OFFSET_X, 0.0f);
				myState.putFloat(MAP_OFFSET_Y, 0.0f);
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Called to retrieve per-instance state from an activity before being killed so that the state
	 * can be restored in onCreate(Bundle).
	 * 
	 * @param outState Bundle containing the state of the activity.
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		myState = outState;
		glv.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}
}

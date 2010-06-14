package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.DoubleTap;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputListener;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.Swipe;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.sound.SoundPlayer;

/**
 * Level 44 (MireRabbit) level activity
 * 
 * This class provides the entry point for Level 44.
 * It also takes care of detecting gestures and managing
 * the life-cycle as well as forwarding input gestures
 * and managing the in-game music playback.
 * 
 * @author Matthias Tretter
 * @author Thomas Perl
 */

public class LevelActivity extends Activity {
	/** The Scene for displaying */
	private GameScene scene;
	/** detect input gestures */
	private GestureDetector gestureDetector;

	/**
	 * Create a new Level 44 LevelActivity
	 * 
	 * This will create a new full-screen landscape window and
	 * set up a GameScene inside it and finally start the game.
	 * 
	 * @see Activity.onCreate(Bundle b)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// call Garbace Collector
		System.gc();

		// always change Media Volume
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		/* Fullscreen window without title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		scene = new GameScene(this);

		if (savedInstanceState != null) {
			scene.restoreInstanceState(savedInstanceState);
		}

		setContentView(scene);

		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		gestureDetector = new GestureDetector(new InputListener(scene, display.getWidth(), display.getHeight()));
		new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};

		/* Default result when user exits the activity */
		SessionState s = new SessionState();
		s.setProgress(0);
		setResult(Activity.RESULT_OK, s.asIntent());
	}

	/**
	 * finish the level with a specific score
	 * @param score the score the user achieved
	 */
	public void finishLevel(int score) {
		// the SessionState is a convenience class to set a result
		SessionState s = new SessionState();
		// we set the progress the user has made (must be between 0-100)
		s.setProgress(score);
		// we call the activity's setResult method
		setResult(Activity.RESULT_OK, s.asIntent());
		// we finish this activity
		finish();
	}

	/**
	 * Called when Android wants us to resume the game
	 */
	@Override
	protected void onResume() {
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		// check if sounds effects are set and create soundPlayer
		SessionState state = new SessionState(getIntent().getExtras());
		if (state != null) {
			SoundPlayer.createInstance(getApplicationContext(), state.isMusicAndSoundOn());
		} else {
			SoundPlayer.createInstance(getApplicationContext(), false);
		}

		SoundPlayer.getInstance().startMusic();

		if (scene != null) {
			scene.onResume();
		}
	}

	/**
	 * Called when Android wants us to pause the game
	 */
	@Override
	protected void onPause() {
		super.onPause();

		SoundPlayer.getInstance().stopMusic();
		SoundPlayer.getInstance().release();

		if (scene != null) {
			scene.onPause();
		}
	}

	/**
	 * Process an incoming touchscreen event
	 * 
	 * @param event The touchscreen event
	 * @return The result of the gesture detector for this event
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	/**
	 * Process an incoming trackball event
	 * 
	 * @param event The trackball motion event
	 * @return This function always returns true
	 */
	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		if (scene != null) {
			InputGesture gesture = null;

			if (event.getY() < -.1f) {
				/* Up movement - flap both wings */
				gesture = new DoubleTap(0, 0);
			} else if (event.getX() < -.1f) {
				/* Left movement - flap right wing (go left) */
				gesture = new Swipe(0, Swipe.MAX_LENGTH, 0, 0, Swipe.MAX_VELOCITY * .7f, InputGesture.Position.RIGHT);
			} else if (event.getX() > .1f) {
				/* Right movement - flap left wing (go right) */
				gesture = new Swipe(0, Swipe.MAX_LENGTH, 0, 0, Swipe.MAX_VELOCITY * .7f, InputGesture.Position.LEFT);
			}

			if (gesture != null) {
				scene.addInputGesture(gesture);
			}
		}
		return true;
	}

	/**
	 * Called when Android wants us to save our state
	 * 
	 * @param outState A bundle into which to save the state
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (scene != null) {
			scene.saveInstanceState(outState);
		}
		super.onSaveInstanceState(outState);
	}

	/**
	 * Called when Android wants us to restore our state
	 * 
	 * @param savedInstanceState A bundle from where to restore the state
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if (scene != null) {
			scene.restoreInstanceState(savedInstanceState);
		}
	}

	/**
	 * Process a key input event
	 * 
	 * If the pressed key is the back button, finish
	 * the level right now and return the current score.
	 * 
	 * @param keyCode The keycode of the key
	 * @param event The event that occurred
	 * @return See Activity.onKeyDown
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finishLevel(scene.getScore());
		}

		return super.onKeyDown(keyCode, event);
	}
}

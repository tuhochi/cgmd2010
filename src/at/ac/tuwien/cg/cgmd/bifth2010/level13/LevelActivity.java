package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.GameObject;

/**
 * 
 * @author group13
 * 
 * main activity for game
 *
 */
public class LevelActivity extends Activity {
	/** custom renderer */
	private MyRenderer myRenderer;

	/** text view for fps-display */
	private TextView fpsTextView;

	/** string used in fps-text-view */
	private String fpsString;

	/** text view for money-display */
	private TextView moneyTextView;

	/** string used in money-text-view */
	private String moneyString;

	/** string containing money */
	private String money;

	/** text view for game-time-display */
	private TextView timeTextView;

	/** string used in time-text-view */
	private String timeString;

	/** string containing remaining time */
	private String time;

	/** thread for fps update */
	private Timer fpsUpdateTimer;

	/** thread for game-time update */
	private Timer gameTimeUpdateTimer;

	/** thread for money update */
	private Timer moneyUpdateTimer;

	/** flag indicating if fps should be displayed */
	private boolean fpsEnabled = false;



	/**
	 * creates the activity
	 * 
	 * @see super{@link #onCreate(Bundle)}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//reset all singleton objects
		SoundManager.reset();
		FPSCounter.reset();
		GameControl.reset();
		GameTimer.reset();
		GameObject.reset();

		//make window fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//setup layout with fps-overlay
		setContentView(R.layout.l13_level);
		myRenderer = (MyRenderer)findViewById(R.id.l13_MyRenderer);
		fpsTextView = (TextView)findViewById(R.id.l13_fpsTextView);
		timeTextView = (TextView)findViewById(R.id.l13_timeText);
		moneyTextView = (TextView)findViewById(R.id.l13_moneyText);

		//timer for fps display (called every second)
		fpsUpdateTimer = new Timer();
		fpsUpdateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				//set visibility according to fpsenabled
				if(fpsEnabled) {
					fpsTextView.setVisibility(View.VISIBLE);
				}
				else {
					fpsTextView.setVisibility(View.INVISIBLE);
				}
				//display fps
				FPSCounter counter = FPSCounter.getInstance();
				fpsString = "fps: " + Math.round(counter.getFPS());
				handleUIChanges.sendEmptyMessage(0);
			}
		}, 0, 1000);

		//timer for game time (called every 0.5 seconds)
		time = this.getString(R.string.l13_timeText);
		gameTimeUpdateTimer = new Timer();
		gameTimeUpdateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				//display game time
				GameTimer gameTimer = GameTimer.getInstance();
				timeString = time + ": " + gameTimer.getRemainingTimeString();
				handleUIChanges.sendEmptyMessage(0);
				//check if game time is over (if so, leave the activity)
				if(gameTimer.isOver()) {
					finish();
				}
			}
		}, 0, 500);


		//timer for money (called every 0.5 seconds)
		money = this.getString(R.string.l13_moneyText);
		moneyUpdateTimer = new Timer();
		moneyUpdateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				//display money
				moneyString = money + ": " + GameControl.getInstance().getMoney() + "$";
				handleUIChanges.sendEmptyMessage(0);
			}
		}, 0, 500);
	}

	/**
	 * responsible for handling ui-changes
	 */
	private Handler handleUIChanges = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			fpsTextView.setText(fpsString);
			timeTextView.setText(timeString);
			moneyTextView.setText(moneyString);
		}
	};


	/**
	 * resumes the activity
	 * 
	 * @see super{@link #onResume()}
	 */
	@Override
	protected void onResume() {
		super.onResume();
		//also resume renderer
		myRenderer.onResume();
	}

	/**
	 * pauses the activity
	 * 
	 * @see super{@link #onPause()}
	 */
	@Override
	protected void onPause() {
		super.onPause();
		//also pause renderer
		myRenderer.onPause();

		//stop music
		if(SoundManager.getInstance() != null) {
			SoundManager.getInstance().stop();
		}
	}


	/**
	 * stops the activity
	 * 
	 * @see super{@link #onStop()}
	 */
	@Override
	protected void onStop() {
		super.onStop();
		//stop music
		if(SoundManager.getInstance() != null) {
			SoundManager.getInstance().stop();
		}
	}


	/**
	 * destroys the activity
	 * 
	 * @see super{@link #onDestroy()}
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		//stop timers
		fpsUpdateTimer.cancel();
		gameTimeUpdateTimer.cancel();
		moneyUpdateTimer.cancel();
	}


	/**
	 * sends the game-result as an intent
	 * 
	 */
	@Override
	public void finish() {
		//send earned money back to caller
		SessionState s = new SessionState();
		s.setProgress(-GameControl.getInstance().getMoney()); 
		setResult(Activity.RESULT_OK, s.asIntent());
		super.finish();
	}


	/**
	 * saves all persistent values in this game
	 * 
	 * @see super{@link #onSaveInstanceState(Bundle)}
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		//save all important values
		GameControl.getInstance().save(outState);
		GameTimer.getInstance().save(outState);
		myRenderer.save(outState);
	}


	/**
	 * restores all important values
	 * 
	 * @see super{@link #onRestoreInstanceState(Bundle)}
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		//restore persistent values
		GameControl.getInstance().restore(savedInstanceState);
		GameTimer.getInstance().restore(savedInstanceState);
		myRenderer.restore(savedInstanceState);
	}


	/**
	 * handles menu-key press
	 * 
	 * @see super{@link #onKeyUp(int, KeyEvent)}
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_MENU) {
			//toggle fps-display with menu-button
			fpsEnabled = !fpsEnabled;
			return true;
		}
		return false;
	}
}

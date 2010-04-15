package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputListener;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.observer.Event;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.observer.Observer;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.observer.ShootEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.observer.TimeEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.CoinBucketSprite;

public class LevelActivity extends Activity implements Observer {
	private final Handler handler = new Handler();

	private GameScene scene;
	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;
	private TextView textCoins = null;
	private TextView textTime = null;
	private Runnable updateTime = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Fullscreen window without title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		scene = new GameScene(this);
		setContentView(scene);

		//setContentView(R.layout.l44_help);
		//finishLevel(90);

		LinearLayout llayout = new LinearLayout(this);
		llayout.setGravity(Gravity.TOP | Gravity.LEFT);
		llayout.setOrientation(LinearLayout.HORIZONTAL);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
		addContentView(llayout, params);

		textCoins = new TextView(this);
		textCoins.setText("" + CoinBucketSprite.FULL_COIN_COUNT);
		textCoins.setTextSize(25);
		textCoins.setGravity(Gravity.CENTER);
		textCoins.setTextColor(Color.BLACK);  
		textCoins.setBackgroundResource(R.drawable.l44_textbg);
		
		TextView spacer = new TextView(this);
		
		textTime = new TextView(this);
		textTime.setText(TimeManager.getFullTimeString());
		textTime.setTextSize(25);
		textTime.setGravity(Gravity.CENTER);
		textTime.setTextColor(Color.BLACK);  
		textTime.setBackgroundResource(R.drawable.l44_textbg);
		
		LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        llparams.weight = 0;
        llparams.setMargins(10, 10, 10, 10);
        llayout.addView(textCoins, llparams);
        LinearLayout.LayoutParams llparams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        llparams2.weight = 1;
        llparams2.setMargins(10, 10, 10, 10);
        llayout.addView(spacer, llparams2);
        llayout.addView(textTime, llparams);

		Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		gestureDetector = new GestureDetector(new InputListener(scene, display.getWidth(), display.getHeight()));
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};
	}

	private void finishLevel(int score) {
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public void notify(Event event) {
		if (event instanceof ShootEvent) {
			ShootEvent shootEvent = (ShootEvent)event;
			
			class UpdateUI implements Runnable {
				private String s;
				public UpdateUI(String s) {
					this.s = s;
				}
				@Override
				public void run() {
					textCoins.setText(s);
				}
			};

			Runnable r = new UpdateUI(event.toString());
			handler.post(r);
			
			// every coin lost -> finish level with full points
			if (shootEvent.getCoinCount() == 0) {
				this.finishLevel(100);
			}
		} else if (event instanceof TimeEvent) {
			class UpdateUI implements Runnable {
				private String s;
				public UpdateUI(String s) {
					this.s = s;
				}
				@Override
				public void run() {
					textTime.setText(s);
				}
			};

			Runnable r = new UpdateUI(event.toString());
			handler.post(r);
			
			// time is over -> quit level
			if (TimeManager.getInstance().getRemainingTimeMillis() == 0) {
				this.finishLevel((CoinBucketSprite.FULL_COIN_COUNT - scene.getRabbit().getCoinCount()) * 10);
			}
		}

	}
}

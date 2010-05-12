package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.camera.Camera;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.MotionManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.GameManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.TimeManager;

/**
 * The Class LevelActivity.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class LevelActivity extends Activity
{
	/** The Constant TAG. */
	public static final String TAG = "Signanzorbit";
	
	/** The Constant SCENE_STATE_KEY. */
	public static final String SCENE_STATE_KEY = "l42_state";
	
	/** The instance. */
	private static LevelActivity instance;
	
	private final SessionState sessionState;
	
	/** The render view. */
	private RenderView renderView;
	
	/** The time TextView. */
	private TextView time;
	
	/** The fps TextView. */
	private TextView fps;
	
	private ProgressBar scoreProgress;
	private ProgressBar timeProgress;
	
	/** The handler. */
	public final Handler handler;
	
	/** The fps update runnable. */
	public final Runnable fpsUpdateRunnable;
	
	/** The score update runnable. */
	public final Runnable scoreUpdateRunnable;
	
	/** The remaining game time runnable. */
	public final Runnable remainingGameTimeRunnable;
	
	/** The time manager. */
	private final TimeManager timeManager = TimeManager.instance;
	
	/** The vibrator. */
	private Vibrator vibrator;
	
	/** The PowerManagers Wake Lock */
	private PowerManager.WakeLock wakeLock;
	
	/**
	 * Instantiates a new level activity.
	 */
	public LevelActivity()
	{
		super();
		instance = this;
		handler = new Handler();
		fpsUpdateRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				fps.setText(timeManager.getFPS() + " fps");
			}
		};
		
		scoreUpdateRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				float scorePercent = GameManager.instance.getScorePercent();
				scoreProgress.setProgress((int)Math.ceil(100-scorePercent));
				sessionState.setProgress((int)Math.ceil(scorePercent)); 
				setResult(Activity.RESULT_OK, sessionState.asIntent());
			}
		};
		
		remainingGameTimeRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				int remainingSeconds = (int)(timeManager.getRemainingGameTime()/1000);
				float totalSeconds = Config.GAMETIME/1000;
				
				timeProgress.setProgress((int)(100-(remainingSeconds*100)/(totalSeconds)));
				
				if(remainingSeconds <= 0)
				{
					time.setText("Time's up!");
					finish();
					return;
				}
				int remainingMinutes = remainingSeconds/60;
				remainingSeconds -= remainingMinutes*60;
				
				String remainingSecondsString = (remainingSeconds < 10 ? "0" : "") + remainingSeconds;
				String remainingMinutesString = (remainingMinutes < 10 ? "0" : "") + remainingMinutes;
				
				time.setText(remainingMinutesString + ":" + remainingSecondsString);
			}
		};
		sessionState = new SessionState();
	}
	
	/**
	 * triggers the Vibrator
	 *
	 * @param millis The Milliseconds the Vibrator will be turned on for
	 */
	public void vibrate(long millis)
	{
		vibrator.vibrate(millis);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.v(TAG,"onCreate(" + savedInstanceState + ")");
		
		if(savedInstanceState == null)	// first start or restart
			timeManager.reset(true);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	Window window = getWindow();
	 	window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	 	setContentView(R.layout.l42_level);
		renderView = (RenderView)findViewById(R.id.l42_RenderView);
		//score = (TextView)findViewById(R.id.l42_scoreTextField);
		scoreProgress = (ProgressBar)findViewById(R.id.l42_scoreProgressbar);
		time = (TextView)findViewById(R.id.l42_timeTextField);
		timeProgress = (ProgressBar)findViewById(R.id.l42_timeProgressbar);
		fps = (TextView)findViewById(R.id.l42_fpsTextField);
		

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, TAG);
        
        sessionState.setProgress(0); 
		setResult(Activity.RESULT_OK, sessionState.asIntent());
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.v(TAG,"onStart()");
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.v(TAG,"onRestart()");
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.v(TAG,"onResume()");
		
		wakeLock.acquire();
		renderView.synchronizer.setActive(true);
		renderView.onResume();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.v(TAG,"onPause()");
		renderView.synchronizer.setActive(false);
		renderView.onPause();
		wakeLock.release();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop()
	{
		super.onStop();
		Log.v(TAG,"onStop()");
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.v(TAG,"onDestroy()");
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Log.v(TAG,"onSaveInstanceState(" + outState + ")");
		
		Scene scene = renderView.scene;
		Camera cam = renderView.cam;

		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			DataOutputStream dos = new DataOutputStream(baos);
			cam.persist(dos);
			scene.persist(dos);
			dos.close();
			baos.close();
			byte[] state = baos.toByteArray();
			outState.putByteArray(SCENE_STATE_KEY, state);
		}
		catch (Throwable t)
		{
			Log.e(TAG, "Failed to persist Scene state: ",t);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		Log.v(TAG,"onRestoreInstanceState(" + savedInstanceState + ")");
		
		byte[] state = savedInstanceState.getByteArray(SCENE_STATE_KEY);
		if(state != null)
		{
			try
			{
				ByteArrayInputStream bais = new ByteArrayInputStream(state);
				DataInputStream dis = new DataInputStream(bais);
				
				Scene scene = renderView.scene;
				Camera cam = renderView.cam;
				
				MotionManager.instance.reset();
				cam.restore(dis);
				scene.restore(dis);
				
				dis.close();
				bais.close();
			}
			catch (Throwable t)
			{
				Log.e(TAG, "Failed to restore Scene state: ",t);
			}
		}
	}

	/**
	 * Gets the single instance of LevelActivity.
	 *
	 * @return single instance of LevelActivity
	 */
	public static LevelActivity getInstance()
	{
		return instance;
	}
}

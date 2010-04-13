package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.camera.Camera;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.MotionManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.TimeManager;

public class LevelActivity extends Activity
{
	public static final String TAG = "Signanzorbit";
	public static final String SCENE_STATE_KEY = "l42_state";
	
	private static LevelActivity instance;
	
	private RenderView renderView;
	private TextView fps;
	private TextView score;
	
	public final Handler handler;
	public final Runnable fpsUpdateRunnable;
	public final Runnable scoreUpdateRunnable;
	
	private final TimeManager timeManager = TimeManager.instance;
	
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
				/*
				 * TODO: set correct score!
				 */
			}
		};
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.v(TAG,"onCreate(" + savedInstanceState + ")");
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	Window window = getWindow();
	 	window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	 	setContentView(R.layout.l42_level);
		renderView = (RenderView)findViewById(R.id.l42_RenderView);
		fps = (TextView)findViewById(R.id.l42_fpsTextField);
		score = (TextView)findViewById(R.id.l42_scoreTextField);
		score.setText("100.00%");
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.v(TAG,"onStart()");
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.v(TAG,"onRestart()");
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.v(TAG,"onResume()");
		
		renderView.synchronizer.setActive(true);
		renderView.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.v(TAG,"onPause()");
		renderView.synchronizer.setActive(false);
		renderView.onPause();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Log.v(TAG,"onStop()");
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.v(TAG,"onDestroy()");
	}
	
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

	public static LevelActivity getInstance()
	{
		return instance;
	}
}

package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class LevelActivity extends Activity
{
	public static final String TAG = "Signanzorbit";
	
	private static LevelActivity instance;
	
	private RenderView renderView;
	
	public LevelActivity()
	{
		super();
		instance = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.v(TAG,"onCreate(" + savedInstanceState + ")");
		
		/* Fullscreen window without title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	Window window = getWindow();
	 	window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	 	setContentView(R.layout.l42_level);
		renderView = (RenderView)findViewById(R.id.l42_RenderView); // seems to be null?!
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
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		Log.v(TAG,"onRestoreInstanceState(" + savedInstanceState + ")");
	}

	public static LevelActivity getInstance()
	{
		return instance;
	}
}

package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;

public class LevelActivity extends Activity
{
	public static final String TAG = "Signanzorbit";
	public static final String TRANSFORMATION_FILE = "l42_SceneState.bin";
	
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
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	Window window = getWindow();
	 	window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	 	setContentView(R.layout.l42_level);
		renderView = (RenderView)findViewById(R.id.l42_RenderView);
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
		
		try
		{
			DataInputStream dis = new DataInputStream(openFileInput(TRANSFORMATION_FILE));
			renderView.sceneStateFile = dis;
		}
		catch (FileNotFoundException e)
		{
			renderView.sceneStateFile = null;
		}
		
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
		
		Scene s = renderView.scene;
		
		if(s==null)
			return;
		
		try
		{
			DataOutputStream dos = new DataOutputStream(openFileOutput(TRANSFORMATION_FILE, MODE_PRIVATE));
			s.persist(dos);
			dos.close();
		}
		catch (Throwable t)
		{
			Log.e(TAG, "Failed to persist Scene state: ",t);
		}
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
		deleteFile(TRANSFORMATION_FILE);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Log.v(TAG,"onSaveInstanceState(" + outState + ")");
		outState.putCharSequence("testkey", "testvalue");
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

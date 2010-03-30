package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;

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
		
		Vector3 p = new Vector3(99,99,0);
		Vector3 q = new Vector3(888,888,0);
		Vector3 a = new Vector3(0,1,0);
		
		Log.d(TAG, "" + Vector3.crossProduct(p.subtract(a),q.normalize()).length());
		
		// thx @ lvl 11
		/* Fullscreen window without title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	Window window = getWindow();
	 	window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	 	setContentView(R.layout.l42_level);
		renderView = (RenderView)findViewById(R.id.l42_RenderView); // seems to be null?!
	}

	public static LevelActivity getInstance()
	{
		return instance;
	}
}

package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * The Intro Activity gives a brief description of how to play
 * @author Gerd Katzenbeisser
 */
public class IntroActivity extends Activity implements OnTouchListener{

	private static String TAG = "IntroActivity";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Starting IntroActivity");
		setContentView(R.layout.l77_intro);
		View v = (View) findViewById(R.id.l77_introview_frame);
		v.setOnTouchListener(this);
		v = (View) findViewById(R.id.l77_introview_image);
		v.setOnTouchListener(this);
	}


	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		Log.d(TAG, "Touching View " + v);
		this.finish();
		return false;
	}
}
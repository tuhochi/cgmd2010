package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import android.app.Activity;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class LevelActivity extends Activity
{
	public static final String TAG = "Signanzorbit";
	
	private RenderView renderView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.l42_level);
		renderView = (RenderView)findViewById(R.id.l42_RenderView);
	}
}

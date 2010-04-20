package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import android.app.Activity;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * The HelpActivity for test level 42. 
 */
public class HelpActivity extends Activity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.l42_help);
		// triggers an exception in the framework
//		setResult(Activity.RESULT_OK);
	}
}

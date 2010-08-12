package at.ac.tuwien.cg.cgmd.bifth2010.level70;

import android.app.Activity;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * Help activity.
 * 
 * @author Christoph Winklhofer
 */
public class HelpActivity extends Activity {

	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Create the help activity..
	 * @param Bundle
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.l70_help);
	}
}
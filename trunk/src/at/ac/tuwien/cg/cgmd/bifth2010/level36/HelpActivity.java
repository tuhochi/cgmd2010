package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * @author Alexander Schmid
 *
 */
public class HelpActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("Helpactivity", "onCreate");
		setContentView(R.layout.l36_help);
	}
}

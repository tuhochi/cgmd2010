package at.ac.tuwien.cg.cgmd.bifth2010.level23;

import android.app.Activity;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * The Class HelpActivity displays the help text and layout.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class HelpActivity extends Activity {

	/**
	 * called when the Activity is created 
	 */
	 public void onCreate(Bundle savedInstanceState) {
	 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.l23_help);
	}
}
package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class HelpActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("Helpactivity", "onCreate");
		setContentView(R.layout.l36_help);
	}
}

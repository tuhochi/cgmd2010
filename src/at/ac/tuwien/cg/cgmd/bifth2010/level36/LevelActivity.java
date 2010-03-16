package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * 
 * @author Gruppe 36
 *
 */
public class LevelActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set a layout
		setContentView(R.layout.l36_level);
		//get the button specified in the layout
		Button buttonFinish = (Button) findViewById(R.id.l36_ButtonFinish);
		//set a onClickListener to react to the user's click
		buttonFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//the SessionState is a convenience class to set a result
				SessionState s = new SessionState();
				//we set the progress the user has made (must be between 0-100)
				s.setProgress(10);
				//we call the activity's setResult method 
				setResult(Activity.RESULT_OK, s.asIntent());
				//we finish this activity
				LevelActivity.this.finish();
			}
		});
	}
}

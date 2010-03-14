package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * This activity demonstrates the basic interaction with the framework. When the Finish button is pressed the a result is set and the activity is finished. 
 * @author Peter
 *
 */
public class LevelActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set a layout
		setContentView(R.layout.l00_testlevel);
		//get the button specified in the layout
		Button buttonFinish = (Button) findViewById(R.id.l00_ButtonFinish);
		//set a onClickListener to react to the user's click
		buttonFinish.setOnClickListener(new OnClickListener(){
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

package at.ac.tuwien.cg.cgmd.bifth2010.level00;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * This activity demonstrates the basic interaction with the framework. When the Finish button is pressed the a result is set and the activity is finished. 
 * @author Peter
 *
 */
public class TestLevelActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set a layout
		setContentView(R.layout.l00_testlevel);
		//get the button specified in the layout
		
		TextView tv = (TextView) findViewById(R.id.l00_TextViewSessionState);

		
		//get the calling intent
		Intent callingIntent = getIntent();
		//get the session state
		SessionState state = new SessionState(callingIntent.getExtras());
		if(state!=null){
			int progress = state.getProgress();
			int level = state.getLevel();
			boolean isMusicAndSoundOn = state.isMusicAndSoundOn(); 
			
			
			String isOn = isMusicAndSoundOn ? "is on" : "is off";
			String s = "SessionState: level " + level + ", progress " + progress + ", music " + isOn;
			tv.setText(s);
		} else {
			tv.setText("No session state");
		}
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
				TestLevelActivity.this.finish();
			}
			
		});
	}
	
}

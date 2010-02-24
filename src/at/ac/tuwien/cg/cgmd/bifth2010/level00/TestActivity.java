package at.ac.tuwien.cg.cgmd.bifth2010.level00;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class TestActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SessionState s = new SessionState();
		s.setProgress(10);
		Intent data = new Intent();
		data.putExtras(s.asBundle());
		setResult(Activity.RESULT_OK, data);
		finish();
	}
	
	
}

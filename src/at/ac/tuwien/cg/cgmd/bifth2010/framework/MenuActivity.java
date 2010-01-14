package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import at.ac.tuwien.cg.cgmd.bifth2010.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * This activity lets the user choose which action to perform
 * @author Peter Rautek
 */

public class MenuActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l00_menu);
        ImageButton buttonStart = (ImageButton) findViewById(R.id.l00_ImageButtonStart);
        buttonStart.setOnClickListener(mStartClickListener);
        //TODO: add buttons for other actions like help, credits, etc.
    }
    
    private OnClickListener mStartClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intentMap = new Intent();
	        intentMap.setClassName(Constants.PACKAGE_NAME, MapActivity.class.getName());
			startActivity(intentMap);
		}
    	
    };
}
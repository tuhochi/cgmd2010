package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MapActivity extends Activity {
	private static final String CLASS_TAG = MapActivity.class.getName();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        //TODO load config file and set up level buttons and listeners accordingly
        ImageButton buttonStart = (ImageButton) findViewById(R.id.ImageButtonLevel1);
        buttonStart.setOnClickListener(mLevelClickListener);
    }
    
    private OnClickListener mLevelClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intentMap = new Intent();
	        intentMap.setClassName("org.groupid.template","GlActivity");
			startActivity(intentMap);
		}
    	
    };
}
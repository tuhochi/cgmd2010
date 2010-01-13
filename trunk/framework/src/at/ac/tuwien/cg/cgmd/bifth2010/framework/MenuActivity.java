package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MenuActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        ImageButton buttonStart = (ImageButton) findViewById(R.id.ImageButtonStart);
        buttonStart.setOnClickListener(mStartClickListener);
    }
    
    private OnClickListener mStartClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intentMap = new Intent();
	        intentMap.setClassName(MapActivity.class.getPackage().getName(), MapActivity.class.getName());
			startActivity(intentMap);
		}
    	
    };
}
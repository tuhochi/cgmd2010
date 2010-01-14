package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * 
 * @author Peter Rautek
 * This Activity displays a map showing the different levels.
 * It also visualizes the user's progress 
 *    
 */

public class MapActivity extends Activity {
	
	private static final String CLASS_TAG = MapActivity.class.getName();
	
	private MapView mMapView;
	private TextView mStatusText;
	private String mFpsString;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l00_map);
        LinearLayout ll = (LinearLayout) findViewById(R.id.l00_LinearLayout);
        mStatusText = (TextView) findViewById(R.id.l00_TextViewStatus); 
        
         /*
        //TODO add buttons and listeners for each level 
        ImageButton buttonStartLevel01 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel01);
        buttonStartLevel01.setOnClickListener(mLevelClickListener01);
        ImageButton buttonStartLevel02 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel02);
        buttonStartLevel02.setOnClickListener(mLevelClickListener02);
        */
        
        mMapView = new MapView(this);
        ll.addView(mMapView);
        
        Timer mFpsUpdateTimer = new Timer();
        mFpsUpdateTimer.schedule(new TimerTask(){

			@Override
			public void run() {
				handleUIChanges.sendEmptyMessage(0);
				float fps = mMapView.getFps();				
				mFpsString = "fps: " + Float.toString(fps);
			}
        }, 1000, 1000);
    }
    
    private Handler handleUIChanges = new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		mStatusText.setText(mFpsString);
			//mStatusText.invalidate();
    	}
    };
    
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    
    private OnClickListener mLevelClickListener01 = new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intentMap = new Intent();
	        intentMap.setClassName(Constants.PACKAGE_NAME, at.ac.tuwien.cg.cgmd.bifth2010.level01.GLActivity.class.getName());
			startActivity(intentMap);
		}
    };
    
    private OnClickListener mLevelClickListener02 = new OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intentMap = new Intent();
	        intentMap.setClassName(Constants.PACKAGE_NAME, at.ac.tuwien.cg.cgmd.bifth2010.level02.GlActivity.class.getName());
			startActivity(intentMap);
		}
    	
    };
}
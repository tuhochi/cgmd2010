package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class EntryPoint extends Activity {
	private static final String CLASS_TAG = EntryPoint.class.getName();
	
	/** Called when the activity is first created.
	 * Shows a splash screen 
	 * Starts the framework service
	 * Starts the Menu
	 * */
	
	private TextView mTextView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l00_main);
        mTextView = (TextView) findViewById(R.id.l00_TextViewEntry);
        
        //TODO: showing the splash screen
        //TODO: starting the service
        //TODO: starting the menu activity
        Intent intentMenu = new Intent();
        //intentMenu.setClassName(Constants.PACKAGE_NAME, MenuActivity.class.getName());
        intentMenu.setClassName(Constants.PACKAGE_NAME, MapActivity.class.getName());
		startActivity(intentMenu);
    }
    
    @Override
    protected void onRestart() {
    	super.onResume();
    	
    	//TODO: stopping the service
    	//TODO: show exit screen
    	Toast.makeText(this, "EntryPointActivity restarted -> we will finish", Toast.LENGTH_LONG).show();
    	finish();
    }
}
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
        Intent intent = new Intent(this, MenuActivity.class);
        //Intent intent = new Intent(this, AboutActivity.class);
        //Intent intent = new Intent(this, TextureTest.class);
        //Intent intent = new Intent(this, MapActivity.class);
        //Intent intent = new Intent(this, HelpActivity.class);
        //Intent intent = new Intent(this, CreditsActivity.class);
        
        startActivity(intent);
		finish();
    }
    
  
}
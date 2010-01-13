package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class EntryPoint extends Activity {
	private static final String CLASS_TAG = EntryPoint.class.getName();
	
	/** Called when the activity is first created.
	 * Shows a splash screen 
	 * Starts the framework service
	 * Starts the Menu
	 * */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //TODO: showing the splash screen
        //TODO: starting the service
        //TODO: starting the menu activity
        Intent intentMenu = new Intent();
        Log.d(CLASS_TAG, MenuActivity.class.getPackage().getName()+" "+ MenuActivity.class.getName());
        intentMenu.setClassName(MenuActivity.class.getPackage().getName(), MenuActivity.class.getName());
		startActivity(intentMenu);
    }
}
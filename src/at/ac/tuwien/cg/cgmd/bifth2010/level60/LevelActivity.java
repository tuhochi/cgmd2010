package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class LevelActivity extends Activity {
	private LevelSurfaceView glv;
	private TextView txtView;
	SharedPreferences prefs;
	private Bundle myState;
	
	private final static String BUNNY_X = "BUNNY_X";
	private final static String BUNNY_Y = "BUNNY_Y";
	private final static String MAP_OFFSET_X = "MAP_OFFSET_X";
	private final static String MAP_OFFSET_Y = "MAP_OFFSET_Y";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        glv = new LevelSurfaceView(this, savedInstanceState);
		setContentView(glv);
		
		myState = savedInstanceState;
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
		txtView = new TextView(this);
		this.addContentView(txtView,params);
		txtView.setText("juggling snowballs through hell....");
		return;
	}
	
	SessionState sessionState;
	
    @Override
 
    public void onPause() {
    //	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        super.onPause();
        glv.onPause();
        //sessionState = glv.saveData(prefs);
        
//        setResult(Activity.RESULT_OK, sessionState.asIntent());
    }

    @Override
    public void onResume() {
 //   	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        super.onResume();
        glv.onResume();
        //glv.updateData(prefs);
    }
    
	 @Override
    protected void onDestroy() {
    	super.onDestroy();
    //	finish();
    }
	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(keyCode==KeyEvent.KEYCODE_BACK) { 
				sessionState = glv.getState();
		    	setResult(Activity.RESULT_OK, sessionState.asIntent());
		    	
		    	/*SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		    	prefs.edit().remove("l60_posX");
		    	prefs.edit().remove("l60_posY");
		    	prefs.edit().remove("l60_mapOffset_x");
				prefs.edit().remove("l60_mapOffset_y");
//		    	prefs.edit().clear();
		    	prefs.edit().commit();
		    	*/
		    	if (myState != null) {
		    		myState.putFloat(BUNNY_Y, 0.0f);
		    		myState.putFloat(BUNNY_X, 0.0f);
		    		myState.putFloat(MAP_OFFSET_X, 0.0f);
		    		myState.putFloat(MAP_OFFSET_Y, 0.0f);
		    	}
				finish();
			}
			return super.onKeyDown(keyCode, event);
	 }
	 
	 @Override
		protected void onSaveInstanceState(Bundle outState) {
		 myState = outState;
		 glv.onSaveInstanceState(outState);
			super.onSaveInstanceState(outState);
		}
}

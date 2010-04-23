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
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        glv = new LevelSurfaceView(this);
		setContentView(glv);
		
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
		txtView = new TextView(this);
		this.addContentView(txtView,params);
		txtView.setText("juggling snowballs through hell....");
		return;
	}
	
	SessionState state;
	
    @Override
 
    public void onPause() {
    	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        super.onPause();
        glv.onPause();
        state = glv.saveData(prefs);
        
//        setResult(Activity.RESULT_OK, state.asIntent());
    }

    @Override
    public void onResume() {
    	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        super.onResume();
        glv.onResume();
        glv.updateData(prefs);
    }
    
	 @Override
    protected void onDestroy() {
    	super.onDestroy();
    //	finish();
    }
	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(keyCode==KeyEvent.KEYCODE_BACK) { 
				state = glv.getState();
		    	setResult(Activity.RESULT_OK, state.asIntent());
		    	
		    	SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		    	prefs.edit().putFloat("l60_posX", 0);
		    	prefs.edit().putFloat("l60_posY", 0);
		    	prefs.edit().putFloat("l60_mapOffset_x", 0);
				prefs.edit().putFloat("l60_mapOffset_y", 0);
		    	prefs.edit().clear();
		    	
				finish();
			}
			return super.onKeyDown(keyCode, event);
	 }
	 
}

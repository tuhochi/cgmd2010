package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level50.LevelObject;

public class LevelActivity extends Activity {
	private LevelSurfaceView glv;
	SharedPreferences prefs;
	private Bundle myState;
	MediaPlayer player;
	
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
		return;
	}
	
	SessionState sessionState;
	
    @Override
 
    public void onPause() {
        super.onPause();
        glv.onPause();
        
        if (player!=null) {
        	if (player.isPlaying())
        		player.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        glv.onResume();
        
        if (player!=null)
        	player.release();
        player = MediaPlayer.create(this, R.raw.l50_music);
		player.setLooping(true);
		player.start();
    }
    
	 @Override
    protected void onDestroy() {
    	super.onDestroy();
    	
    	
    	if (player!=null) {
			if (player.isPlaying())
				player.stop();
			player.release();
		}
		LevelObject.clean();

    }
	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(keyCode==KeyEvent.KEYCODE_BACK) { 
				sessionState = glv.getState();
		    	setResult(Activity.RESULT_OK, sessionState.asIntent());
		    	
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

package at.ac.tuwien.cg.cgmd.bifth2010.level50;


import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * Entry point for Level 50.
 * 
 * @author      Alexander Fritz
 * @author      Michael Benda
 */
public class LevelActivity extends Activity {
	
	MediaPlayer mp;
	
	/** 
     * Called when the activity is starting.
     * Initializes Display of the {@link SurfaceView}.
     *
     * @param savedInstanceState	If the activity is being re-initialized after
     * 								previously being shut down then this Bundle
     * 								contains the data it most recently supplied
     * 								in onSaveInstanceState(Bundle).
     * @see SurfaceView
     * @see onSaveInstanceState(Bundle)
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        SessionState s = new SessionState();
		s.setProgress(0);
		setResult(Activity.RESULT_OK, s.asIntent());

        mGLView = new LevelSurfaceView(this); 
   		setContentView(mGLView);
    }
	
	/** 
     * Called when a key was pressed down.
     * Handles the press of the back button. Every other event is passed on.
     * Before exiting the Level the {@link SessionState} is updated and
     * the {@link SharedPreferences} are cleared.
     *
     * @param keyCode	The value in event.getKeyCode().
     * @param event		Description of the key event. 
     * @return          <code>true</code> if the event equaled the usage of the back button
     * 					<code>false</code> otherwise. 
     * @see KeyEvent
     * @see #finish()
     * @see SessionState
     * @see SharedPreferences
     */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				
				SessionState s = new SessionState();
				s.setProgress(mGLView.getScore());
				setResult(Activity.RESULT_OK, s.asIntent());
				
		        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
		        SharedPreferences.Editor ed = mPrefs.edit();
		        ed.putInt("L50_SCORE", 0);
		        ed.putFloat("L50_POSX", mGLView.getPositionX());
		        ed.putFloat("L50_POSY", mGLView.getPositionY());
//		        ed.clear();
		        ed.commit();
		        
				finish();
				return true;
			}
		return super.onKeyDown(keyCode, event);
	}
				
	/** 
     * Called as part of the activity lifecycle when an activity is going into the background, but has not (yet) been killed.
     * Before pausing the Level the {@link SessionState} and the {@link SharedPreferences} are updated.
     * 
     * @see SessionState
     * @see SharedPreferences
     */
    @Override
    public void onPause() {
        super.onPause();
        mGLView.onPause();
        int score = mGLView.getScore();
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("L50_SCORE", score);
        ed.putFloat("L50_POSX", mGLView.getPositionX());
        ed.putFloat("L50_POSY", mGLView.getPositionY());        
        ed.putString("L50_COINS", mGLView.getCoinState());
        ed.commit();
        
        if (mp!=null) {
        	if (mp.isPlaying())
        		mp.stop();
        }
        
        SessionState s = new SessionState();
		s.setProgress(score);
		setResult(Activity.RESULT_OK, s.asIntent());
    }

    /** 
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(), for the activity to start interacting with the user.
     * Before pausing the Level the {@link SessionState} and the {@link SharedPreferences} are updated.
     * 
     * @see SessionState
     * @see SharedPreferences
     */
    @Override
    public void onResume() {
        super.onResume();
        mGLView.onResume();
        
        if (mp!=null)
        	mp.release();
        mp = MediaPlayer.create(this, R.raw.l50_music);
		mp.setLooping(true);
		mp.start();
        
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);        
        mGLView.setScore(mPrefs.getInt("L50_SCORE", 0));
        mGLView.setPosition(mPrefs.getFloat("L50_POSX", 10.0f), mPrefs.getFloat("L50_POSY", 0.0f));
        mGLView.setCoinState(mPrefs.getString("L50_COINS", ""));
    }
    
    /** 
     * Performs final cleanup before the activity is destroyed.
     * Releases any {@link MediaPlayer}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        mp.release();
        mGLView.clear();
		
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("L50_SCORE", 0);
//        ed.putFloat("L50_POSX", mGLView.getPositionX());
//        ed.putFloat("L50_POSY", mGLView.getPositionY());
        ed.clear();
        ed.commit();
    }
    
    /** 
     * Updates the {@link SessionState} with the current score.
     * 
     * @see SessionState
     */
    public void saveScore() {
    	SessionState s = new SessionState();
		s.setProgress(mGLView.getScore());
		setResult(Activity.RESULT_OK, s.asIntent());
    }
    
    /** 
     * 
     * @see SessionState
     */
    public void clearPrefs() {
    	SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("L50_SCORE", 0);
        ed.putFloat("L50_POSX", 0.0f);
        ed.putFloat("L50_POSY", 0.0f);        
        ed.putString("L50_COINS", "");
        ed.commit();
    }

    private LevelSurfaceView mGLView;

}

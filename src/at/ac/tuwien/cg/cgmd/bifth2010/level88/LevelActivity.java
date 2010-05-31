package at.ac.tuwien.cg.cgmd.bifth2010.level88;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.game.Game;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;

/**
 * Class for the general level activity of level 88
 * @author Asperger, Radax
 */
/**
 * @author Asperger
 *
 */
public class LevelActivity extends Activity{
	/** Public Tag of the level activity*/
	public static final String TAG = "LevelActivity"; 
	/** Handler for the level*/
	private final Handler handler = new Handler();
	/** GLSurfaceView instance of the level*/
	private GLSurfaceView glSurfaceView;
	/** GLView instance of the level*/
	private GLView normalModeView;
	/** Game instance of the level*/
	private Game game;
	/** Vector with the window size*/
	private Vector2 windowSize;
	/** Text for display*/
	private TextView goldText;
	//the url of the background music
	/** Url of the background music*/
	private static final String LOOP_URL = "android.resource://at.ac.tuwien.cg.cgmd.bifth2010/" + R.raw.l88_background;
	//boolean if the music should be played or not
	/** Set true if music should be played*/
	public boolean mMusicOn = false;
	//media player used for the background loop
	/** Media player used for the background loop*/
	private MediaPlayer mLoopPlayer = null;
	
	//kurze, eingebette Klasse fuer den Player
	/**
	 * wait for the MediaManager to prepare the audio loop
	 */
	private OnPreparedListener mSoundPrepListener = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			mLoopPlayer.setLooping(true);
			if(mMusicOn){
				//only play the music if sound is allowed
				mLoopPlayer.start();
			}
		}
	};
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	Window window = getWindow();
	 	window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        windowSize = new Vector2((float)d.getWidth(), (float)d.getHeight());
	 	
        /*
         * Create a new game
         */
	 	game = new Game(this, handler);

	 	/*
	 	 * Create the GLView and set it as the content view
	 	 */
	 	normalModeView = new GLView(this, game);
	 	glSurfaceView = normalModeView; 
        setContentView(glSurfaceView);
        
        /*
         * Create texts that are shown in the screen
         */
        LinearLayout llayout = new LinearLayout(this);
        llayout.setGravity(Gravity.TOP | Gravity.LEFT);
        llayout.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(llayout, params);
        
        goldText = new TextView(this);
        goldText.setTextSize(25);
        goldText.setGravity(Gravity.CENTER);
        goldText.setTextColor(Color.WHITE);  
        goldText.setBackgroundResource(R.drawable.l88_text);
        
        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        llparams.weight = 0;
        llparams.setMargins(10, 10, 10, 10);
        llayout.addView(goldText, llparams);
        
        /*
         * Create a listener for touch events
         */
        glSurfaceView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				game.touchEvent(new Vector2(event.getX() / windowSize.x, event.getY() / windowSize.y));
				return true;
			}
        });

        //get the information if sound is to be played or not
        SharedPreferences settings = this.getSharedPreferences(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
        mMusicOn = settings.getBoolean(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.PREFERENCE_MUSIC, true);
        
        updateTexts();
        
        mLoopPlayer = new MediaPlayer();
        mLoopPlayer.setOnPreparedListener(mSoundPrepListener);
        mLoopPlayer.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// on any error create a new media player
				mLoopPlayer.release();
				mLoopPlayer = MediaPlayer.create(LevelActivity.this, R.raw.l88_background);
				mLoopPlayer.setOnPreparedListener(mSoundPrepListener);
				mLoopPlayer.setOnErrorListener(this);
				return true;
			}
		});
        mLoopPlayer.setLooping(true);
        
        //updateTexts();
	}
	
	/**
	 * End the LevelActivity and set progress as result 
	 */
	public void endLevel() {
		//the SessionState is a convenience class to set a result
		SessionState s = new SessionState();
		//we set the progress the user has made (must be between 0-100)
		s.setProgress(100-game.gold);
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
		//stop the sounds
		game.player.destroy();
		LevelActivity.this.finish();
	}
	
	
	/**
	 * Update the screen texts 
	 */
	public void updateTexts() {
		goldText.setText(" " + getString(getResources().getIdentifier("l88_gold_text", "string", "at.ac.tuwien.cg.cgmd.bifth2010")) + game.gold + " ");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	public void onPause() {
		super.onPause(); 
		Log.d(TAG, "onPause()");
		glSurfaceView.onPause();
		if( game!=null ) {
			game.pause();
		}
		//stop the music if it is running
		if(mLoopPlayer != null){
			if(mLoopPlayer.isPlaying()){
				mLoopPlayer.pause();
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	public void onResume() {
		if((mLoopPlayer != null)&&(mMusicOn)){
			//prepare the mediaplayer for audio replay
			mLoopPlayer.reset();
			Uri uri = Uri.parse(LOOP_URL);
			try {
				mLoopPlayer.setDataSource(this, uri);
				mLoopPlayer.prepareAsync();
			} catch (IllegalArgumentException e) {
				Toast.makeText(this, "Audio replay is currently not working. Restarting the game or phone might help.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (SecurityException e) {
				Toast.makeText(this, "Audio replay is currently not working. Restarting the game or phone might help.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IllegalStateException e) {
				Toast.makeText(this, "Audio replay is currently not working. Restarting the game or phone might help.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(this, "Audio replay is currently not working. Restarting the game or phone might help.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
		
		super.onResume(); 
		Log.d(TAG, "onResume()");
		glSurfaceView.onResume();
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	protected void onStart()
	{
		super.onStart();
		Log.v(TAG, "onStart()");
	}
	

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	protected void onRestart()
	{
		super.onRestart();
		Log.v(TAG, "onRestart()");
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	protected void onStop()
	{
		super.onStop();
		Log.v(TAG, "onStop()");
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	protected void onDestroy()
	{
		//stop the music if it is running
		if(mLoopPlayer != null){
			if(mLoopPlayer.isPlaying()){
				mLoopPlayer.stop();
			}
			mLoopPlayer.release();
			mLoopPlayer = null;
		}
		super.onDestroy();
		Log.v(TAG, "onDestroy()");
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Log.v(TAG,"onSaveInstanceState(" + outState + ")");
		
		// Save Game
		outState.putInt("game.gold", game.gold);
		
		// Save Bunny
		outState.putInt("game.bunny.currentPosX", game.bunny.currentPosX);
		outState.putInt("game.bunny.currentPosY", game.bunny.currentPosY);
		outState.putInt("game.bunny.moveStatus", game.bunny.moveStatus);
		outState.putFloat("game.bunny.transition", game.bunny.transition);
		
		// Save Police
		for(int i=0; i<game.police.size(); i++) {
			outState.putInt("game.police.get("+i+").currentPosX", game.police.get(i).currentPosX);
			outState.putInt("game.police.get("+i+").currentPosY", game.police.get(i).currentPosY);
			outState.putInt("game.police.get("+i+").moveStatus", game.police.get(i).moveStatus);
			outState.putFloat("game.police.get("+i+").transition", game.police.get(i).transition);
			outState.putInt("game.police.get("+i+").distanceToLastBunnyPos", game.police.get(i).distanceToLastBunnyPos);			
		}

		// Save Stashes
		for(int i=0; i<game.stashes.size(); i++) {
			outState.putFloat("game.stashes.get("+i+").hideTime", game.stashes.get(i).hideTime);	
		}
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		Log.v(TAG,"onRestoreInstanceState(" + savedInstanceState + ")");
		
		// Restore Game
		game.gold = savedInstanceState.getInt("game.gold");
		
		// Restore Bunny
		game.bunny.setPosition(
				savedInstanceState.getInt("game.bunny.currentPosX"),
				savedInstanceState.getInt("game.bunny.currentPosY")
			);
		game.bunny.moveStatus = savedInstanceState.getInt("game.bunny.moveStatus");
		game.bunny.transition = savedInstanceState.getFloat("game.bunny.transition");
		
		// Restore Police
		for(int i=0; i<game.police.size(); i++) {
			game.police.get(i).setPosition(
					savedInstanceState.getInt("game.police.get("+i+").currentPosX"),
					savedInstanceState.getInt("game.police.get("+i+").currentPosY")
				);
			game.police.get(i).moveStatus = savedInstanceState.getInt("game.police.get("+i+").moveStatus");
			game.police.get(i).transition = savedInstanceState.getFloat("game.police.get("+i+").transition");
			game.police.get(i).distanceToLastBunnyPos = savedInstanceState.getInt("game.police.get("+i+").distanceToLastBunnyPos");
		}

		// Restore Stashes
		for(int i=0; i<game.stashes.size(); i++) {
			game.stashes.get(i).hideTime = savedInstanceState.getFloat("game.stashes.get("+i+").hideTime");
		}
		
		updateTexts();
	}
}

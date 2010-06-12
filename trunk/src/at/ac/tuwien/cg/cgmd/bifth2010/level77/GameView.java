package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import java.io.BufferedInputStream;
import java.util.Date;
import java.util.prefs.Preferences;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.preference.Preference;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * GameView is the view of the game implementation of group 77. 
 * @author Gerd Katzenbeisser
 *
 */
public class GameView extends GLSurfaceView
{

	protected static final String	TAG	= "GameView";
	L77Renderer renderer;
	
	
	Native jni;
	Audio audio;
	private int score;
	/**
	 * @return the score
	 */
	public int getScore()
	{
		return score;
	}


	private Callback<Integer> deleteMe;
	private SharedPreferences prefs;
	private SessionState sessionState;
	private long startTime;
	
	public GameView(Context context, final Callback<Integer> gameEnded, SessionState sessionState)
	{
		super(context);
		
		this.sessionState = sessionState;
	
		
		audio = new Audio(context, sessionState);
		deleteMe = gameEnded;
		
		Callback<Integer> updateScore = new Callback<Integer>()
		{
			
			@Override
			public void onSucces(Integer result)
			{
				Log.i(TAG, "Received new score: " + result);
				updateScore(result);
			}
			
			@Override
			public void onFailure(Throwable caught)
			{
				Log.e(TAG, "Error while updating score");
				caught.printStackTrace();
			}
		};
		
		Callback<Void> timeUp = new Callback<Void>()
		{
			
			@Override
			public void onSucces(Void result)
			{
				
				gameEnded.onSucces(getScore());
			}
			
			@Override
			public void onFailure(Throwable caught)
			{
				Log.e(TAG, "Error when ending game");
				caught.printStackTrace();
			}
		}; 
		
		// Play the theme song
		audio.playSound(Audio.BUNNY_BLOCK_THEME);
		
		// Set startDate
		startTime = System.currentTimeMillis();

		jni = new Native(context, audio, gameEnded, updateScore);
		
		// native depends on renderer vars initialised
		renderer = new L77Renderer(true, context, jni, timeUp);
		renderer.setStartTime(startTime);
		setRenderer(renderer);
		
	}
	
	/**
	 * TODO Javadoc
	 * @param sharedPreferences 
	 */
	public void onPause(SharedPreferences sharedPreferences) {
		super.onPause();
		SharedPreferences.Editor ed = sharedPreferences.edit();
		String state = jni.nativeGetSavedState();
		audio.stopAllSounds();
		Log.i(TAG + " onPause", "Length of serialized game data: " + state.length());
		//this does not work in java: if (state != "")
		if (state.length() != 0)
		{
			ed.putString("native", state);
		}
		ed.putInt("score", score);
		ed.putLong("dateOffset", System.currentTimeMillis() - startTime);
		ed.commit();
	}

	/**
	 * TODO Javadoc
	 */
	public void onResume(SharedPreferences sharedPreferences) {
		super.onResume();
		score = sharedPreferences.getInt("score", 0);
		String state = sharedPreferences.getString("native", "");
		jni.nativeRestoreSavedState(state);
		startTime = System.currentTimeMillis() - sharedPreferences.getLong("dateOffset", 0);
		renderer.setStartTime(startTime);
	}
	
	/**
	 * Touch Events are catched and sent to a native call.
	 */
	@Override
	public boolean onTouchEvent(final MotionEvent event)
	{
		float x = event.getX(), y = event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			jni.touchesBegan(x, y);
		if (event.getAction() == MotionEvent.ACTION_MOVE)
			jni.touchesMoved(x, y);
		if (event.getAction() == MotionEvent.ACTION_UP)
			jni.touchesEnded(x, y);
		
		// For development only
		renderer.onTouchEvent(event);
		
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Log.d(TAG, "Key down: " + keyCode);
		
		return true;
	}
	
	
	/**
	 * Updates the current score
	 * @param score
	 */
	private void updateScore(int score)
	{
		this.score = score;
		// TODO Display score
	}
}

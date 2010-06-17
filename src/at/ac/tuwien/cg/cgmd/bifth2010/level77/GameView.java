package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * GameView is the view of the game implementation of group 77.
 * 
 * @author Gerd Katzenbeisser
 * 
 */
public class GameView extends GLSurfaceView
{

	protected static final String	TAG	= "GameView";
	BlockRenderer					renderer;
	Native							jni;
	Audio							audio;
	private long					startTime;
	public int						score;

	/**
	 * @return the score
	 */
	public int getScore()
	{
		return score;
	}

	/**
	 * Constructor for a GameView. It initializes Audio and uses a BlockRenderer
	 * as the renderer.
	 * 
	 * @param context
	 *            The context this GameView lives in
	 * @param gameEnded
	 *            Callback when game has ended
	 * @param sessionState
	 *            The session state
	 */
	public GameView(Context context, final Callback<Integer> gameEnded, SessionState sessionState)
	{
		super(context);
		audio = new Audio(context, sessionState);
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

		jni = new Native(context, audio, gameEnded, updateScore);

		// native depends on renderer vars initialised
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

		renderer = new BlockRenderer(context, jni, timeUp, audio);
		renderer.setDateOffset(startTime);
		setRenderer(renderer);
	}

	/**
	 * Saves the game state.
	 * 
	 * @param sharedPreferences
	 */
	public void onPause(SharedPreferences sharedPreferences)
	{
		super.onPause();
		SharedPreferences.Editor ed = sharedPreferences.edit();
		String state = jni.nativeGetSavedState();
		audio.stopAllSounds();
		Log.i(TAG + " onPause", "Length of serialized game data: " + state.length());
		// this does not work in java: if (state != "")
		if (state.length() != 0)
		{
			ed.putString("native", state);
		}
		Log.v(TAG, "onPause receives length " + state.length());

		ed.putInt("score", score);
		ed.putLong("dateOffset", System.currentTimeMillis() - startTime);
		ed.commit();
	}

	/**
	 * Restores the game state
	 */
	public void onResume(SharedPreferences sharedPreferences)
	{
		super.onResume();
		score = sharedPreferences.getInt("score", 0);
		String state = sharedPreferences.getString("native", "");
		Log.v(TAG, "*received from native state length " + state.length());
		jni.nativeRestoreSavedState(state);
		long dateOffset = sharedPreferences.getLong("dateOffset", 0);
		renderer.setDateOffset(dateOffset);
	}
	
	/**
	 * Releases DSs of the game
	 */
	public void onDestoy() 
	{
		//de-init
		jni.deInit();
	}

	/**
	 * Touch Events are catched and sent to a native handler.
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
	 * Not implemented
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Log.d(TAG, "Key down: " + keyCode);

		return true;
	}

	/**
	 * Updates the current score
	 * 
	 * @param score
	 *            the new score value
	 */
	private void updateScore(int score)
	{
		this.score = score;
		Log.i("l77callback", "scores now "+ score);
	}
}

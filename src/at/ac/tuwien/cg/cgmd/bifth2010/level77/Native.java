package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * Wrapper class for native calls
 * @author Gerd Katzenbeisser
 *
 */
public class Native
{
	
	public native void nativePause();
	public native void nativeResume();
	public native void nativeTouchesBegan(int [] touches);
	public native void nativeTouchesMoved(int [] touches);
	public native void nativeTouchesEnded(int [] touches);

	private final static int BLOCK_DROPPED_SOUND = 0;
	public static final int BLOCK_SWAPPED_SOUND = 1;
	private final static int BLOCK_EXPLODE_SOUND = 2;	
	
	private Audio audio;
	private Context context;
	private Callback<Integer> callbackGameEnded;
	private Callback<Integer> callbackUpdateScore;
	
	/**
	 * Constructor for Native object
	 * @param context
	 * @param audio
	 * @param gameEnded
	 * @param updateScore
	 */
	public Native(Context context, Audio audio, Callback<Integer> gameEnded, Callback<Integer> updateScore)
	{
		this.audio = audio;
		this.context = context;
		this.callbackGameEnded = gameEnded;
		this.callbackUpdateScore = updateScore;
	}
	
	/**
	 * Forwards call to native touchesBegan(int [])
	 * @param x position as float
	 * @param y position as float
	 */
	public void touchesBegan(float x, float y)
	{
		nativeTouchesBegan(singleTouch(x, y));
	}
	
	/**
	 * Forwards call to native touchesMoved(int [])
	 * @param rawX
	 * @param rawY
	 */
	public void touchesMoved(float x, float y)
	{
		nativeTouchesMoved(singleTouch(x,y));
	}
	
	/**
	 * Forwards call to native touchesEnded(int [])
	 * @param x position as float value
	 * @param y position as float value
	 */
	public void touchesEnded(float x, float y)
	{
		nativeTouchesEnded(singleTouch(x,y));		
	}
	
	/**
	 * Creates int array for a single touch
	 * @param x position as int value
	 * @param y position as int value
	 * @return native call compatible touch position array
	 */
	private int [] singleTouch(int x, int y)
	{
		int [] ret = new int[2];
		ret[0] = x;
		ret[1] = y;
		return ret;
	}
	
	/**
	 * Creates an int array for a single touch with float parameters
	 * @param x x position as float value
	 * @param y y position as float value
	 * @return native call compatible touch position array
	 */
	private int [] singleTouch(float x, float y)
	{
		return singleTouch((int) x, (int) y);
	}
	
	/**
	 * Called by native code to play a sound
	 * @param soundid
	 */
	@SuppressWarnings("unused")
	private void playSound(int soundid)
	{
		switch (soundid) {
			case BLOCK_DROPPED_SOUND:
				audio.playSound(Audio.BLOCK_DROPPED_SOUND);
				break;
			case BLOCK_SWAPPED_SOUND:
				audio.playSound(Audio.BLOCK_SWAPPED_SOUND);
				break;
			case BLOCK_EXPLODE_SOUND:
				audio.playSound(Audio.BLOCK_EXPLODE_SOUND);
				break;
			default:
				System.out.println("Request to play unknown soundid " + soundid);
				break;
		}		
	}
	
	/**
	 * Called by native code to update the score
	 * @param score
	 */
	private void updateScore(int score)
	{
		if (score >= 0)
			callbackUpdateScore.onSucces(score);
		else
			callbackUpdateScore.onFailure(new Throwable("Native code sent negative score: " + score));			
	}
	
	/**
	 * Called by native code when game is ended
	 * @param score
	 */
	@SuppressWarnings("unused")
	private void gameEnded(int score)
	{
		if (score >= 0)
			callbackGameEnded.onSucces(score);
		else
			callbackGameEnded.onFailure(new Throwable("Game ended with negative score: " + score));
	}

}

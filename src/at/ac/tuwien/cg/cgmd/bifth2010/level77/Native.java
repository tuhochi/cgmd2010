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
	
	// Getting and saving state
	public native String nativeGetSavedState();
	public native void nativeRestoreSavedState(String string);
	
	//drawing moved from render
	private static native void nativeInit();
	private static native void nativeInitClasses();

	// rendering 
    private static native void nativeDone();
    private static native void nativeRender();
    private static native void nativeResize(int w, int h);
    
    // TODO @Mike can this be deleted?
    //debug
    private static native int nativeNumDrawn();
	
	public native void nativeRegisterCallbacks(Audio a, Images i);

	private static final int BLOCK_DROPPED_SOUND = 0;
	private static final int BLOCK_SWAPPED_SOUND = 1;
	private static final int BLOCK_EXPLODE_SOUND_1 = 2;
	private static final int BLOCK_EXPLODE_SOUND_2 = 3;
	private static final int BLOCK_EXPLODE_SOUND_3 = 4;
	private static final int BLOCK_EXPLODE_SOUND_4 = 5;
	private static final int BLOCK_SOLVE_SOUND = 6;
	
	private Audio audio;
	private Images images;
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
		this.images = new Images(this.context);
	
	}
	
	/**
	 * Calls nativeInit()
	 * <p>
	 * This must be run after(!) renderer creates a context
	 */
	public void init()
	{	
 		nativeInit();
		nativeRegisterCallbacks(this.audio, this.images);
	}
	
	/**
	 * Loads images and calls nativeInitClasses()
	 */
	public void initClasses()
	{
		//TODO @Mike replace with callbacks! 
 		this.images.loadImages(); 		
 		nativeInitClasses();
	}
	
	/**
	 * Calls nativeRender()
	 * <p>
	 * OpenGl drawing in native code.
	 */
	public void render()
	{
		nativeRender();	
	}
	
	/**
	 * Calls nativeDone()
	 * <P>
	 * Finish everything in native part.
	 */
	public void deInit()
	{
		nativeDone();
	}
	
	/**
	 * Calls nativeResize(int, int)
	 * <p>
	 * Tells the native implementation that the view has resized.
	 * @param w The width
	 * @param h The height
	 */
	public void resizeView(int w, int h)
	{
		nativeResize(w, h);
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
			case BLOCK_EXPLODE_SOUND_1:
				audio.playSound(Audio.BLOCK_EXPLODE_SOUND_1);
				break;
			case BLOCK_EXPLODE_SOUND_2:
				audio.playSound(Audio.BLOCK_EXPLODE_SOUND_2);
				break;
			case BLOCK_EXPLODE_SOUND_3:
				audio.playSound(Audio.BLOCK_EXPLODE_SOUND_3);
				break;
			case BLOCK_EXPLODE_SOUND_4:
				audio.playSound(Audio.BLOCK_EXPLODE_SOUND_4);
				break;
			case BLOCK_SOLVE_SOUND:
				audio.playSound(Audio.BLOCK_SOLVE_SOUND);
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
	@SuppressWarnings("unused")
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

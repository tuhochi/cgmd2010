package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * 
 * @author group13
 * 
 * class for playing sound effects / music
 *
 */
public class SoundManager {

	/** enumeration of existing sounds **/
	public enum SoundFX {POLICE,DRUNK,BURP,ORGASM};
	
	/** to handle multiple sounds at once **/
	private int soundamount = 8;
	
	/** soundpool for playing sounds **/
	private SoundPool soundPool;
	
	/** hashmap with all available sounds **/
	private HashMap<SoundFX, Integer> soundMap;
	
	/** stream id of sound playing */
	private int streamID;
	
	/** flag if sound is enabled/disabled **/
	private boolean soundOn = true;
	
	/** media player for background music */
	private MediaPlayer mediaPlayer; 
	
	/** singleton object of this class */
	private static SoundManager instance;
	
	
	/**
	 * resets the singleton object
	 */
	public static void reset() {
		instance = null;
	}
	
	
	/**
	 * gets the singleton object of this class
	 * 
	 * @return singleton object of this class
	 */
	public static SoundManager getInstance() {
		return instance;
	}
	
	
	/**
	 * initializes all members
	 * 
	 * @param context context
	 */
	public static void init(Context context) {
		if(instance == null) {
			instance = new SoundManager(context);
		}
	}
	
	
	/**
	 * creates a new SoundManager
	 * 
	 * @param context context
	 */
	public SoundManager(Context context)
	{
		//init members
		streamID = -1;
		mediaPlayer = MediaPlayer.create(context, R.raw.l13_music);
		//loop background music
		mediaPlayer.setLooping(true);
		soundPool = new SoundPool(soundamount, AudioManager.STREAM_MUSIC, 100);
		
		//check if sound is enabled
		SharedPreferences audiosettings = context.getSharedPreferences(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
		soundOn = audiosettings.getBoolean(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.PREFERENCE_MUSIC, true);
		
		//populate sound map
		soundMap = new HashMap<SoundFX, Integer>();
		soundMap.put(SoundFX.POLICE, soundPool.load(context,R.raw.l13_police,1));
		soundMap.put(SoundFX.DRUNK, soundPool.load(context,R.raw.l13_drunk,1));
		soundMap.put(SoundFX.BURP, soundPool.load(context, R.raw.l13_burp, 1));
		soundMap.put(SoundFX.ORGASM, soundPool.load(context, R.raw.l13_orgasm, 1));
	}
	
	/**
	 * play available sounds
	 * 
	 * @param sound sound to be played
	 */
	public void playSound(SoundFX sound)
	{
		if(soundOn)
		{
			streamID = soundPool.play(soundMap.get(sound),1.0f, 1.0f, 1, 0, 1.0f);	
		}
	}
	
	
	/**
	 * stops currently playing sound
	 */
	public void stopSounds() {
		if(soundOn) {
			soundPool.stop(streamID);
		}
	}
	
	
	/**
	 * starts the background music
	 */
	public void startMusic() {
		if(soundOn) {
			mediaPlayer.start();
		}
	}
	
	
	/**
	 * stops all sounds/music
	 */
	public void stop(){
		mediaPlayer.stop();
		soundPool.stop(streamID);
		reset();
	}
}
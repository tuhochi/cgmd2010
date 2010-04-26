package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class SoundManager {

	/** enumeration of existing sounds **/
	public enum SoundFX {DROP, HIT, MISS};
	private int soundamount = 3;
	/** soundpool for playing sounds **/
	private SoundPool soundPool;
	/** hashmap with all available sounds **/
	private HashMap<SoundFX, Integer> soundMap;
	
	/** flag if sound is enabled/disabled **/
	private boolean soundOn = true;
	
	
	/**
	 * create a new SoundManager
	 * @param context
	 */
	public SoundManager(Context context)
	{
		soundPool = new SoundPool(soundamount, AudioManager.STREAM_MUSIC, 100);
		initSoundMap(context);
		
		SharedPreferences audiosettings = context.getSharedPreferences(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
		soundOn = audiosettings.getBoolean(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.PREFERENCE_MUSIC, true);
	}
	
	/**
	 * init the soundmap with soundeffects
	 * @param context
	 */
	private void initSoundMap(Context context)
	{
		soundMap = new HashMap<SoundFX, Integer>();
	
		soundMap.put(SoundFX.DROP, soundPool.load(context,R.raw.l44_shot,1));
		soundMap.put(SoundFX.HIT, soundPool.load(context,R.raw.l44_shot,1));
	}
	
	/**	SoundPool.play  (int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
		soundID 	a soundID returned by the load() function
		leftVolume 	left volume value (range = 0.0 to 1.0)
		rightVolume 	right volume value (range = 0.0 to 1.0)
		priority 	stream priority (0 = lowest priority)
		loop 	loop mode (0 = no loop, -1 = loop forever)
		rate 	playback rate (1.0 = normal playback, range 0.5 to 2.0)
	**/
	
	/**
	 * play available sounds
	 * @param sound
	 */
	public void playSound(SoundFX sound)
	{
		if (soundOn)
		{
			soundPool.play(soundMap.get(sound),1.0f, 1.0f, 1, 0, 1.0f);
		}
	}
	
	
}

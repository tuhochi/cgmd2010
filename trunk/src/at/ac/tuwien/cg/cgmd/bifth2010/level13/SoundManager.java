package at.ac.tuwien.cg.cgmd.bifth2010.level13;


import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class SoundManager {

	/** enumeration of existing sounds **/
	public enum SoundFX {POLICE,DRUNK,MUSIC};
	//to handle multiple sounds at onece
	private static int soundamount = 8;
	/** soundpool for playing sounds **/
	private static SoundPool soundPool;
	/** hashmap with all available sounds **/
	private static HashMap<SoundFX, Integer> soundMap;
	private static int musicID;
	/** flag if sound is enabled/disabled **/
	private static boolean soundOn = true;
	private static boolean musicInit = false;
	
	/**
	 * create a new SoundManager
	 * @param context
	 */
	public static void initSoundManager(Context context)
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
	private static void initSoundMap(Context context)
	{
		
		soundMap = new HashMap<SoundFX, Integer>();
	
		soundMap.put(SoundFX.POLICE, soundPool.load(context,R.raw.l13_police,1));
		soundMap.put(SoundFX.DRUNK, soundPool.load(context,R.raw.l13_drunk,1));
		soundMap.put(SoundFX.MUSIC, soundPool.load(context,R.raw.l13_music,2 ));
		
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
	public static void playSound(SoundFX sound)
	{
		if (soundOn)
		{
		//	soundPool
			if(sound == SoundFX.MUSIC && !musicInit ){
				musicInit = true;
				musicID = soundPool.play(soundMap.get(sound),1.0f, 1.0f, 1, -1, 1.0f);
			}else if(sound == SoundFX.MUSIC){
				soundPool.resume(musicID);
			}else{
				soundPool.play(soundMap.get(sound),1.0f, 1.0f, 1, 0, 1.0f);
				
			}
		}
	}
	
	public static void stopMusic(){
		soundPool.stop(musicID);
		
}
	
	
	public static void pauseMusic(){
		soundPool.pause(musicID);
		
}
	
	
}
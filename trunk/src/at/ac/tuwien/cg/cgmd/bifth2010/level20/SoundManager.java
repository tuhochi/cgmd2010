package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * Loads audio resources and is responsible for playing/stopping sounds.
 * 
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 * @author level13
 */
public class SoundManager {
	
	/** Enumeration of used sounds. **/
	protected enum SOUNDS { RUN, LAUGH_1, LAUGH_2, LAUGH_3, CRASH };
	/** Number of used sounds. */
	private int nSounds;	
	/** Necessary for playing sounds. **/
	private SoundPool soundPool;
	/** Stores the loaded sounds. */
	private HashMap<SOUNDS, Integer> sounds;
	
	private int musicID;
	/** Flag indicating if sound is en-/disabled. */
	private boolean soundOn;
	/** Flag whether music was initialized. */
	private boolean musicInit = false;	
	
	public SoundManager(Context context) {
		soundOn = true;
		musicInit = false;
		nSounds = 5;
		sounds = new HashMap<SOUNDS, Integer>(nSounds);
		init();
	}

	
	/**
	 * Initializes the SoundManager.
	 */
	private void init()
	{
		soundPool = new SoundPool(nSounds, AudioManager.STREAM_MUSIC, 100);
		loadSounds(LevelActivity.instance);
		
		// FERDI: Funktioniert leider nicht. 
//		SharedPreferences audiosettings = context.getSharedPreferences(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
//		soundOn = audiosettings.getBoolean(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.PREFERENCE_MUSIC, true);
		
		// So sollts laut FAQ aber gehn:
		
		// Get the calling intent & the session state
		Intent callingIntent = LevelActivity.instance.getIntent();
		SessionState state = new SessionState(callingIntent.getExtras());
		
		if (state != null) {
			soundOn = state.isMusicAndSoundOn(); 
		}
		
	}
	
	/**
	 * Loads the used sounds into the manager.
	 * @param context The context needed to load the resources.
	 */
	private void loadSounds(Context context)
	{			
		sounds.put(SOUNDS.RUN, soundPool.load(context,R.raw.l20_bunny_run,1));
		sounds.put(SOUNDS.LAUGH_1, soundPool.load(context,R.raw.l20_bunny_laugh1,1));
		sounds.put(SOUNDS.LAUGH_2, soundPool.load(context,R.raw.l20_bunny_laugh2,2 ));
		sounds.put(SOUNDS.LAUGH_3, soundPool.load(context, R.raw.l20_bunny_laugh3, 1));
		sounds.put(SOUNDS.CRASH, soundPool.load(context, R.raw.l20_bunny_crash, 1));
		
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
	 * Starts/continues playing the specified sound.
	 * @param The identifier for the sound to play.
	 */
	public void playSound(SOUNDS sound)
	{
		if (soundOn)
		{
			if(sound == SOUNDS.RUN && !musicInit ){
				musicInit = true;
				musicID = soundPool.play(sounds.get(sound), 1.0f, 1.0f, 1, -1, 1.0f);
			}else if(sound == SOUNDS.RUN){
				soundPool.resume(musicID);
			}else{
				soundPool.play(sounds.get(sound), 1.0f, 1.0f, 1, 0, 1.0f);
			}
		}
	}
	
	/** Stops the continuously playing background sound. */
	public void stopMusic(){
		soundPool.stop(musicID);	
	}
	
	/** Pauses the continuously playing background sound. */
	public void pauseMusic(){
		soundPool.pause(musicID);		
	}
	
	/** Plays a laughing sound. */
	public void laugh() {
		int laugh = ((int)(Math.random() * 10)) % 3;
		switch (laugh) {
		case 0:
			playSound(SOUNDS.LAUGH_1);
			break;
		case 1:
			playSound(SOUNDS.LAUGH_2);
			break;
		case 2:
			playSound(SOUNDS.LAUGH_3);
			break;
		default:
			break;
		}
	}
	
}

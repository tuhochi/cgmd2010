package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnErrorListener;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * Loads audio resources and is responsible for playing/stopping sounds.
 * 
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 * @author level13
 * @author level33
 */
public class SoundManager {
	
	/** Enumeration of used sounds. **/
	protected enum SOUNDS { LAUGH_1, LAUGH_2, LAUGH_3, CRASH };
	/** MediaPlayer responsible for playing the background music. */
	private MediaPlayer musicPlayer;
	/** MediaPlayer responsible for playing the running sound. */
	private MediaPlayer runningPlayer;
	/** Necessary for playing sounds. **/
	private SoundPool soundPool;
	/** Stores the loaded sounds. */
	private HashMap<SOUNDS, Integer> sounds;	
	/** Flag indicating if sound is en-/disabled. */
	private boolean soundOn;
	/** The global volume of the sounds and music. */
	float volume;
	
	public SoundManager(Context context) {
		soundOn = true;
		volume = 1.0f;
		sounds = new HashMap<SOUNDS, Integer>(SOUNDS.values().length);
		init(context);
	}
	

	/**
	 * Initializes the SoundManager.
	 * @param context The context of the game.
	 */
	private void init(Context context)
	{
		soundPool = new SoundPool(SOUNDS.values().length, AudioManager.STREAM_MUSIC, 100);
		loadSounds(LevelActivity.instance);
		
		musicPlayer = MediaPlayer.create(context, R.raw.l20_music);
		musicPlayer.setLooping(true);	
		
		runningPlayer = MediaPlayer.create(context, R.raw.l20_bunny_run);
		runningPlayer.setLooping(true);			
		
		// Get the calling intent & the session state
		Intent callingIntent = LevelActivity.instance.getIntent();
		SessionState state = new SessionState(callingIntent.getExtras());
		
		if (state != null) {
			soundOn = state.isMusicAndSoundOn(); 
		}		
		
		AudioManager audioM = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		volume = (float)audioM.getStreamVolume(AudioManager.STREAM_MUSIC)/(float)audioM.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		musicPlayer.setVolume(volume, volume);
		runningPlayer.setVolume(volume*0.3f, volume*0.3f);
	}
	
	/**
	 * Loads the used sounds into the manager.
	 * @param context The context needed to load the resources.
	 */
	private void loadSounds(Context context)
	{	
		sounds.put(SOUNDS.LAUGH_1, soundPool.load(context,R.raw.l20_bunny_laugh1, 1));
		sounds.put(SOUNDS.LAUGH_2, soundPool.load(context,R.raw.l20_bunny_laugh2, 1));
		sounds.put(SOUNDS.LAUGH_3, soundPool.load(context, R.raw.l20_bunny_laugh3, 1));
		sounds.put(SOUNDS.CRASH, soundPool.load(context, R.raw.l20_bunny_crash, 2));
		
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
			soundPool.play(sounds.get(sound), volume, volume, 1, 0, 1.0f);
		}
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
	
	/** Stops running sounds.*/
	public void stopSounds() {
		stopMusic();
		stopRunSound();
	}
	
	/**
	 * Starts the MediaPlayer which plays the background music.
	 */
	public void startMusic() {		
		if (soundOn && musicPlayer != null && !musicPlayer.isPlaying())
		{
			musicPlayer.start();
		}
	}
	
	/**
	 * Pauses the MediaPlayer which plays the background music.
	 */
	public void pauseMusic() {		
		if(musicPlayer != null && musicPlayer.isPlaying())
			musicPlayer.pause();
		
	}
	
	/**
	 * Resumes the MediaPlayer which plays the background music.
	 */
	public void resumeMusic() {		
		if(soundOn &&  musicPlayer != null && !musicPlayer.isPlaying())
			musicPlayer.start();		
	}
	
	/**
	 * Stops the MediaPlayer which plays the background music.
	 */
	public void stopMusic() {		
		if(musicPlayer != null)
			musicPlayer.stop();		
	}
	
	/**
	 * Starts the MediaPlayer which plays the background music.
	 */
	public void startRunSound() {		
//		if(runningPlayer != null && !runningPlayer.isPlaying() )
//		{
//			runningPlayer.start();
//		}
	}
	
	
	/**
	 * Stops the MediaPlayer which plays the background music.
	 */
	public void stopRunSound() {		
		if(runningPlayer != null)
			runningPlayer.stop();		
	}
	
	/**
	 * Releases the MediaPlayer which plays the background music.
	 */
	public void destroy() {
		
		//Stop music if it's running
		if (musicPlayer != null ) {
			if (musicPlayer.isPlaying())
				musicPlayer.stop();
			musicPlayer.release();
			musicPlayer = null;
		}
		if (runningPlayer != null ) {
			if (runningPlayer.isPlaying())
				runningPlayer.stop();
			runningPlayer.release();
			runningPlayer = null;
		}
		
		//Release the SoundPool
		soundPool.release();
		
	}
}

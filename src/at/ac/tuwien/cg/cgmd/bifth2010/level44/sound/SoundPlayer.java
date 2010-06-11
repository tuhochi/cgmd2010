package at.ac.tuwien.cg.cgmd.bifth2010.level44.sound;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * Class for playing SoundEffects
 * 
 * @author Matthias
 */

public class SoundPlayer {
	/** The Sound Effects we support */
	public enum SoundEffect { LOAD, SHOT, FLAP, DROP, BEEP, END };

	/** SoundPool for playing sounds */
	private SoundPool soundPool;
	/** Map of all sounds */
	private HashMap<SoundEffect, Integer> sounds;
	/** the volume set by the user */
	private float volume = 1.0f;
	/** does the user want to play sound effects? */
	private boolean musicOn = true;
	/** Singleton-Object */
	private static SoundPlayer instance = null;
	/** the Context */
	private static Context context = null;

	/**
	 * create the Singleton-Object
	 * @param context the context of the SoundPlayer
	 * @param musicOn shall music be played
	 */
	public static void createInstance(Context context, boolean musicOn) {
		if (instance == null) {
			setContext(context);
			instance = new SoundPlayer(context, musicOn);
		}
	}
	/**
	 * Get the Singleton-Object
	 * 
	 * @param context needed for creation
	 * @return the SoundPlayer
	 */
	public static SoundPlayer getInstance() {
		return instance;
	}
	
	/**
	 * set the context 
	 * @param c the new context
	 */
	public static void setContext(Context c) {
		SoundPlayer.context = c;
	}
	
	/**
	 * internally creates the Soundplayer
	 * @param context the context of the SoundPlayer
	 */
	private SoundPlayer(Context context, boolean musicOn) {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		sounds = new HashMap<SoundEffect, Integer>();

		// add all supported sounds
		reloadSounds();

		// get system volume
		AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		volume = (float)mgr.getStreamVolume(AudioManager.STREAM_MUSIC)	/ (float)mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		this.musicOn = musicOn;
	}
	
	/**
	 * releases the memory and resources
	 */
	public void release() {
		if (soundPool != null) {
			soundPool.release();
			soundPool = null;
		}
		
		instance = null;
	}
	
	/**
	 * reload all sound samples
	 */
	public void reloadSounds() {
		sounds.put(SoundEffect.LOAD, soundPool.load(context, R.raw.l44_load, 1));
		sounds.put(SoundEffect.SHOT, soundPool.load(context, R.raw.l44_shot, 1));
		sounds.put(SoundEffect.FLAP, soundPool.load(context, R.raw.l44_flap, 1));
		sounds.put(SoundEffect.DROP, soundPool.load(context, R.raw.l44_drop, 1));
		sounds.put(SoundEffect.BEEP, soundPool.load(context, R.raw.l44_beep, 1));
		sounds.put(SoundEffect.END, soundPool.load(context, R.raw.l44_endding, 1));
	}

	/**
	 * Plays a SoundEffect
	 * 
	 * @param sound
	 *            the Sound to play
	 * @param position
	 *            ranges from 0.f to 1.f, where 0.5f means the sound is equally
	 *            distributed between left and right speaker
	 */
	public void play(SoundEffect sound, float position) {
		if (musicOn) {
			float leftVolume = volume, rightVolume = volume;

			if (position < 0.5f) { 
				leftVolume = volume * position * 2.0f; 
			} else { 
				rightVolume = volume * (1.0f - (position - 0.5f) * 2.0f); 
			}
		
			soundPool.play(sounds.get(sound), leftVolume, rightVolume, 1, 0, 1f);
		}
	}
	
	/**
	 * @return true, if music shall be played, otherwise false
	 */
	public boolean isMusicOn() {
		return musicOn;
	}
}

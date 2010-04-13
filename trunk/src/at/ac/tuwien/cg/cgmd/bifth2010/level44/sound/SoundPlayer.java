package at.ac.tuwien.cg.cgmd.bifth2010.level44.sound;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class SoundPlayer {
	/** The Sound Effects we support */
	public enum SoundEffect {
		LOAD, SHOT, FLAP, DROP
	};

	/** SoundPool for playing sounds */
	private SoundPool soundPool;
	/** Map of all sounds */
	private HashMap<SoundEffect, Integer> sounds;
	/** the volume set by the user */
	private float volume = 1.0f;
	/** does the user want to play sound effects? */
	private boolean musicOn = true;

	public SoundPlayer(Context context) {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		sounds = new HashMap<SoundEffect, Integer>();

		// add all supported sounds
		sounds.put(SoundEffect.LOAD, soundPool.load(context, R.raw.l44_load, 1));
		sounds.put(SoundEffect.SHOT, soundPool.load(context, R.raw.l44_shot, 1));
		sounds.put(SoundEffect.FLAP, soundPool.load(context, R.raw.l44_flap, 1));
		sounds.put(SoundEffect.DROP, soundPool.load(context, R.raw.l44_drop, 1));

		// get system volume
		AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		volume = (float)mgr.getStreamVolume(AudioManager.STREAM_MUSIC)	/ (float)mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		// check if sounds effects are set
		SharedPreferences settings = context.getSharedPreferences(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
		musicOn = settings.getBoolean(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.PREFERENCE_MUSIC, true);
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
			
			System.err.println("left: " + leftVolume + " right: " + rightVolume);
			
			soundPool.play(sounds.get(sound), leftVolume, rightVolume, 1, 0, 1f);
		}
	}
}

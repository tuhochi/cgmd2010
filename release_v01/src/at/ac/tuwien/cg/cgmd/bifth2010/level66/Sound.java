/**
 * Flight66 - a trip to hell
 * 
 * @author brm, dwi
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level66;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Sound {
	private static SoundPool sounds;
	private static int aviator;
	private static float sound_volume= 1.0f;
	private static int max_streams=1;
	private static boolean sound_active = false;
	
	public static void loadSound(Context context) {
	    sounds = new SoundPool(max_streams, AudioManager.STREAM_MUSIC, 0);
	    
	    // load all sounds needed | param 1 has no effect maybe in future android versions
	    aviator = sounds.load(context, R.raw.l66_proper, 1);
		
	    // check the volume of the system
		AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		sound_volume = (float)mgr.getStreamVolume(AudioManager.STREAM_MUSIC)/(float)mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		// check if sound is activated
		SharedPreferences settings = context.getSharedPreferences(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
		sound_active = settings.getBoolean(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.PREFERENCE_MUSIC, true);
	}
	
	public static void playSound(float playback_rate) {
		//playback_rate means the playback speed 2.0 twice as fast
	    if (!sound_active) return; // if sound is turned off no need to continue
	    sounds.play(aviator, sound_volume, sound_volume, 1, -1, playback_rate);
	}
	
	public static void resumeSound() {
	    if (!sound_active) return; // if sound is turned off no need to continue
	    sounds.resume(aviator);
	}
	
	public static void pauseSound() {
	    if (!sound_active) return; // if sound is turned off no need to continue
	    sounds.pause(aviator);
	}
	
	public static void destroySound() {
	    sounds.release();
	}
}
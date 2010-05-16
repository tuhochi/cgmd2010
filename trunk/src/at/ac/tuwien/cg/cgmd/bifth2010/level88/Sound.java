package at.ac.tuwien.cg.cgmd.bifth2010.level88;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
/*
 * TODO die Anzal der Sounds muss in amount auch angepasst werden
 */

/**
 * Soundmanager class
 * @author Asperger, Radax
 */
public class Sound {

	public enum Sounds {BACKGROUND, WIN, LOOSE, GOLD};
	
	private int amount = 3;
	private SoundPool soundPool;
	private HashMap<Sounds, Integer> soundMap;
	private AudioManager audioManager;
	private Context context;
	private boolean musicOn = true;
	private float volume = 1.0f;
	private static Sound instance = null;
	private int iD = 0;
	
	

	/**
	 * Get the Object
	 * @param con context of the android application
	 * @return the Soundobject
	 */
	public static Sound getInstance(Context con){
		if(instance == null){
			instance = new Sound(con);
		}
		return instance;
	}
	
	/**
	 * Initialize all the sounds
	 * @param con context of the android application
	 */
	private Sound(Context con){
		context = con;
		soundPool = new SoundPool(amount, AudioManager.STREAM_MUSIC, 100);
		soundMap = new HashMap<Sounds, Integer>();
		
		//add our supported sounds TODO
		soundMap.put(Sounds.BACKGROUND, soundPool.load(context, R.raw.l88_background, 1));
		soundMap.put(Sounds.GOLD, soundPool.load(context, R.raw.l00_gold01, 1));
		
		//check if music is on
		SharedPreferences settings = context.getSharedPreferences(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
		musicOn = settings.getBoolean(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.PREFERENCE_MUSIC, true);
		
		//get the system volume
		AudioManager amr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		volume = (float)amr.getStreamVolume(AudioManager.STREAM_MUSIC)/(float)amr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
	}
	
	
	/**
	 * Play the sounds
	 * @param sounds the sound to play
	 * @param loop if the sound should be looped (0 = no, -1 = loop forever, else = amount of times to play the sound)
	 */
	public void play(Sounds sounds){
		if(musicOn){
			if(sounds == Sounds.BACKGROUND){
				iD = soundPool.play(soundMap.get(sounds), 1.0f, 1.0f, 1, -1, 1.0f);
			} else {
				soundPool.play(soundMap.get(sounds), 1.0f, 1.0f, 1, 0, 1.0f);
			}
		}
	}
	
	public void destroy(){
		if(!musicOn){
			return;
		}else{
			soundPool.release();
		}
	}
	
	public void pause(){
		if(!musicOn){
			return;
		} else {
			soundPool.pause(iD);
		}
	}
	
	public void resume(){
		if(!musicOn){
			return;
		} else {
			soundPool.resume(iD);
		}
	}
	
}

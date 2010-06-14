package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

/**
 * This class provides access to the sound files needed for the game. 
 * @author Manuel Keglevic, Thomas Schulz
 *
 */
public class SoundManager {

	private static int maxStreams = 10;
	public static SoundManager singleton = null;
	private SoundPool soundPool;
	private Context context;
	private int backgroundStreamID = -1;
	private HashMap<Integer,Integer> sounds = new HashMap<Integer,Integer>();
	
	/**
	 * Creates a new SoundManager. I.e. creates a new SoundPool and loads the sound files.
	 * @param context - context of the activity, needed for accessing the resource files
	 */
	public SoundManager(Context context, boolean musicOn) {
		
		if(singleton != null)
			return;
		
		singleton = this;
		
		if(musicOn){
			soundPool = new SoundPool(maxStreams,AudioManager.STREAM_MUSIC,0);
			
			if(soundPool == null){
				Log.d("SoundManager","SoundPool creation failed.");	
				return;
			}else
				Log.d("SoundManager","SoundPool created.");	
			
			sounds.put(Constants.MUSIC_BACKGROUND, soundPool.load(context, Constants.MUSIC_BACKGROUND, 1));
			sounds.put(Constants.SOUND_COUNTDOWN, soundPool.load(context, Constants.SOUND_COUNTDOWN,1));
			sounds.put(Constants.SOUND_BYBY, soundPool.load(context, Constants.SOUND_BYBY, 1));
			sounds.put(Constants.SOUND_JUMP_DOWN, soundPool.load(context, Constants.SOUND_JUMP_DOWN, 1));
			sounds.put(Constants.SOUND_JUMP_UP, soundPool.load(context, Constants.SOUND_JUMP_UP, 1));
			sounds.put(Constants.SOUND_DOLLAR, soundPool.load(context,Constants.SOUND_DOLLAR,1));
			sounds.put(Constants.SOUND_ITEM,soundPool.load(context, Constants.SOUND_ITEM, 1));
			sounds.put(Constants.SOUND_BEAM_IN, soundPool.load(context, Constants.SOUND_BEAM_IN, 1));
			sounds.put(Constants.SOUND_BEAM_OUT, soundPool.load(context, Constants.SOUND_BEAM_OUT, 1));
		}
		this.context = context;
	}
	
	/**
	 * Starts the playback of a sound file.
	 * @param id - resource id of the file
	 * @param loop - if true, infinite loop
	 * @param volume - volume from 0..1
	 * @param rate - playback rate, 1 is standard playback
	 */
	public void play(int id,boolean loop, float volume, float rate){
		if(soundPool == null)
			return;
		
		AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		float vmax = (float)mgr.getStreamVolume(AudioManager.STREAM_MUSIC)	/ (float)mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		soundPool.play(sounds.get(id), volume*vmax, volume*vmax, 1, (loop)?-1:0, rate);
		
		Log.d("SoundManager", "Play");
		
	}
	
	/**
	 * Plays the background sound rescource as defined in the {@link Constants} class.
	 */
	public void playBackGround(){	
		if(soundPool == null)
			return;
		
		AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		float vmax = (float)mgr.getStreamVolume(AudioManager.STREAM_MUSIC)	/ (float)mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		backgroundStreamID = soundPool.play(sounds.get(Constants.MUSIC_BACKGROUND), 0.4f*vmax, 0.4f*vmax, 1, -1, 1.0f);
		
		
//		backgroundStreamID = play(Constants.MUSIC_BACKGROUND,true,0.4f,1.0f);
	}
	
	public void dispose(){
		if(soundPool != null)
			soundPool.stop(backgroundStreamID);
		
		singleton = null;
		
		if(soundPool != null)
			soundPool.release();
		
		soundPool = null;
	}
}

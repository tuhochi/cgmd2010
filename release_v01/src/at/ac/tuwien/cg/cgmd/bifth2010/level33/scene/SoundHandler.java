package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnErrorListener;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.sound.SoundPlayer.SoundEffect;

/**
 * 
 * All the sounds in the level will be handled by this class. A MediaPlayer will be used to play
 * the level-music. Because it's too expensive to create every time a new MediaPlayer if you catch an item,
 * a SoundPool will be used to play the sound-effects.
 * The music-preferences, which can be changed in the menu will be considered.
 *
 */
public class SoundHandler {
	
	private Context context = null;
	
	/**
	 * the name of the preference file that stores global user settings 
	 */
	public static final String SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE = "l00_settings";

	/**
	 * global user setting of type boolean that determines wether sound and music is allowed 
	 */
	public static final String PREFERENCE_MUSIC = "music";
	
	
	/** The level-music-file*/
	public static final int LEVEL_MUSIC1 = R.raw.l33_levelmusic;
	
	private MediaPlayer levelAudioPlayer = null;
	
	private SoundPool soundPool = null;
	public enum SoundEffect { STONE, BARREL, TRASH,SPRING,MAP,FINAL };
	private HashMap<SoundEffect, Integer> soundMap;
	
	private float volume = 1.0f;
	
	
	public SoundHandler(Context context)
	{
		this.context = context;
		
		//LEVEL MUSIC
		initLevelSound();
		
		//Sound EFFECT
		initSoundEffects();
	}
	
	/**
	 * 
	 * Creates an Audio-Player for the Music during a level. The MediaPlayer plays the music endless.
	 * 
	 */
	private void initLevelSound() {
		
		levelAudioPlayer = MediaPlayer.create(context, LEVEL_MUSIC1);
		levelAudioPlayer.setOnErrorListener(new OnErrorListener(){

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				//on any error we create a new media player
				levelAudioPlayer.release();
				levelAudioPlayer = MediaPlayer.create(context, LEVEL_MUSIC1);
				levelAudioPlayer.setOnErrorListener(this);
				return true;
			}
        });
		levelAudioPlayer.setLooping(true);	
	}
	
	/**
	 * 
	 * All sound-effects will be loaded and stored in a hash.map. So it's easy to access the audio-files.
	 * Also the volume-settings will be set.
	 * 
	 */
	private void initSoundEffects() {
		
		soundMap = new HashMap<SoundEffect, Integer>();
		soundPool = new SoundPool(SoundEffect.values().length, AudioManager.STREAM_MUSIC, 100);
		
		//load the sounds
		soundMap.put(SoundEffect.STONE, soundPool.load(context, R.raw.l00_gold02, 1));
		soundMap.put(SoundEffect.BARREL, soundPool.load(context, R.raw.l00_gold03, 1));
		soundMap.put(SoundEffect.SPRING, soundPool.load(context, R.raw.l33_spring, 1));
		soundMap.put(SoundEffect.MAP, soundPool.load(context, R.raw.l33_map, 1));
		soundMap.put(SoundEffect.TRASH, soundPool.load(context, R.raw.l33_trash, 1));
		soundMap.put(SoundEffect.FINAL, soundPool.load(context, R.raw.l33_final, 1));
		
		AudioManager audioM = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		volume = (float)audioM.getStreamVolume(AudioManager.STREAM_MUSIC)/(float)audioM.getStreamMaxVolume(AudioManager.STREAM_MUSIC);	
	}
	
	/**
	 * 
	 * The specify sound will be played.
	 * 
	 * @param soundEffect			The specify sound which should be played.
	 */
	public void playSoundEffect(SoundEffect soundEffect){
		if(LevelActivity.IS_MUSIC_ON)
			soundPool.play(soundMap.get(soundEffect), volume, volume, 1, 0, 1);
	}
	
	
	/**
	 * Releases the Audio-Player which plays the level-music
	 */
	public void releaseLevelAudioPlayer() {
		
		//Stop music if it's running
		if(levelAudioPlayer!=null){
			if(levelAudioPlayer.isPlaying()){
				levelAudioPlayer.stop();
			}
			levelAudioPlayer.release();
			levelAudioPlayer=null;
		}
		
		//Release the SoundPool
		soundPool.release();
		
	}
	
	/**
	 * Starts the Audio-Player which plays the level-music.
	 */
	public void startLevelAudioPlayer() {
		
		if(levelAudioPlayer != null && !levelAudioPlayer.isPlaying() )
		{
			levelAudioPlayer.start();
		}
	}
	
	/**
	 * Pauses the Audio-Player which plays the level-music.
	 */
	public void pauseLevelAudioPlayer() {
		
		if(levelAudioPlayer != null && levelAudioPlayer.isPlaying())
			levelAudioPlayer.pause();
		
	}
	
	/**
	 * Resumes the Audio-Player which plays the level-music.
	 */
	public void resumeLevelAudioPlayer() {
		
		if(levelAudioPlayer != null && !levelAudioPlayer.isPlaying())
			levelAudioPlayer.start();
		
	}
}

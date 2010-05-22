package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnErrorListener;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class SoundManager {

	private Context context;
	
	/** enumeration of existing sounds **/
	public enum SoundFX {BUTTON, HIT, MISS, BREAK, SWOOSH};
	private int soundsamount = 5;
	/** soundpool for playing sounds **/
	private SoundPool soundPool;
	/** hashmap with all available sounds **/
	private HashMap<SoundFX, Integer> soundMap;
	
	/** mediaplayer for playing background sounds **/
	private MediaPlayer musicPlayerBG;
	private MediaPlayer musicPlayerNature;
	public static final int MUSIC_BACKGROUND = R.raw.l00_menu;
	public static final int MUSIC_NATURE = R.raw.l84_nature;
	
	/** flag if sound is enabled/disabled **/
	private boolean soundOn = true;
	
	
	/**
	 * create a new SoundManager
	 * @param context
	 */
	public SoundManager(Context context)
	{
		this.context = context;
		
		//init soundPool and soundeffects
		soundPool = new SoundPool(soundsamount, AudioManager.STREAM_MUSIC, 100);
		initSoundMap(context);
		
		//init musicplayer for background sounds
		initMusicBG(context);
		initMusicNature(context);
		
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
	
		//TODO: sound for drop, hit (watersplash), ev. streetnoise, ...
		soundMap.put(SoundFX.BUTTON, soundPool.load(context,R.raw.l84_button,1));
		soundMap.put(SoundFX.HIT, soundPool.load(context,R.raw.l84_hit,1));
		soundMap.put(SoundFX.MISS, soundPool.load(context,R.raw.l84_miss,1));
		soundMap.put(SoundFX.SWOOSH, soundPool.load(context,R.raw.l84_swoosh,1));
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
	 * @param sound available sound of type SoundFX 
	 * @param leftVolume range = 0.0 to 1.0
	 * @param rightVolume range = 0.0 to 1.0
	 * @param loop 0 = no loop, -1 = loop forever
	 */
	public void playSound(SoundFX sound, float leftVolume, float rightVolume, int loop)
	{
		if (soundOn)
		{
			soundPool.play(soundMap.get(sound),leftVolume, rightVolume, 1, loop, 1.0f);
			/**
			 * resume background sound
			 */	}
	}
	
	/**
	 * release sounds and music
	 */
	public void releaseSounds()
	{
		releaseBackground();
		releaseNature();
		soundPool.release();
	}
	
	
	/**
	 * init background sound - ambient theme
	 * @param context
	 */
	private void initMusicBG(Context context)
	{
		musicPlayerBG = MediaPlayer.create(context, MUSIC_BACKGROUND);
		musicPlayerBG.setOnErrorListener(new OnErrorListener(){

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				//on any error we create a new media player
				musicPlayerBG.release();
				musicPlayerBG = MediaPlayer.create(SoundManager.this.context, MUSIC_BACKGROUND);
				musicPlayerBG.setOnErrorListener(this);
				return true;
			}
        });
		musicPlayerBG.setLooping(true);	
	}
	
	/**
	 * start playing background sounds (ambient and nature)
	 */
	public void playMusic()
	{
		startBackground();
		startNature();
	}
	
	/**
	 * release background sound
	 */
	public void releaseBackground() {
		
		if(musicPlayerBG!=null){
			if(musicPlayerBG.isPlaying())
			{
				musicPlayerBG.stop();
			}
			
			musicPlayerBG.release();
			musicPlayerBG=null;
		}
	}
	
	/**
	 * start background sound
	 */
	public void startBackground() {
		
		if(musicPlayerBG != null && !musicPlayerBG.isPlaying() )
		{
			musicPlayerBG.start();
		}
	}
	
	/**
	 * pause background sound
	 */
	public void pauseBackground()
	{
		if(musicPlayerBG != null && musicPlayerBG.isPlaying())
		{
			musicPlayerBG.pause();
		}
	}

	/**
	 * resume background sound
	 */
	public void resumeBackground()
	{
		if(musicPlayerBG != null && !musicPlayerBG.isPlaying())
		{
			musicPlayerBG.start();
		}
	}

	/**
	 * init music for background - nature theme
	 * @param context
	 */
	private void initMusicNature(Context context)
	{
		musicPlayerNature = MediaPlayer.create(context, MUSIC_NATURE);
		musicPlayerNature.setOnErrorListener(new OnErrorListener(){

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				//on any error we create a new media player
				musicPlayerNature.release();
				musicPlayerNature = MediaPlayer.create(SoundManager.this.context, MUSIC_NATURE);
				musicPlayerNature.setOnErrorListener(this);
				return true;
			}
        });
		musicPlayerNature.setLooping(true);	
	}

	/**
	 * release nature background sound
	 */

	public void releaseNature() {
		
		if(musicPlayerNature!=null){
			if(musicPlayerNature.isPlaying())
			{
				musicPlayerNature.stop();
			}
			
			musicPlayerNature.release();
			musicPlayerNature=null;
		}
	}
	
	/**
	 * start nature background sound
	 */
	public void startNature() {
		
		if(musicPlayerNature != null && !musicPlayerNature.isPlaying() )
		{
			musicPlayerNature.start();
		}
	}
	
	/**
	 * pause nature background sound
	 */
	public void pauseNature()
	{
		if(musicPlayerNature != null && musicPlayerNature.isPlaying())
		{
			musicPlayerNature.pause();
		}
	}
	
	/**
	 * resume nature background sound
	 */
	public void resumeNature()
	{
		if(musicPlayerNature != null && !musicPlayerNature.isPlaying())
		{
			musicPlayerNature.start();
		}
	}

	
	
}

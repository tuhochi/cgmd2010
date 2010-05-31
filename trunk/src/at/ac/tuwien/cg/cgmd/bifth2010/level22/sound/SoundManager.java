package at.ac.tuwien.cg.cgmd.bifth2010.level22.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnErrorListener;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class SoundManager {
	
	public static void init ( Context srcContext )
	{
		
		myContextRef = srcContext;
		
		musicProcessor = MediaPlayer.create( srcContext, R.raw.l22_music );
		musicProcessor.setOnErrorListener( new OnErrorListener() {
			
			public boolean onError(MediaPlayer mp, int what, int extra) {

				musicProcessor.release();
				musicProcessor = MediaPlayer.create( myContextRef, R.raw.l22_music );
				musicProcessor.setOnErrorListener( this );
				
				return true;
			}
		});
		
		musicProcessor.setLooping( true );

		soundFXPool = new SoundPool( 2, AudioManager.STREAM_MUSIC, 100 );
		loseSound = soundFXPool.load( myContextRef, R.raw.l22_lose, 1 );
		winSound = soundFXPool.load( myContextRef, R.raw.l22_win, 1 );
		
		AudioManager audioRef = (AudioManager) myContextRef.getSystemService(Context.AUDIO_SERVICE);
		audioManVolume = (float)audioRef.getStreamVolume(AudioManager.STREAM_MUSIC)/(float)audioRef.getStreamMaxVolume(AudioManager.STREAM_MUSIC); 
	}
	
	public static void playLoseSound()
	{
		
		soundFXPool.play( loseSound, audioManVolume, audioManVolume, 1, 0, 1 );
	}
	
	public static void playWinSound()
	{
		
		soundFXPool.play( winSound, audioManVolume, audioManVolume, 1, 0, 1 );
	}
	
	public static void pauseMusic()
	{
		
		musicProcessor.pause();
	}
	
	public static void continueMusic()
	{
		
		musicProcessor.start();
	}

	public static void uninit()
	{
		
		musicProcessor.stop();
		
		soundFXPool.release();
		musicProcessor.release();
	}
	
	private static MediaPlayer musicProcessor;
	
	private static SoundPool soundFXPool;
	
	private static Context myContextRef;
	
	private static int loseSound;
	private static int winSound;
	
	private static float audioManVolume;
}

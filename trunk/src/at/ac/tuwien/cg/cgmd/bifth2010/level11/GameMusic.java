package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * GameMusic plays one sound file, continuously or otherwise.
 * It is suitable for background music, and should not be used for
 * sound effects in game.
 * 
 * MediaPlayer is used.
 * @author g11
 */
public class GameMusic {
	/**
	 * player, that plays back the sound
	 */
	private static MediaPlayer _mediaPlayer;
	
	/**
	 * Begins playing a sound file in the background
	 * @param filename path to a sound file in the /assets/ folder of your APK
	 * @param continuous TRUE if the sound is to loop indefinitely
	 */
	public static void play(int id, boolean continuous) {
		if(_mediaPlayer != null)
			_mediaPlayer.release();
		_mediaPlayer = new MediaPlayer();
		if(continuous)
			setContinuous(true);
		
		AssetFileDescriptor afd = GameActivity.singleton.getResources().openRawResourceFd(id);
			
		try {
			_mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			_mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			_mediaPlayer.prepare();
			_mediaPlayer.start();
			_mediaPlayer.setVolume(0.5f, 0.5f);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	
	}
	/**
	 * 
	 * @return returns  true if the player loops the sound
	 */
	public static boolean isContinuous() {
		if (_mediaPlayer != null)
			return _mediaPlayer.isLooping();
		return false;
	}
	/**
	 * 
	 * @return retuns true if the player is currently playing back
	 */
	public static boolean isPlaying() {
		if (_mediaPlayer != null)
			return _mediaPlayer.isPlaying();
		return false;
	}
	/**
	 * set true, if sound that is played back in the player shall be looped
	 * @param continuous
	 */
	public static void setContinuous(boolean continuous) {
		_mediaPlayer.setLooping(continuous);
	}
	/**
	 * sets the volume of the player
	 * @param volume new volume of the player
	 */
	public static void setVolume(float volume) {
		if (_mediaPlayer != null)
			_mediaPlayer.setVolume(volume, volume);
	}
	/**
	 * pauses the player
	 */
	public static void pause() {
		if (_mediaPlayer != null)
			_mediaPlayer.pause();
	}
	/**
	 * continues the player
	 */
	public static void resume() {
		if (_mediaPlayer != null)
			_mediaPlayer.start();
	}
	
	/**
	 * restarts the player
	 */
	public static void restart() {
		if (_mediaPlayer != null)
			_mediaPlayer.seekTo(0);
	}
	/**
	 * stops the player
	 */
	public static void stop() {
		if (_mediaPlayer != null)
			_mediaPlayer.stop();
	}
	/**
	 * 
	 * @return returns the player object of type MediaPlayer
	 */
	public static MediaPlayer getMediaPlayer() {
		return _mediaPlayer;
	}
	
	/**
	 * Stops the music, and cleans up the MediaPlayer. Should be called when you are finished with music.
	 */
	public static void end() {
		if(_mediaPlayer.isPlaying()) {
			_mediaPlayer.stop();
		}
		
		if(_mediaPlayer != null) {
			_mediaPlayer.release();
			_mediaPlayer = null;
		}
	}

}

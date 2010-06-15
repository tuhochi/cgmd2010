package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import android.media.AudioManager;
import android.media.SoundPool;

/**
 * This is optimized for short sound files, which must be played quickly
 * such as sound effects in games. This is not suitable for music, and a 
 * relatively low file size limit exists.
 * 
 * The GameAudio class uses SoundPool to manage its sounds.
 * @author g11
 */
public class GameAudio {
	/**
	 * allocated sounds number
	 */
	public static final int MAX_SOUNDS = 50;
	/**
	 * maximal streams of the sound pool
	 */
	public static final int MAX_STREAMS = 200;
	/**
	 * singleton, points to this
	 */
	public static GameAudio singleton;
	/**
	 * master volume of all sounds
	 */
	private float _masterVolume;
	/**
	 * pool, that holds the sounds and load them
	 */
	private SoundPool _soundPool;
	/**
	 * array for the sound files
	 */
	private SoundFile[] soundArr = new SoundFile[MAX_SOUNDS];
	/**
	 * true if sound is muted
	 */
	public static boolean mute = false;
	
	public GameAudio() {
		GameAudio.singleton = this;
		_soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 100);
		_masterVolume = 1;
	}
	/**
	 * mutes the sound
	 */
	public void mute() {
		mute = true;
	}
	/**
	 * unmutes the sound
	 */
	public void unmute() {
		mute = false;
	}
	/**
	 * 
	 * @return returns true, if sound is muted
	 */
	public boolean isMuted() {
		return mute;
	}
	
	/**
	 * Frees up some memory, this should be called when you are finished.
	 */
	public void destroy() {
		try {
			for(int i = 0; i < MAX_STREAMS; i++)
				soundArr[i].unload();
			_soundPool.release();
		} catch (Exception e) { }
		_soundPool = null;
	}
	
	/**
	 * @return a HashSet of all current SoundFile's 
	 */
	public SoundFile[] getSounds() {
		return soundArr;
	}
	
	/**
	 * @return the SoundPool object currently being used
	 */
	public SoundPool getSoundPool() {
		return _soundPool;
	}
	
	/**
	 * Loads a file, ready to be played
	 * @param filename
	 * @return SoundFile
	 */
	public SoundFile createSoundFile(int file) {
		try {
			int id = _soundPool.load(GameActivity.singleton, file, 0);
			SoundFile soundFile = new SoundFile(id);
			int j = -1;
			for(int i = 0; i < MAX_SOUNDS; i++)
				if(soundArr[i] == null)
					j = i;
			if(j == -1) {
				// too many sounds
				return null;
			}
			soundArr[j] = soundFile;
			return soundFile;
		} catch (Exception e) {
			// not found
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param soundFile SoundFile to be removed from the memory
	 */
	public void removeSoundFile(SoundFile soundFile) {
		_soundPool.unload(soundFile.getId());
		for(int i = 0; i < MAX_SOUNDS; i++)
			if(soundArr[i] != null)
				if(soundArr[i].equals(soundFile))
					soundArr[i] = null;
	}
	
	/**
	 * Removes all SoundFile's from the memory
	 */
	public void removeAllSoundFiles() {
		for(int i = 0; i < MAX_SOUNDS; i++)
			soundArr[i] = null;
		_soundPool.release();
	}
	
	/**
	 * @param masterVolume the volume at which all future AudioStream's will play
	 */
	public void setMasterVolume(float masterVolume) {
		_masterVolume = masterVolume;
	}
	
	/**
	 * @return the current volume at which AudioStream's will play at
	 */
	public float getMasterVolume() {
		return _masterVolume;
	}
	

}

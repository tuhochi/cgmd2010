package at.ac.tuwien.cg.cgmd.bifth2010.level11;

/**
 * AudioFile contains sounds which have been loaded into the class.
 * @author g11
 */
public class SoundFile {
	/**
	 * id of the sound
	 */
	private int _id;
	/**
	 * stream, at which the sound file is played
	 */
	private AudioStream audioStream;
	
	public SoundFile(int streamId) {
		_id = streamId;
	}
	/**
	 * 
	 * @return returns the id of the sound file
	 */
	public int getId() {
		return _id;
	}
	
	/**
	 * @return the AudioStream through which the sound is playing
	 */
	public AudioStream play() {
		if(GameAudio.mute)
			return null;
		
		int result = 0;
		if (GameAudio.singleton.getSoundPool() != null) {
			result = GameAudio.singleton.getSoundPool().play(_id, GameAudio.singleton.getMasterVolume(), GameAudio.singleton.getMasterVolume(), 1, 0, 1f);
		}
		
		if(result != 0) {
			audioStream = new AudioStream(result, false, GameAudio.singleton.getMasterVolume());
			return audioStream;
		} else
			return null;
	}
	
	/**
	 * @return the AudioStream through which the sound is playing
	 */
	public AudioStream playContinuous() {
		if(GameAudio.mute)
			return null;
		int result = GameAudio.singleton.getSoundPool().play(_id, GameAudio.singleton.getMasterVolume(), GameAudio.singleton.getMasterVolume(), 0, -1, 1);
		if(result != 0) {
			audioStream = new AudioStream(result, true, GameAudio.singleton.getMasterVolume());
			return audioStream;
		} else
			return null;
	}
	
	/**
	 * Removes this file from the memory. Should be used when this file is no longer needed.
	 */
	public void unload() {
		GameAudio.singleton.getSoundPool().unload(_id);
	}
}
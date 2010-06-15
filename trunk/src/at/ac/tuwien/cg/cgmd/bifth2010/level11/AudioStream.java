package at.ac.tuwien.cg.cgmd.bifth2010.level11;

/**
 * Handles a stream of sound, launched through GameAudio.
 * Each time a SoundFile is played it is given a unique AudioStream
 */
public class AudioStream {
	/**
	 * volume of the stream
	 */
	private float _volume;
	/**
	 * id of the stream
	 */
	private int _id;
	/**
	 * true if stream is paused
	 */
	private boolean _paused;
	/**
	 * true if looped
	 */
	private boolean _continuous;
	/**
	 * speed at which the stream is played back
	 */
	private float _rate;
	
	/**
	 * Creates the AudioStream
	 * @param id the streamId
	 * @param continuous TRUE if looping indefinitely
	 * @param volume 0.0 to 1.0
	 */
	public AudioStream(int id, boolean continuous, float volume) {
		_id = id;
		_continuous = continuous;
		_volume = volume;
		_rate = 1;
	}
	
	/**
	 * @return TRUE if the AudioStream is paused
	 */
	public boolean isPaused() {
		return _paused;
	}
	
	/**
	 * @return The raw streamId from the SoundPool interface
	 */
	public int getId() {
		return _id;
	}
	
	/**
	 * Pauses the AudioStream, it must be started again with resume()
	 */
	public void pause() {
		_paused = true;
		GameAudio.singleton.getSoundPool().pause(_id);
	}
	
	/**
	 * Resumes a paused AudioStream
	 */
	public void resume() {
		_paused = false;
		GameAudio.singleton.getSoundPool().resume(_id);
	}
	
	/**
	 * Stops an AudioStream from being played, it cannot be restarted
	 */
	public void stop() {
		GameAudio.singleton.getSoundPool().stop(_id);
	}
	
	/**
	 * Defines whether the AudioStream is looping indefinitely
	 * @param continuous TRUE if indefinite looping
	 */
	public void setContinuous(boolean continuous) {
		if(continuous) 
			GameAudio.singleton.getSoundPool().setLoop(_id, -1);
		else
			GameAudio.singleton.getSoundPool().setLoop(_id, 0);
		_continuous = continuous;
	}
	
	/**
	 * @return TRUE if the AudioStream is looping indefinitely
	 */
	public boolean isContinuous() {
		return _continuous;
	}
	
	/**
	 * Sets the volume of this particular AudioStream
	 * @param volume 0.0 to 1.0
	 */
	public void setVolume(float volume) {
		_volume = volume;
		GameAudio.singleton.getSoundPool().setVolume(_id, _volume, _volume);
	}
	
	/**
	 * @return 0.0 to 1.0 the volume of this AudioStream
	 */
	public float getVolume() {
		return _volume;
	}
	
	/**
	 * Sets the rate at which the AudioStream is played, slow normal or fast.
	 * @param rate 0.5 to 2.0
	 */
	public void setRate(float rate) {
		_rate = rate;
		GameAudio.singleton.getSoundPool().setRate(_id, _rate);
	}
	
	/**
	 * Returns the rate at which the AudioStream is playing
	 * @return 0.5 to 2.0
	 */
	public float getRate() {
		return _rate;
	}

}

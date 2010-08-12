package at.ac.tuwien.cg.cgmd.bifth2010.level17.sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * 
 * @author MaMa
 *
 *Thanks to group 23!
 */
public class SoundManager {
	
	private Activity mContext;
	private List<MediaPlayer> mPlayers = new ArrayList<MediaPlayer>();

	/**
	 * Initializes the Soundmanager
	 * @param context The app context
	 */
	public void init(Activity context) {
		mContext = context;
		mPlayers.clear();
	}
	
	/**
	 * Get a player out of the List
	 * @param resId The resource id for the player
	 * @param isLooping Should this player play the sound in loop
	 * @return the id of the player
	 */
	public int getPlayer(int resId,boolean isLooping)
	{
		MediaPlayer newPlayer = MediaPlayer.create(mContext, resId);
		newPlayer.setLooping(isLooping);
		
		mPlayers.add(newPlayer);
		
		return mPlayers.size()-1;
	}
	
	/**
	 * Start playback
	 * @param id id of the player
	 */
	public void startPlayer(int id)
	{

		//get the calling intent
		Intent callingIntent = mContext.getIntent();
		//get the session state
		SessionState state = new SessionState(callingIntent.getExtras());
		boolean isMusicAndSoundOn = true;
		if(state!=null){
			int progress = state.getProgress();
			int level = state.getLevel();
			isMusicAndSoundOn = state.isMusicAndSoundOn(); 
		}
		if (isMusicAndSoundOn)
			mPlayers.get(id).start();
	}

	/**
	 * Pause playback
	 * @param id The id of the player
	 */
	public void pausePlayer(int id)
	{
		mPlayers.get(id).pause();
	}
	/**
	 * Returns if the current player is still playing
	 * @param id the id for the requested player
	 * @return true if playing, false otherwise
	 */
	public boolean isPlaying(int id) {
		return mPlayers.get(id).isPlaying();
	}
	/**
	 * Sets the volume for a specific player
	 * @param id playerid of the player to set the volume
	 * @param volume Volume to set for the specific player
	 */
	public void setVolumeForPlayer(int id, float volume)
	{
		mPlayers.get(id).setVolume(volume, volume);
	}
	
	/**
	 * Free media player resources
	 */
	public void releaseAudioResources()
	{
		for(int i=0;i<mPlayers.size();i++)
		{
			MediaPlayer tempPlayer = mPlayers.get(i);
			if(tempPlayer != null && tempPlayer.isPlaying())
				tempPlayer.stop();
			tempPlayer.release();
		}
		mPlayers.clear();
	}
	
	/**
	 * Pause all players
	 */
	public void pauseAllAudio()
	{
		for(int i=0;i<mPlayers.size();i++)
		{
			MediaPlayer tempPlayer = mPlayers.get(i);
			if(tempPlayer.isPlaying())
				tempPlayer.pause();
		}
	}
		
	/**
	 * Reset the audio manager
	 */
	public void reset()
	{
		try {
			for(int i=0;i<mPlayers.size();i++)
			{
				mPlayers.get(i).stop();
				mPlayers.get(i).prepare();
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static SoundManager mInstance = null;
	private SoundManager() {
		
	}

	/**
	 * Getter for the singleton instance
	 * @return Returns the singleton instance
	 */
	public static SoundManager getInstance() {
		if (mInstance == null) {
			mInstance = new SoundManager();
		}
		return mInstance;
	}
}

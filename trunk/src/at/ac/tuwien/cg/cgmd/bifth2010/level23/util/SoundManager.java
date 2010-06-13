package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * The Class manages audio output
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class SoundManager 
{
	
	/**
	 * Instance of this Manager
	 */
	public static SoundManager instance;
	
	/**
	 * The CONTEXT
	 */
	private Context context;
			
	/**
	 * players currently initialized
	 */
	private ArrayList<MediaPlayer> players;

	/**
	 * resId to MediaPlayer mapping
	 */
	private HashMap<Integer,MediaPlayer> playerMapping;
	
	/**
	 * Default Constructor
	 * @param context
	 */
	public SoundManager(Context context)
	{
		this.context = context;
		players = new ArrayList<MediaPlayer>();
		playerMapping = new HashMap<Integer, MediaPlayer>();
		instance = this;
	}
	
	/**
	 * Request a media player
	 * @param resId resource id for the player
	 * @param isLooping true for player looping
	 * @return playerId the id of the media player
	 */
	public int requestPlayer(int resId,boolean isLooping)
	{
		if(!playerMapping.containsKey(resId))
		{
			MediaPlayer newPlayer = MediaPlayer.create(context,resId);
			newPlayer.setLooping(isLooping);

			playerMapping.put(resId,newPlayer);
			players.add(newPlayer);
		}
		return resId;		
	}
	
	/**
	 * Start a player with a specific id
	 * @param id playerid of the player which will be started
	 */
	public void startPlayer(int id)
	{
		if(Settings.SOUND_ENABLED)
			playerMapping.get(id).start();
	}
	
	/**
	 * Pause a player with a specific id
	 * @param id playerid of the player which will be paused
	 */
	public void pausePlayer(int id)
	{
		playerMapping.get(id).pause();
	}
	
	/**
	 * Returns if the current player is still playing
	 * @param id the id for the requested player
	 * @return true if playing, false otherwise
	 */
	public boolean isPlaying(int id) {
		return playerMapping.get(id).isPlaying();
	}
	/**
	 * Sets the volume for a specific player
	 * @param id playerid of the player to set the volume
	 * @param volume Volume to set for the specific player
	 */
	public void setVolumeForPlayer(int id, float volume)
	{
		playerMapping.get(id).setVolume(volume, volume);
	}
	
	/**
	 * Free media player resources
	 */
	public void releaseAudioResources()
	{
		for(int i=0;i<players.size();i++)
		{
			MediaPlayer tempPlayer = players.get(i);
			if(tempPlayer.isPlaying())
				tempPlayer.stop();
			tempPlayer.release();
		}
		
		players.clear();
		players.clear();
	}
	
	public int getPlayerPosition(int playerId)
	{
		return playerMapping.get(playerId).getCurrentPosition();
	}
	
	public void setPlayerPosition(int playerId, int playerPos)
	{
		playerMapping.get(playerId).seekTo(playerPos);
	}
	
	/**
	 * Pause all players
	 */
	public void pauseAllAudio()
	{
		for(int i=0;i<players.size();i++)
		{
			MediaPlayer tempPlayer = players.get(i);
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
			for(int i=0;i<players.size();i++)
			{
				players.get(i).stop();
				players.get(i).prepare();
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

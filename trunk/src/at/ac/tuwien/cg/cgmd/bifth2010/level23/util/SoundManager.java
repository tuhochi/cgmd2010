package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.io.IOException;
import java.util.ArrayList;

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
	
	private Context context;
			
	/**
	 * players currently initialized
	 */
	private ArrayList<MediaPlayer> players;
	
	
	
	
	/**
	 * Default Constructor
	 * @param context
	 */
	public SoundManager(Context context)
	{
		this.context = context;
		players = new ArrayList<MediaPlayer>();
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
		MediaPlayer newPlayer = MediaPlayer.create(context,resId);
		newPlayer.setLooping(true);
		
		players.add(newPlayer);
		
		return players.size()-1;
	}
	
	/**
	 * Start a player with a specific id
	 * @param id playerid of the player which will be started
	 */
	public void startPlayer(int id)
	{
		players.get(id).start();
	}
	
	/**
	 * Pause a player with a specific id
	 * @param id playerid of the player which will be paused
	 */
	public void pausePlayer(int id)
	{
		players.get(id).pause();
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

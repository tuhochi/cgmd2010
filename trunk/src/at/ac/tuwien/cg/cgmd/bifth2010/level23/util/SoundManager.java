package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager 
{
	private Context context;
	
	private int numberOfPlayers=-1;
	private ArrayList<MediaPlayer> players;
	
	public static SoundManager instance;
	
	public SoundManager(Context context)
	{
		this.context = context;
		players = new ArrayList<MediaPlayer>();
		instance = this;
	}
	
	public int requestPlayer(int resId,boolean isLooping)
	{
		numberOfPlayers++;
		MediaPlayer newPlayer = MediaPlayer.create(context,resId);
		newPlayer.setLooping(true);
		
		players.add(newPlayer);
		
		return numberOfPlayers;
	}
	
	public void startPlayer(int id)
	{
		players.get(id).start();
	}
	
	public void pausePlayer(int id)
	{
		players.get(id).pause();
	}
	
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
	
	public void stopAllAudio()
	{
		for(int i=0;i<players.size();i++)
		{
			players.get(i).stop();
		}
	}
		
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

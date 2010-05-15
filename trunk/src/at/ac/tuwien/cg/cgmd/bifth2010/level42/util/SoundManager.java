package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;

/**
 * The Class manages audio output
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class SoundManager 
{
	
	/**
	 * Instance of this Manager
	 */
	public static final SoundManager instance = new SoundManager();
	
	/**
	 * The context
	 */
	private Context context;
			
	/**
	 * Currently ready MediaPlayers for each resID
	 */
	private HashMap<Integer, ArrayList<MediaPlayer>> players;
	
	private ArrayList<Pair<MediaPlayer,Integer>> availablePlayers;
	
	private ArrayList<MediaPlayer> pausedPlayers;
	
	/**
	 * Default Constructor
	 * @param context
	 */
	private SoundManager()
	{
		
	}
	
	private synchronized MediaPlayer preparePlayer(int resID)
	{
		Log.i(LevelActivity.TAG, "Preparing player for " + resID);
		MediaPlayer myPlayer = null;
		
		/*
		 * Find a player
		 */
		ArrayList<Pair<MediaPlayer,Integer>> availablePlayers = this.availablePlayers;
		HashMap<Integer, ArrayList<MediaPlayer>> players = this.players;
		
		int totalNumberOfPlayers = availablePlayers.size();
		for(int i=0; i<totalNumberOfPlayers; i++)
		{
			Pair<MediaPlayer,Integer> p = availablePlayers.get(i);
			
			MediaPlayer currentPlayer = p.getFirst();
			Integer oldGroupObject = p.getSecond();
			int oldGroup = oldGroupObject != null ? oldGroupObject : -1;
			
			if(!currentPlayer.isPlaying() && oldGroup != resID)
			{
				myPlayer = currentPlayer;
				
				// move it to the back of the list, for faster finding of new players..
				availablePlayers.remove(i);
				availablePlayers.add(p);
				
				// remove it from its old group
				if(oldGroup != -1)
				{
					Log.i(LevelActivity.TAG, "Removing player from group " + oldGroup + " in favor of " + resID);
					players.get(oldGroup).remove(currentPlayer);
				}
				
				// put it into the new group
				p.setSecond(resID);
				ArrayList<MediaPlayer> playersForThisResID = players.get(resID);
				if(playersForThisResID == null)
				{
					playersForThisResID = new ArrayList<MediaPlayer>();
					players.put(resID, playersForThisResID);
				}
				playersForThisResID.add(myPlayer);
				
				break;
			}
		}
		
		/*
		 * If there was a free player, re-initialize it
		 */
		if(myPlayer != null)
		{
			myPlayer.reset();
			
			AssetFileDescriptor fd = context.getResources().openRawResourceFd(resID);
			try
			{
				try
				{
					myPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
				}
				catch(IllegalStateException e)
				{
					myPlayer.reset();
					myPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
				}
				myPlayer.prepare();
				
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try { fd.close(); } catch (IOException e) {}
			}
		}
		
		return myPlayer;
	}
	
	public synchronized void playSound(int resID)
	{
		MediaPlayer myPlayer = null;
		
		// Try to find a player for this resource
		ArrayList<MediaPlayer> playersForThisRes = players.get(resID);
		if(playersForThisRes != null)
		{
			int numberOfPlayersForThisRes = playersForThisRes.size();
			for(int i=0; i<numberOfPlayersForThisRes; i++)
			{
				MediaPlayer currentPlayer = playersForThisRes.get(i);
				if(!currentPlayer.isPlaying())
				{
					myPlayer = currentPlayer;
					break;
				}
			}
		}
		
		if(myPlayer == null)
			myPlayer = preparePlayer(resID);
		
		// check if we found a player
		if(myPlayer != null)
		{
			myPlayer.seekTo(0);
			myPlayer.start();
			return;
		}
	}
	
	public synchronized void onCreate(Context context)
	{
		this.context = context;
		players = new HashMap<Integer, ArrayList<MediaPlayer>>();
		availablePlayers = new ArrayList<Pair<MediaPlayer,Integer>>();
		pausedPlayers = new ArrayList<MediaPlayer>();
		
		// create a fixed number of players
		for(int i=0; i<20; i++)
			availablePlayers.add(new Pair<MediaPlayer, Integer>(new MediaPlayer(),null));
		
		// prepare two players for each audio resource
		preparePlayer(R.raw.l42_test);
		preparePlayer(R.raw.l42_test);
	}
	
	public synchronized void onResume()
	{
		ArrayList<MediaPlayer> pausedPlayers = this.pausedPlayers;
	
		int size = pausedPlayers.size();
		for(int i=0; i<size; i++)
			pausedPlayers.get(i).start();
	}
	
	public synchronized void onPause()
	{
		ArrayList<Pair<MediaPlayer, Integer>> availablePlayers = this.availablePlayers;
		ArrayList<MediaPlayer> pausedPlayers = this.pausedPlayers;
		pausedPlayers.clear();
		
		int size = availablePlayers.size();
		for(int i=0;i<size;i++)
		{
			MediaPlayer currentPlayer = availablePlayers.get(i).getFirst();
			if(currentPlayer.isPlaying())
			{
				currentPlayer.pause();
				pausedPlayers.add(currentPlayer);
			}
		}
	}
	
	public synchronized void onDestroy()
	{
		ArrayList<Pair<MediaPlayer, Integer>> availablePlayers = this.availablePlayers;
		int size = availablePlayers.size();
		for(int i=0;i<size;i++)
		{
			MediaPlayer currentPlayer = availablePlayers.get(i).getFirst();
			if(currentPlayer.isPlaying())
				currentPlayer.stop();
			currentPlayer.release();
		}
	}
}

package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import android.content.Context;
import android.media.MediaPlayer;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class SoundManager 
{
	private Context context;
	
	private MediaPlayer musicPlayer;
	private MediaPlayer boostPlayer;
	
	public static SoundManager instance;
	
	public SoundManager(Context context)
	{
		this.context = context;
		initAudio();
		instance = this;
	}
	
	private void initAudio()
	{
		musicPlayer = MediaPlayer.create(context,R.raw.l00_map);
		musicPlayer.setLooping(true);
		
		boostPlayer = MediaPlayer.create(context,R.raw.l23_boost);
		boostPlayer.setLooping(true);
	}
	
	public void startMusic()
	{
		musicPlayer.start();
	}
	
	public void releaseAudioResources()
	{
		if(musicPlayer.isPlaying())
			musicPlayer.stop();
		musicPlayer.release();
		
		if(boostPlayer.isPlaying())
			boostPlayer.stop();
		boostPlayer.release();
	}
	
	public void startBoostSound()
	{
		boostPlayer.start();
	}
	
	public void stopBoostSound()
	{
		//stop + prepare is slow
		boostPlayer.pause();
	}
}

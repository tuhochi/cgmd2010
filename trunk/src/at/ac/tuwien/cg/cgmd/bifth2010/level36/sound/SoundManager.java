package at.ac.tuwien.cg.cgmd.bifth2010.level36.sound;

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
	private MediaPlayer mPlayer;
	
	public SoundManager(Activity context, int resid) {
		this.mContext = context;
		this.mPlayer = MediaPlayer.create(context,resid);
		this.mPlayer.setLooping(true);
	}
		
	public void startPlayer()
	{
		Intent callingIntent = mContext.getIntent();
		SessionState state = new SessionState(callingIntent.getExtras());
		boolean isMusicAndSoundOn = true;
		if(state!=null){
			//int progress = state.getProgress();
			//int level = state.getLevel();
			isMusicAndSoundOn = state.isMusicAndSoundOn(); 
		}
		if (isMusicAndSoundOn)
			this.mPlayer.start();
	}

	public void pausePlayer()
	{
		mPlayer.pause();
	}

	public boolean isPlaying() {
		return mPlayer.isPlaying();
	}

	public void setVolumeForPlayer(int id, float volume)
	{
		mPlayer.setVolume(volume, volume);
	}
	
//	public void stopPlayer()
//	{
//		this.mPlayer.stop();
//		this.mPlayer.release();
//	}
		
	public void reset()
	{
		this.mPlayer.stop();
		try {
			this.mPlayer.prepare();
		} catch (Exception e) {
			
		}
	}

}

package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import java.io.IOException;
import java.util.HashMap;

import android.R.bool;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.game.Map;

/**
 * This class handles all audio stuff
 * @author Gerd Katzenbeisser
 *
 */
public class Audio implements OnErrorListener, OnCompletionListener, OnPreparedListener
{
	private Context context;
	
	public static final int BUNNY_BLOCK_THEME = R.raw.l77_bunnyblock_theme;
	
	public static final int BLOCK_DROPPED_SOUND = R.raw.l77_block_dropped;
	public static final int BLOCK_SWAPPED_SOUND = R.raw.l77_block_swapped;
	public static final int BLOCK_EXPLODE_SOUND_1 = R.raw.l77_block_explode_1;
	public static final int BLOCK_EXPLODE_SOUND_2 = R.raw.l77_block_explode_1;
	public static final int BLOCK_EXPLODE_SOUND_3 = R.raw.l77_block_explode_1;
	public static final int BLOCK_EXPLODE_SOUND_4 = R.raw.l77_block_explode_1;
	public static final int	BLOCK_SOLVE_SOUND	= R.raw.l77_block_solve;
	
	private static final int IDLE = 0;
	private static final int PLAYING = 1;

	private static final String	TAG	= "Audio";
	
	private int test_var;
	
	// sounds
	public native void nativeRegisterAudio();

	
	private static java.util.Map<Integer, MediaPlayer> mps = new HashMap<Integer, MediaPlayer>();
	private static java.util.Map<MediaPlayer, Boolean> mpBlocked = new HashMap<MediaPlayer, Boolean>();

	
	public Audio (Context context)
	{
		this.context = context;
		test_var = 0xdeadbeef;

		putMp(BUNNY_BLOCK_THEME);
		putMp(BLOCK_DROPPED_SOUND);
		putMp(BLOCK_EXPLODE_SOUND_1);
		putMp(BLOCK_EXPLODE_SOUND_2);
		putMp(BLOCK_EXPLODE_SOUND_3);
		putMp(BLOCK_EXPLODE_SOUND_4);
		putMp(BLOCK_SWAPPED_SOUND);
	}
	
	private static void StaticAudioTest()
	{
		Log.d("l77native", "static callback!");
	}
	
	public void AudioTest()
	{
		Log.d("l77native", "non-static callback!");
		//putMp(BLOCK_DROPPED_SOUND);
		//Log.d( "l77native", String.format("solve sound value %d %x", BLOCK_SOLVE_SOUND, test_var) );
		playSound(Audio.BLOCK_EXPLODE_SOUND_1);
	}
	
	public void registerNativeCall()
	{
		nativeRegisterAudio();
	}
	
	
	/**
	 * Adds an Mediaplayer to the map mp identified by the resid
	 * @param resid
	 */
	private void putMp(int resid)
	{	
		MediaPlayer mp = MediaPlayer.create(context, resid);
		mp.setOnCompletionListener(this);
		mp.setOnPreparedListener(this);
		mps.put(resid, mp);
		mpBlocked.put(mp, false);
	}
	
	/**
	 * Playback of the given soundid. Available sounds are defined in this class as static.
	 * @param soundid
	 */
	public void playSound(int soundid)
	{
		playSound(soundid, false);
	}
	
	public void playSound(int soundid, boolean stopSound)
	{
		Log.d(TAG, "Start playback of soundid " + soundid);
		
		MediaPlayer mp = mps.get(soundid);
        

		if (mp != null)
			if (mp.isPlaying() && stopSound && !mpBlocked.get(mp))
			{
				mpBlocked.put(mp, true);
				mp.stop();
				mp.prepareAsync();
			} else if (!mp.isPlaying())
			{
				mp.start();
			}
		else
			Log.e(TAG, "Error MediaPlayer is NULL");
		Log.d(TAG, "Playback of soundid " + soundid + " started");
	}


	/**
	 * @param context the context to set
	 */
	public void setContext(Context context)
	{
		this.context = context;
	}


	/**
	 * @return the context
	 */
	public Context getContext()
	{
		return context;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp)
	{
	//	mp.prepareAsync();		
	}

	@Override
	public void onPrepared(MediaPlayer mp)
	{
		mpBlocked.put(mp, false);
	}
	
}

package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * This class handles all audio stuff for level 77 (BunnyBlock).
 * 
 * @author Gerd Katzenbeisser
 * 
 */
public class Audio implements OnErrorListener, OnCompletionListener, OnPreparedListener
{
	private Context				context;

	public static final int		BUNNY_BLOCK_THEME		= R.raw.l77_bunnyblock_theme;
	public static final int		BLOCK_DROPPED_SOUND		= R.raw.l77_block_dropped;
	public static final int		BLOCK_SWAPPED_SOUND		= R.raw.l77_block_swapped;
	public static final int		BLOCK_EXPLODE_SOUND_1	= R.raw.l77_block_explode_1;
	public static final int		BLOCK_EXPLODE_SOUND_2	= R.raw.l77_block_explode_1;
	public static final int		BLOCK_EXPLODE_SOUND_3	= R.raw.l77_block_explode_1;
	public static final int		BLOCK_EXPLODE_SOUND_4	= R.raw.l77_block_explode_1;
	public static final int		BLOCK_SOLVE_SOUND		= R.raw.l77_block_solve;

	private static final String	TAG						= "Audio";

	/**
	 * Registers this class for JNI callbacks
	 */
	public native void nativeRegisterAudio();

	private static java.util.Map<Integer, MediaPlayer>	mps			= new HashMap<Integer, MediaPlayer>();
	private static java.util.Map<MediaPlayer, Boolean>	mpBlocked	= new HashMap<MediaPlayer, Boolean>();

	private static SessionState							sessionState;

	/**
	 * Loads MediaPlayers for each sound.
	 * 
	 * @param context
	 */
	public Audio(Context context, SessionState sessionState)
	{
		this.context = context;

		Audio.sessionState = sessionState;

		putMp(BUNNY_BLOCK_THEME);
		putMp(BLOCK_DROPPED_SOUND);
		putMp(BLOCK_EXPLODE_SOUND_1);
		putMp(BLOCK_EXPLODE_SOUND_2);
		putMp(BLOCK_EXPLODE_SOUND_3);
		putMp(BLOCK_EXPLODE_SOUND_4);
		putMp(BLOCK_SWAPPED_SOUND);
	}

	/**
	 * JNI Callback for native code. Forwards call to playSound(int)
	 * 
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level77.Audio#playSound(int soundid)
	 * @param sound_id
	 */
	public void playCallback(int sound_jni_id)
	{
		Log.d("l77native", "non-static callback!");

		switch (sound_jni_id)
		{
			case 1:
				playSound(Audio.BLOCK_EXPLODE_SOUND_1);
				break;
			default:
				playSound(Audio.BLOCK_SWAPPED_SOUND);
		}
	}

	// TODO @Mike plz check if this can be removed
	public void voidTest()
	{
		Log.d("l77native", "--jne debug callback, voidTest()--");
	}

	// TODO @Mike plz check if this can be removed. This seems to be called
	// never - or is it called by native code? - If so plz write it into the
	// javadoc
	/**
	 * Registers audio callback with jni
	 */
	public void registerNativeCall()
	{
		nativeRegisterAudio();
	}

	/**
	 * Adds an Mediaplayer to the map mp identified by the resid
	 * 
	 * @param resid
	 */
	private void putMp(int resid)
	{
		MediaPlayer mp = MediaPlayer.create(context, resid);

		if (mp == null)
		{
			Log.e("l77native", "mplayer could not be created  (null)!");
			return;
		}

		mp.setOnCompletionListener(this);
		mp.setOnPreparedListener(this);
		mps.put(resid, mp);
		mpBlocked.put(mp, false);
	}

	/**
	 * Playback of the given soundid. Available sounds are defined in this class
	 * as static. An already playing sound is not interrupted.
	 * 
	 * @param soundid
	 *            The sound to play
	 */
	public void playSound(int soundid)
	{
		if (sessionState.isMusicAndSoundOn())
			playSound(soundid, false);
	}

	/**
	 * Playback of the given soundid. Available sounds are defined in this class
	 * as static. An already playing sound is interrupted if stopSound is set to
	 * true. If the player can be interrupted it is marked as blocked until the
	 * player has finished onPrepared state.
	 * 
	 * @param soundid
	 *            The sound to play
	 * @param stopSound
	 *            If set to true the playing sound is interrupted first.
	 * @see #onPrepared(MediaPlayer)
	 */
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
			} else
				Log.e(TAG, "Error MediaPlayer is NULL");
		Log.d(TAG, "Playback of soundid " + soundid + " started");
	}

	/**
	 * @param context
	 *            the context to set
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

	/**
	 * Not implemented
	 */
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra)
	{
		return false;
	}

	/**
	 * Not implemented
	 */
	@Override
	public void onCompletion(MediaPlayer mp)
	{

	}

	/**
	 * Marks the player as beeing not blocked anymore.
	 * 
	 * @param mp
	 *            The player to mark
	 */
	@Override
	public void onPrepared(MediaPlayer mp)
	{
		mpBlocked.put(mp, false);
	}

	/**
	 * Stops all playing sounds used by this class
	 */
	public void stopAllSounds()
	{
		Iterator<Entry<Integer, MediaPlayer>> it = mps.entrySet().iterator();
		while (it.hasNext())
			it.next().getValue().stop();

	}

}

package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import android.content.Context;
import android.media.MediaPlayer;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * This class handles all audio stuff
 * @author Gerd Katzenbeisser
 *
 */
public class Audio
{
	private Context context;
	
	public static final int BLOCK_DROPPED_SOUND = R.raw.l00_gold01;
	public static final int BLOCK_SWAPPED_SOUND = R.raw.l00_gold01;
	public static final int BLOCK_EXPLODE_SOUND = R.raw.l00_gold01;
	
	
	public Audio (Context context)
	{
		this.context = context;
	}
	
	/**
	 * Playback of the given soundid. Available sounds are defined in this class as static.
	 * @param soundid
	 */
	public void playSound(int soundid)
	{
		MediaPlayer mp = MediaPlayer.create(this.getContext(), soundid);
		mp.start();
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
	
}

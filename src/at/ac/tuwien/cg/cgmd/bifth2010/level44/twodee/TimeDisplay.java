package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.TimeManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.sound.SoundPlayer;

/**
 * The "time cake" for visualizing remaining time
 * 
 * A circle in the form of a cake that visualizes the
 * remaining time in the game, and shows a countdown
 * in the last several seconds.
 * 
 * @author Thomas Perl
 *
 */

public class TimeDisplay extends Sprite {
	/** constant: width of the display */
	private static final float WIDTH = 55f;
	/** constant: height of the display */
	private static final float HEIGHT = 55f;
	/** constant: x-pos of the texture */
	private static final float TEXTURE_X = 20f;
	/** constant: y-pos of the texture */
	private static final float TEXTURE_Y = 600f;

	/** Number of "clock fraction" parts in the texture */
	private static final int CLOCK_PARTS = 8;
	/** Number of "countdown timer" parts in the texture */
	private static final int COUNTDOWN_PARTS = 5;

	/** the context of the game */
	private Context context = null;
	/** the one big texture */
	private Texture texture;
	/** the timeManager that is used for managing the time */
	private TimeManager timeManager;
	/** used for retrieving the current TexturePart to display */
	private int offset;

	/**
	 * Creates the TimeDisplay-Sprite
	 * @param context the context used
	 * @param texture the one big texture
	 * @param timeManager the manager of the game-time
	 */
	public TimeDisplay(Context context, Texture texture, TimeManager timeManager) {
		super(new TexturePart(texture, TEXTURE_X, TEXTURE_Y, TEXTURE_X + WIDTH, TEXTURE_Y + HEIGHT));
		setCenter(0, 0);

		this.context = context;
		this.texture = texture;
		this.timeManager = timeManager;
		this.offset = 0;
	}

	/**
	 * displays the time left
	 */
	@Override
	public void draw(GL10 gl) {
		int remainingSeconds = (int) (timeManager.getRemainingTimeMillis() / 1000);
		int newOffset = offset;

		if (remainingSeconds <= COUNTDOWN_PARTS) {
			newOffset = CLOCK_PARTS + COUNTDOWN_PARTS - remainingSeconds;
		} else {
			newOffset = Math.max(0, CLOCK_PARTS - 1 - (int) (CLOCK_PARTS * remainingSeconds * 1000 / timeManager.getDuration()));
		}

		if (newOffset != offset) {
			offset = newOffset;
			onOffsetChanged(remainingSeconds);
		}

		timeManager.getDuration();

		super.draw(gl);
	}

	/**
	 * is called when the texture-part to display changes
	 * @param remainingSeconds the seconds remaining to play
	 */
	private void onOffsetChanged(int remainingSeconds) {
		float x = TEXTURE_X + WIDTH * offset;
		float y = TEXTURE_Y;

		TexturePart newTexturePart = new TexturePart(texture, x, y, x + WIDTH, y + HEIGHT);
		updateTexturePart(newTexturePart);
		
		// Beep for Countdown last 5 Seconds
		if (remainingSeconds == 0) {
			SoundPlayer.play(SoundPlayer.SoundEffect.END, 0.5f);
		} else if (remainingSeconds > 0 && remainingSeconds <= COUNTDOWN_PARTS) {
			SoundPlayer.play(SoundPlayer.SoundEffect.BEEP, 0.5f);
		}
	}

}

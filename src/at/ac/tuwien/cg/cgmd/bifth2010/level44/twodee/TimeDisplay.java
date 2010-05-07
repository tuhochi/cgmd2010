package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.os.Vibrator;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.TimeManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.sound.SoundPlayer;

public class TimeDisplay extends Sprite {
	private static final float WIDTH = 55f;
	private static final float HEIGHT = 55f;
	private static final float TEXTURE_X = 0f;
	private static final float TEXTURE_Y = 600f;
	
	/* Number of "clock fraction" parts in the texture */
	private static final int CLOCK_PARTS = 8;
	
	/* Number of "countdown timer" parts in the texture */
	private static final int COUNTDOWN_PARTS = 5;
	
	private Context context = null;
	private Texture texture;
	private TimeManager timeManager;
	private int offset;

	public TimeDisplay(Context context, Texture texture, TimeManager timeManager) {
		super(new TexturePart(texture, TEXTURE_X, TEXTURE_Y, TEXTURE_X+WIDTH, TEXTURE_Y+HEIGHT));
		setCenter(0, 0);
		
		this.context = context;
		this.texture = texture;
		this.timeManager = timeManager;
		this.offset = 0;
	}

	@Override
	public void draw(GL10 gl) {
		int remainingSeconds = (int)(timeManager.getRemainingTimeMillis()/1000);
		int newOffset = offset;
		
		if (remainingSeconds <= COUNTDOWN_PARTS) {
			newOffset = CLOCK_PARTS + COUNTDOWN_PARTS - remainingSeconds;
		} else {
			newOffset = Math.max(0, CLOCK_PARTS - 1 - (int)(CLOCK_PARTS*remainingSeconds*1000/timeManager.getDuration()));
		}
		
		if (newOffset != offset) {
			offset = newOffset;
			onOffsetChanged(remainingSeconds);
		}
		
		timeManager.getDuration();
		
		super.draw(gl);
	}

	private void onOffsetChanged(int remainingSeconds) {
		float x = TEXTURE_X + WIDTH*offset;
		float y = TEXTURE_Y;
		
		TexturePart newTexturePart = new TexturePart(texture, x, y, x+WIDTH, y+HEIGHT);
		updateTexturePart(newTexturePart);		
		
		// Beep for Countdown last 5 Seconds
		if (remainingSeconds > 0 && remainingSeconds <= COUNTDOWN_PARTS) {
			SoundPlayer.getInstance(context).play(SoundPlayer.SoundEffect.BEEP, 0.5f);
		}
	}

}

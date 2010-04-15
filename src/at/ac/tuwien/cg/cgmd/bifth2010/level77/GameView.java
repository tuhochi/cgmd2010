package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import java.io.BufferedInputStream;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * GameView is the view of the game implementation of group 77. 
 * @author Gerd Katzenbeisser
 *
 */
public class GameView extends GLSurfaceView
{

	L77Renderer renderer;
	Native jni;
	Audio audio;
	
	public GameView(Context context, Callback<Integer> gameEnded)
	{
		super(context);
		audio = new Audio(context);
		
		// Test sounds
		audio.playSound(Audio.BLOCK_DROPPED_SOUND);
		audio.playSound(Audio.BLOCK_EXPLODE_SOUND);
		audio.playSound(Audio.BLOCK_SWAPPED_SOUND);
		

		jni = new Native(context, audio, gameEnded);
		
		setRenderer(new L77Renderer(true, context));
	}
	


	/**
	 * Touch Events are catched and sent to a native call.
	 */
	@Override
	public boolean onTouchEvent(final MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			jni.touchesBegan(event.getX(), event.getY());
		if (event.getAction() == MotionEvent.ACTION_MOVE)
			jni.touchesMoved(event.getX(), event.getY());
		if (event.getAction() == MotionEvent.ACTION_UP)
			jni.touchesEnded(event.getX(), event.getY());
		
		return true;
	}		
}

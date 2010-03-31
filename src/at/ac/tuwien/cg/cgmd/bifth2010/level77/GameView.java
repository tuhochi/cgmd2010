package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * GameView is the view of the game implementation of group 77. 
 * @author gerd
 *
 */
public class GameView extends GLSurfaceView
{

	L77Renderer renderer;
	
	public GameView(Context context)
	{
		super(context);

		setRenderer(new L77Renderer(true, context));
	}

	/**
	 * Touch Events are catched and sent to a native call.
	 */
	@Override
	public boolean onTouchEvent(final MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			nativeDown(event.getRawX(), event.getRawY());
		
		return true;
	}
	
	private void nativeDown(float x, float y)
	{
		// TODO ndk Call here
	}
	
}

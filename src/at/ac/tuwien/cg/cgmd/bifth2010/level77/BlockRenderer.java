package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.Rectangle;

//one of the bloody things needed for getResources()
import android.content.Context;

/**
 * OpenGl Renderer for level 77 (BunnyBlock)
 * <p>
 * The renderer mixes rendering in native code for displaying the blocks and
 * java code for displaying the time bar and the amount of lost gold.
 * <p>
 * The renderer is also responsible for exiting the game after two minutes
 * playing time.
 * 
 * @author Gerd Katzenbeisser
 * @author Michael Vasiljevs
 */
public class BlockRenderer implements Renderer
{
	private Context			mContext;
	private Native			jni;
	private Rectangle		timeBar;
	private CoinBar			coinBar;
	private long			startTime;
	private long			dateOffset	= 0;
	private Rectangle		clock;
	private static String	TAG			= "L77Renderer";
	private Callback<Void>	timeUp;
	private float			lastDragX	= 0, lastDragY = 0;

	/**
	 * Constructor for Level 77 Renderer
	 * 
	 * @param context
	 *            The context this renderer lives in
	 * @param timeUp
	 *            Callback when the playing time is up
	 */
	public BlockRenderer(Context context, Native nat_jni, Callback<Void> timeUp)
	{
		mContext = context;
		jni = nat_jni;
		this.timeUp = timeUp;
	}

	private float	percent	= 1.0f;

	/**
	 * {@inheritDoc} (Implementation)
	 * <p>
	 * Calls the native rendering and also renders time- and progressbar.
	 */
	@Override
	public void onDrawFrame(GL10 gl)
	{
		// Render in native code

		gl.glEnable(GL10.GL_TEXTURE_2D);
		jni.render();

		gl.glClear(GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glTranslatef(6.5f, 0.0f, 5.0f);
		coinBar.draw(percent);
		gl.glColor4f(1, 1, 1, 1);
		gl.glTranslatef(0.5f, 0.25f, 0);
		clock.draw(gl);

		long currentTime = System.currentTimeMillis();
		float timePercent = 1.0f - ((float) (currentTime - startTime)) / 1000f / 120f;
		Log.d(TAG, "Time percent: " + timePercent);
		if (timePercent <= 0)
			timeUp.onSucces(null);

		gl.glScalef(1.0f, timePercent, 1.0f);
		gl.glTranslatef(0, 4.75f, 0.0f);
		gl.glColor4f(1, 1, 1, 1);
		timeBar.draw(gl);

		gl.glFlush();
	}

	/**
	 * {@inheritDoc} (Implementation)
	 * <p>
	 * Forwards the call to native method to handle the resize.
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		jni.resizeView(width, height);
		Log.i("l77renderer", String.format("surface resized to %dx%d", width, height));
	}

	/**
	 * {@inheritDoc} (Implementation)
	 * <p>
	 * Initializes native code, creates instances of objects rendered in java
	 * and sets the start time for the game.
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		// init first part of jni stuff
		jni.init();
		jni.initClasses();

		timeBar = new Rectangle(0.25f, 9.0f);
		timeBar.setTexture(gl, mContext.getResources(), R.drawable.l77_time_bar);
		clock = new Rectangle(0.25f, 0.25f);
		clock.setTexture(gl, mContext.getResources(), R.drawable.l77_clock);
		coinBar = new CoinBar(0.5f, 9.0f, 0.1f, -42.0f, mContext, gl);

		// TODO Reduce to 0.2f for the abgabe
		coinBar.setCoinHeight(0.5f);

		setStartTime(System.currentTimeMillis() - dateOffset);
		Log.i("renderer", "initilized");
	}

	// TODO @Mike is this the right place for deInit() ??
	/**
	 * Calls deInit() in native code.
	 */
	@Override
	public void finalize()
	{
		jni.deInit();
	}

	/**
	 * Helper method for development only
	 * 
	 * @param event
	 */
	public void onTouchEvent(MotionEvent event)
	{
		float x = event.getX(), y = event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			lastDragX = x;
			lastDragY = y;
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			float distanceX = x - lastDragX;
			float distanceY = y - lastDragY;
			lastDragX = x;
			lastDragY = y;

			percent = percent - distanceX / 100.0f;
			Log.v(TAG, "percent: " + percent);
			Log.v(TAG, "distanceY: " + distanceY);
		}
	}

	/**
	 * Sets the startTime for the game
	 * 
	 * @param startTime
	 */
	private void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * Sets the dateOffset for the start time. For example when the game was
	 * interrupted and the player comes back later.
	 * 
	 * @param dateOffset
	 */
	public void setDateOffset(long dateOffset)
	{
		this.dateOffset = dateOffset;
	}

}

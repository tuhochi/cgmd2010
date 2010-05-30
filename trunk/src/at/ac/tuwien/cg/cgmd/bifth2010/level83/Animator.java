package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;

/**
 * Animator interface used to add animations to existing {@link MySprite}.
 * @author Manuel Keglevic, Thomas Schulz
 *
 */
public interface Animator {
	public static final int NO_ANIMATION = -1;
	
	/**
	 * Draw sprite with animation.
	 * @param sprite - MySprite used for drawing
	 * @param gl
	 */
	public void drawAnimated(MySprite sprite, GL10 gl);
	
	/**
	 * Starts the animation defined by the animationID
	 * @param animationID id of the animation to start
	 * @param value animation specific parameter value
	 */
	public void startAnimation(int animationID,float value);
	
	/**
	 * Determines if an animation is running and returns the animationID
	 * @return animationID of the animation running or {@link Animator#NO_ANIMATION}
	 */
	public int animationRunning();
}

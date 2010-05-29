package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;

public interface Animator {
	public static final int NO_ANIMATION = -1;
	public void drawAnimated(MySprite sprite, GL10 gl);
	public void startAnimation(int animationID,float value);
	public int animationRunning();
}

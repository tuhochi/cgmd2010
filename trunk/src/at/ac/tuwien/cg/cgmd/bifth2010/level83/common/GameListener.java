package at.ac.tuwien.cg.cgmd.bifth2010.level83.common;

import javax.microedition.khronos.opengles.GL10;


public interface GameListener {
	public void setup(GameActivity gameActivity,GL10 gl);
	public void mainLoopIteration( GameActivity activity, GL10 gl );
}

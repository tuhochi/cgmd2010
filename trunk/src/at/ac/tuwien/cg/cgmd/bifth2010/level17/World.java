package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;

public interface World {
	public void update();
	public void draw(GL10 gl);
	public void onSurfaceChanged(GL10 gl, int width, int height);
	public void init(GL10 gl);
	public void fingerDown(Vector2 pos);
	public void fingerMove(Vector2 pos);
	public void fingerUp(Vector2 pos);
}

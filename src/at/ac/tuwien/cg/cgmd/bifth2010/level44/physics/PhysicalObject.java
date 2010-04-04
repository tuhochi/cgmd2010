package at.ac.tuwien.cg.cgmd.bifth2010.level44.physics;

import javax.microedition.khronos.opengles.GL10;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;

public interface PhysicalObject {
	public static final float GRAVITY = 9.81f;
	
	public void move(long time);
	public void processGesture(InputGesture gesture);
	
	public void draw(GL10 gl);
	public void setPosition(float x, float y);
}

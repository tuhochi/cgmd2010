package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;

public interface Drawable {
	
	public void Init(GL10 gl);
	public void Draw(GL10 gl);
	public void Dispose(GL10 gl);
}

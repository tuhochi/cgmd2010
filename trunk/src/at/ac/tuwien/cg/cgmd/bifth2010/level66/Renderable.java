package at.ac.tuwien.cg.cgmd.bifth2010.level66;

import javax.microedition.khronos.opengles.GL10;

/**
 * An interface for drawable objects
 * @author MaMa
 *
 */
public interface Renderable 
{
	/**
	 * Draw the renderable
	 * @param gl The OpenGL context
	 */
	public void draw(GL10 gl);
}

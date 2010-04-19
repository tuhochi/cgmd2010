package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;

/**
 * An interface for objects which can be drawn. Implementations have to 
 * provide {@link #Init(GL10)}, {@link #Draw(GL10)} and {@link #Dispose(GL10)} 
 * methods.
 */
public interface Drawable {
	
	/**
	 * This method is used to initialize the {@link Drawable}. It can be used 
	 * to load textures for example.
	 * 
	 * @param gl
	 */
	public void Init(GL10 gl);
	
	/**
	 * This method is used to draw the {@link Drawable}.
	 * 
	 * @param gl
	 */
	public void Draw(GL10 gl);
	
	/**
	 * This method can be used to free resources or to unbind textures from a 
	 * {@link Drawable}.
	 * 
	 * @param gl
	 */
	public void Dispose(GL10 gl);
}

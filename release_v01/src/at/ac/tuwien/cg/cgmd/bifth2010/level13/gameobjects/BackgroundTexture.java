package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * 
 * @author group13
 * 
 * texture of background object
 *
 */
public class BackgroundTexture extends Texture {
	
	/**
	 * constructor loads bg-texture
	 * @param gl gl
	 * @param context context
	 */
	public BackgroundTexture(GL10 gl, Context context) {
		super();
		this.textureId[0] = R.drawable.l13_bg;
		this.loadGLTexture(gl, context);
	}
}

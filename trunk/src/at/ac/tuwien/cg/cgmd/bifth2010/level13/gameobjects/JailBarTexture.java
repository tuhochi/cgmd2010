package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * 
 * @author group13
 * 
 * texture for jail-bar
 *
 */
public class JailBarTexture extends Texture {
	
	/**
	 * constructor loads jail-bar texture
	 * @param gl gl
	 * @param context context
	 */
	public JailBarTexture(GL10 gl, Context context) {
		super();
		this.textureId[0] = R.drawable.l13_busted;
		this.loadGLTexture(gl, context);
	}
}

package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


/**
 * 
 * @author group13
 * 
 * censored texture for player object
 *
 */
public class CensoredTexture extends Texture {

	/**
	 * constructor loads censored texture
	 * 
	 * @param gl gl
	 * @param context context
	 */
	public CensoredTexture(GL10 gl, Context context) {
		super();
		this.textureId[0] = R.drawable.l13_censored;
		this.loadGLTexture(gl, context);
	}
}

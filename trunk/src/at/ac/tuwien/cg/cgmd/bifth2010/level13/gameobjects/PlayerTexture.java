package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * 
 * @author group13
 * 
 * texture of player object
 *
 */
public class PlayerTexture extends Texture {

	/**
	 * constructor loads rabbit texture
	 * @param gl gl
	 * @param context context
	 */
	public PlayerTexture(GL10 gl, Context context) {
		super();
		this.textureId[0] = R.drawable.l13_bunny;
		this.loadGLTexture(gl, context);
	}
}

package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * 
 * @author arthur (group 13)
 *
 */
public class PlayerTexture extends Texture {

	/**
	 * constructor loads rabbit texture
	 * @param gl
	 * @param context
	 */
	public PlayerTexture(GL10 gl, Context context) {
		super();
		this.textureId = new int[1];
		this.textureId[0] = R.drawable.l00_rabit_256;
		this.textures = new int[1];
		this.loadGLTextures(gl, context);
	}
}

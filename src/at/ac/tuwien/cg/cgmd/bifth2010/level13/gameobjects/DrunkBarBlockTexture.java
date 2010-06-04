package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * @author group13
 * 
 * texture for drunkbarblock
 *
 */
public class DrunkBarBlockTexture extends Texture {
	
	/**
	 * constructor loads beer texture
	 * @param gl gl
	 * @param context context
	 */
	public DrunkBarBlockTexture(GL10 gl, Context context) {
		super();
		this.textureId[0] = R.drawable.l13_beer;
		this.loadGLTexture(gl, context);
	}
}

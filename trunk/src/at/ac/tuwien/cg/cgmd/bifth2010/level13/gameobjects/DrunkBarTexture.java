package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * 
 * @author group13
 * 
 * texture for drunk-bar
 *
 */
public class DrunkBarTexture extends Texture {
	
	/**
	 * constructor loads drunk-bar texture
	 * @param gl gl 
	 * @param context context
	 */
	public DrunkBarTexture(GL10 gl, Context context) {
		super();
		this.textureId[0] = R.drawable.l13_wasted;
		this.loadGLTexture(gl, context);
	}
}

package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.Texture;

/**
 * 
 * @author arthur (group 13)
 *
 */
public class BeerTexture extends Texture {
	
	/**
	 * constructor loads beer texture
	 * @param gl
	 * @param context
	 */
	public BeerTexture(GL10 gl, Context context) {
		super();
		this.textureId = new int[1];
		this.textureId[0] = R.drawable.l13_beer;
		this.textures = new int[1];
		this.loadGLTextures(gl, context);
	}
}

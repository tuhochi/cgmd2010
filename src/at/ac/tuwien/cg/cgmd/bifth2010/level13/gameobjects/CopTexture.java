package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * 
 * @author group13
 * 
 * texture for cop object
 *
 */
public class CopTexture extends Texture{
	
	/**
	 * constructor loads cop texture
	 * @param gl gl 
	 * @param context context
	 */
	public CopTexture(GL10 gl, Context context){
		super();
		this.textureId[0] = R.drawable.l13_cop;
		this.loadGLTexture(gl, context);
	}
}

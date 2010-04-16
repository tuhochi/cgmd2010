package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class MistressTexture extends Texture{
	public MistressTexture(GL10 gl, Context context){
		
		super();
		this.textureId = new int[1];
		this.textureId[0] = R.drawable.l13_mistress;
		this.textures = new int[1];
		this.loadGLTextures(gl, context);
	}
	
	
}
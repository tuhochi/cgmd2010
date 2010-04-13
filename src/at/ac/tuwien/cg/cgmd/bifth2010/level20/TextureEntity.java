package at.ac.tuwien.cg.cgmd.bifth2010.level20;


import static android.opengl.GLES10.*;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.opengl.GLES11;
import android.opengl.GLUtils;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Quad;



public class TextureEntity extends GameEntity implements Renderable {

	protected int textureId;
	protected boolean visible;	
	protected Quad quad;
	
	public TextureEntity() {
		textureId = -1;
		visible = true;
		quad = new Quad(50, 50);
	}
	
	
	public TextureEntity(float width, float height) {
		textureId = -1;
		visible = true;
		quad = new Quad(width, height);
		setScale(width, height);
	}

	@Override
	public void render(GL10 gl) {
		if(!visible || textureId < 0) return;
		
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			 		
		quad.draw(gl);		
	
	}	

	
	public int getTextureId() {
		return textureId;
	}

	public void setTextureId(int textureId) {
		this.textureId = textureId;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}

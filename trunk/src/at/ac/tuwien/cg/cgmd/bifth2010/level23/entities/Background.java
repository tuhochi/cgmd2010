package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;
import static android.opengl.GLES10.glBindTexture;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES10.glTexCoordPointer;
import static android.opengl.GLES10.glTranslatef;
import static android.opengl.GLES10.glVertexPointer;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES10;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;

public class Background implements SceneEntity 
{

	private static final long serialVersionUID = -8706705496517584380L;
	private FloatBuffer vertexBuffer;
	private int textureID;
	private FloatBuffer[] texCoordBuffer;
	private final float[] texCoords = {0.f, 0.5f, 1.f, 0.5f, 0.f, 0.f, 1.f, 0.f, 0.f, 1.f, 1.f, 1.f, 0.f, 0.5f, 1.f, 0.5f}; 
	private float scrollSpeed = 0.01f;
	private float positionY;
	private RenderView renderView; 
	private GeometryManager geometryManager; 
	//hack only needed now for only 2 textures
	private boolean switchTexture = false;
	
	public Background()
	{
		renderView = RenderView.getInstance();	
		//texCoordBuffer = new FloatBuffer[2];
		preprocess();
		
	}
	
	private void preprocess() {
		
		geometryManager = GeometryManager.getInstance(); 
		texCoordBuffer = geometryManager.createTexCoordBufferQuad(texCoords);
		vertexBuffer = geometryManager.createVertexBufferQuad(renderView.getRightBounds(), renderView.getTopBounds());
		//texCoordBuffer = geometryManager.createTexCoordBufferQuad(texCoords);
		//vboId = geometryManager.createVBO(vertexBuffer1, texCoordBuffer);
		
	}
	
	

	
	public void setTextureID(int texID)
	{
		textureID = texID;
	}
	
	public void update(float dt)
	{
		positionY -= dt*scrollSpeed;
		
		if(positionY <= renderView.getTopBounds()*-1)
		{
			positionY += renderView.getTopBounds(); 
			switchTexture = !switchTexture;
		}
	}

	@Override
	public void render() 
	{
		glPushMatrix();
		
		glTranslatef(0, positionY, 0);
		//render first BG quad
		glBindTexture(GL10.GL_TEXTURE_2D, textureID);
		glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer[switchTexture?1:0]);
		glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		GLES10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4); 
		
		
		//render second BG quad
		glTranslatef(0,renderView.getTopBounds(),0);
		glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer[switchTexture?0:1]);
		GLES10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		glPopMatrix();
	}

}

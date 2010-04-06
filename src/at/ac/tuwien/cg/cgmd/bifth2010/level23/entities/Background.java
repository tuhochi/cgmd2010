package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;
import static android.opengl.GLES10.*;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES10;
import android.opengl.GLES11;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;

public class Background implements SceneEntity 
{

	private static final long serialVersionUID = -8706705496517584380L;
	private FloatBuffer vertexBuffer;
	private int textureID;
	private FloatBuffer texCoordBuffer;
	private final float[] texCoords = {0.f, 0.5f, 1.f, 0.5f, 0.f, 0.f, 1.f, 0.f}; 
	private float scrollSpeed = 0.01f;
	private float positionY;
	private RenderView renderView; 
	private GeometryManager geometryManager; 
	private boolean vbo=true;
	private int vboId;
	
	public Background()
	{
		renderView = RenderView.getInstance();	
		//texCoordBuffer = new FloatBuffer[2];
		preprocess();
		
	}
	
	private void preprocess() {
		
		geometryManager = GeometryManager.getInstance(); 
		texCoordBuffer = geometryManager.createTexCoordBufferQuad(texCoords)[0];
		vertexBuffer = geometryManager.createVertexBufferQuad(renderView.getRightBounds(), renderView.getTopBounds());
		//texCoordBuffer = geometryManager.createTexCoordBufferQuad(texCoords);
		vboId = geometryManager.createVBO(vertexBuffer, texCoordBuffer);
		
	}
	
	public void setTextureID(int texID)
	{
		textureID = texID;
	}
	
	public void update(float dt)
	{
		positionY -= dt*scrollSpeed/RenderView.getInstance().getTopBounds();
		
//		if(positionY <= renderView.getTopBounds()*-1)
//		{
//			positionY += renderView.getTopBounds(); 
//		}
	}

	@Override
	public void render() 
	{
		glMatrixMode(GL_TEXTURE);
		
		glPushMatrix();
		
			//translate texture
			glTranslatef(0, positionY, 0);
			glBindTexture(GL10.GL_TEXTURE_2D, textureID);
			if (!vbo) 
			{
				glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
				glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
				
				
				GLES10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4); 
			
			} 
			else 
			{
		
				GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, vboId);

				GLES11.glVertexPointer(3, GL_FLOAT, 0, 0);
				GLES11.glTexCoordPointer(2, GL_FLOAT, 0, 12*4); // 4 vertices with 3 coordinates, 4 bytes per float

				glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
				
				GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, 0);
			}
		
		glPopMatrix();
		
		glMatrixMode(GL_MODELVIEW);
	}

}

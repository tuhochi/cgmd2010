package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;

import static android.opengl.GLES10.*;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES11;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;

public class Button
{
	private Vector2 position;
	private float width;
	private float height;
	private boolean active=true;
	private FloatBuffer vertexBuffer;
	private FloatBuffer texCoordBuffer;
	private int vboId;
	private GeometryManager geometryManager;
//	private int textureID=0; 
	
	public Button(float width, float height, Vector2 position)
	{
		this.width = width;
		this.height = height;
		this.position = position;
		geometryManager = GeometryManager.getInstance(); 
		vertexBuffer = geometryManager.createVertexBufferQuad(width, height);
		texCoordBuffer = geometryManager.createTexCoordBufferQuad();
		if(Settings.GLES11Supported) 
		{
			vboId = geometryManager.createVBO(vertexBuffer, texCoordBuffer);
		}
	}
	
	public boolean isPressed(float x, float y)
	{
		if(x < position.x)
			return false;
		if(y < position.y)
			return false;
		if(x > position.x+ width)
			return false;
		if(y > position.y+height)
			return false;

		return true;				
	}
	
	public void render()
	{
		glDisable(GL_TEXTURE_2D);
		
		//just for testing
		if(active)
			glColor4f(1f, 0f, 0f, 1f);
		else
			glColor4f(0f, 0f, 0f, 1f);
		
		glPushMatrix();
		
		glTranslatef(position.x, position.y, 0);
		
		if (!Settings.GLES11Supported) 
		{
			
//			glBindTexture(GL10.GL_TEXTURE_2D, textureID);
			glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
		
			glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		} 
		else 
		{
	
			GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, vboId);
			
//			glBindTexture(GL10.GL_TEXTURE_2D, textureID);
			GLES11.glVertexPointer(3, GL_FLOAT, 0, 0);
			
			GLES11.glTexCoordPointer(2, GL_FLOAT, 0, 12*4); // 4 vertices with 3 coordinates, 4 bytes per float

			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
			GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, 0);
		}
		
		glPopMatrix();
		glEnable(GL_TEXTURE_2D);
		glColor4f(1f, 1f, 1f, 1f);
	}

	/**
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}

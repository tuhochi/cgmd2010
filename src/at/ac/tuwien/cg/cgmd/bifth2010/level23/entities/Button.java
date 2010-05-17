package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.GL_TEXTURE;
import static android.opengl.GLES10.GL_TRIANGLE_STRIP;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES10.glTexCoordPointer;
import static android.opengl.GLES10.glTranslatef;
import static android.opengl.GLES10.glVertexPointer;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TextureAtlas;

/**
 * Button is used to define buttons shown in the level. 
 * It is used to define position, width, height, active state and others.
 * 
 * @author Markus Ernst
 * @author Florian Felberbauer
 */

public class Button
{
	/** The position of the button. */
	private Vector2 position;
	
	/** The width of the button. */
	private float width;
	
	/** The height of the button. */
	private float height;
	
	/** The active of the button. */
	private boolean active=true;
	
	/** The vertex buffer. */
	private FloatBuffer vertexBuffer;
	
	/** The texture part. */
	private TexturePart texture;
	
	/** The unique vertex buffer id. */
	private int vboId;
	
	/** The geometry manager. */
	private GeometryManager geometryManager;
//	private int textureID=0; 
	
	/**
	 * Constructor to set width, height, position, as well as instantiating the <code>GeometryManager</code><p>
	 * Additionally, <code>FrontBuffer</code> vertexBuffer and texCoordBuffer are created. Then, the VBOs are generated
	 * @param width describes the width of the button 
	 * @param height describes the height of the button 
	 * @param position describes the 2d position (x,y) of the button, where (0,0) is left bottom  
	 * 
	 */
	public Button(float width, float height, Vector2 position)
	{
		this.width = width;
		this.height = height;
		this.position = position;
	}
	
	public void preprocess()
	{
		geometryManager = GeometryManager.instance; 
		vertexBuffer = geometryManager.createVertexBufferQuad(width, height);
		texture = TextureAtlas.instance.getBoostButtonTextur();
		if(Settings.GLES11Supported) 
		{
			vboId = geometryManager.createVBO(vertexBuffer, texture.texCoords);
		}
	}
	
	/**
	 * Checks if the button is pressed or not.
	 *
	 * @param x x-coordinate of the position
	 * @param y y-coordinate of the position
	 * @return true if the button is pressed, false else
	 */
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
	
	/**
	 * Renders the button <p>
	 * If GLES11 is supported, the button is rendered using VBOs <p>
	 * Else, Vertex Arrays are used.
	 */
	public void render()
	{
		
		if(!active)
		{
			glMatrixMode(GL_TEXTURE);
			glPushMatrix();
			glTranslatef(texture.dimension.x, 0, 0);		
		}
		
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		
		glTranslatef(position.x, position.y, 0);
		
		if (!Settings.GLES11Supported) 
		{
			glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.texCoords);
		
			glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		} 
		else 
		{
			geometryManager.bindVBO(vboId);
		
			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
		}
		
		if(!active)
		{
			glMatrixMode(GL_TEXTURE);
			glPopMatrix();
		}
		
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();
	}

	/**
	 * Gets the button's position.
	 *
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * Sets the button's position.
	 *
	 * @param position the position to set
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}

	/**
	 * Gets the width of the button.
	 *
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Sets the width of the button.
	 *
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * Gets the height of the button.
	 *
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * sets the height of the button.
	 *
	 * @param height the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * Returns the active state of the button.
	 *
	 * @return true if the button is allowed to be pressed, false otherwise
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * sets the active state of the button (allowed to be pressed?).
	 *
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}

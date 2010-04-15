package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;
import static android.opengl.GLES10.GL_FLOAT;
import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.GL_TEXTURE;
import static android.opengl.GLES10.GL_TRIANGLE_STRIP;
import static android.opengl.GLES10.glBindTexture;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES10.glTexCoordPointer;
import static android.opengl.GLES10.glTranslatef;
import static android.opengl.GLES10.glVertexPointer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES10;
import android.opengl.GLES11;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;

/**
 * This class does the background
 * It is using a full screen quad and maps the texture on it
 * @author Markus Ernst
 * @author Florian Felberbauer
 *
 */
public class Background implements SceneEntity 
{

	private static final long serialVersionUID = -8706705496517584380L;
	private FloatBuffer vertexBuffer;
	private int textureID;
	private FloatBuffer texCoordBuffer;
	private final float[] texCoords = {0.f, 0.5f, 1.f, 0.5f, 0.f, 0.f, 1.f, 0.f}; 
	private float scrollSpeed = 0.01f;
	private float positionY;
	private boolean gameOver = false; 
	private int vboId;
	
	/**
	 * Default constructor, which calls @see #preprocess() 
	 */
	public Background()
	{
		//texCoordBuffer = new FloatBuffer[2];		
		preprocess();
	}
	
	/**
	 * Writes to stream
	 * @param dos Stream to write to
	 */
	public void writeToStream(DataOutputStream dos) {
		try {
			dos.writeFloat(positionY);
			dos.writeFloat(scrollSpeed);
			
		} catch (Exception e) {
			System.out.println("Error writing to stream in Background.java: "+e.getMessage());
		}
		
	}
	
	/**
	 * Reads from stream
	 * @param dis Stream to read from 
	 */
	
	public void readFromStream(DataInputStream dis) {
		try {
			positionY = dis.readFloat(); 
			scrollSpeed = dis.readFloat(); 
			
		} catch (Exception e) {
			System.out.println("Error reading from stream in Background.java: "+e.getMessage());
		}
	}
	
	/**
	 * Creates the <code>FrontBuffer</code> used for rendering
	 * It uses VBO if GLES11 is supported and vertex arrays otherwise
	 */
	public void preprocess() {
		
		RenderView renderView = RenderView.instance; 
		GeometryManager geometryManager = GeometryManager.instance;
		
		texCoordBuffer = geometryManager.createTexCoordBufferQuad(texCoords)[0];
		vertexBuffer = geometryManager.createVertexBufferQuad(renderView.getRightBounds(), renderView.getTopBounds());
		//texCoordBuffer = geometryManager.createTexCoordBufferQuad(texCoords);
		if(Settings.GLES11Supported) 
		{
			vboId = geometryManager.createVBO(vertexBuffer, texCoordBuffer);
		}
		
	}
	
	/**
	 * Sets the unique texture id 
	 * @param texID texture id 
	 */
	public void setTextureID(int texID)
	{
		textureID = texID;
	}
	
	/**
	 * Updates the vertical position, depending on time 
	 * @param dt time elapsed
	 */
	public void update(float dt)
	{
		positionY -= dt*scrollSpeed/RenderView.instance.getTopBounds();
		
//		if(positionY <= renderView.getTopBounds()*-1)
//		{
//			positionY += renderView.getTopBounds(); 
//		}
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.SceneEntity#render()
	 */
	/**
	 * Renders the background
	 */
	@Override
	public void render() 
	{
		glMatrixMode(GL_TEXTURE);
		
		glPushMatrix();
		
			//translate texture
			if(!isGameOver())
				glTranslatef(0, positionY, 0);
			
			glBindTexture(GL10.GL_TEXTURE_2D, textureID);
			if (!Settings.GLES11Supported) 
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
	
	/**
	 * Returns the state if the level is game over or not
	 * @return true if level is game over, else otherwise
	 */
	public boolean isGameOver() {
		return gameOver; 
	}
	
	/**
	 * Sets the game over state
	 * @param isDead boolean which states if the level is game over
	 */
	public void setGameOver(boolean isGameOver) {
		this.gameOver = isGameOver;
	}
	
	public void reset()
	{
		preprocess();
	}

}

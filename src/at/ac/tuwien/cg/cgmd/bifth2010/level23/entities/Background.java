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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES10;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TextureAtlas;

/**
 * This class does the background
 * It is using a full screen quad and maps the texture on it
 * @author Markus Ernst
 * @author Florian Felberbauer
 *
 */
public class Background implements SceneEntity 
{
	/** The serialization id. */
	private static final long serialVersionUID = -8706705496517584380L;
	
	/** The vertex buffer. */
	private FloatBuffer vertexBuffer;
	
	/** The texture part. */
	private TexturePart texture;
	
	/** The scroll speed (texture space). */
	private float scrollSpeed = 0.01f;
	
	/** The current y coordinate. */
	private float positionY;
	
	/** The game over status. */
	private boolean gameOver = false; 
	
	/** The VBO id. */
	private int vboId;
	
	/** The Geometry Manager. */
	private GeometryManager geometryManager = GeometryManager.instance;
	
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
		
		texture = TextureAtlas.instance.getBackgroundTextur();
		vertexBuffer = geometryManager.createVertexBufferQuad(renderView.getRightBounds(), renderView.getTopBounds());
		//texCoordBuffer = geometryManager.createTexCoordBufferQuad(texCoords);
		if(Settings.GLES11Supported) 
		{
			vboId = geometryManager.createVBO(vertexBuffer, texture.texCoords);
		}
		
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
			
			if (!Settings.GLES11Supported) 
			{
				glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.texCoords);
				glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
				
				GLES10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4); 
			} 
			else 
			{
				geometryManager.bindVBO(vboId);
				glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
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
	 * @param isGameOver boolean which states if the level is game over
	 */
	public void setGameOver(boolean isGameOver) {
		this.gameOver = isGameOver;
	}
	
	public void reset()
	{
		preprocess();
	}

}

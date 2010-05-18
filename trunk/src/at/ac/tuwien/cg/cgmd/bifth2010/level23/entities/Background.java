package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;
import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.GL_TEXTURE;
import static android.opengl.GLES10.GL_TEXTURE_2D;
import static android.opengl.GLES10.GL_TRIANGLE_STRIP;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES10.glTexCoordPointer;
import static android.opengl.GLES10.glTexParameterf;
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
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Vector2;

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
	private TexturePart[] textures;
	
	/** The scroll speed (texture space). */
	private float scrollSpeed = 0.01f;
	
	/** The current y coordinate. */
	private float positionY;
	
	/** The game over status. */
	private boolean gameOver = false; 
	
	/** The VBO id. */
	private int[] vboIDs;
	
	/** The Geometry Manager. */
	private GeometryManager geometryManager = GeometryManager.instance;
	
	private int textureLoopValue;
	
	private boolean repeat = false;
	
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
			dos.writeInt(textureLoopValue);
			dos.writeBoolean(repeat);
			
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
			textureLoopValue = dis.readInt();
			repeat = dis.readBoolean();
			
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
		textures = new TexturePart[8];
		vboIDs = new int[8];	
		
		vertexBuffer = geometryManager.createVertexBufferQuad(renderView.getRightBounds(), renderView.getTopBounds());
		//texCoordBuffer = geometryManager.createTexCoordBufferQuad(texCoords);
		
		int index = 0;
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<2;j++)
			{
				textures[index] = TextureAtlas.instance.generateTexturePart(new Vector2(256*i,512*j), new Vector2(256*i+256,512*j+512));
				
				if(Settings.GLES11Supported) 
					vboIDs[index] = geometryManager.createVBO(vertexBuffer, textures[index].texCoords);
				
				index++;
			}
		}		
	}
		
	/**
	 * Updates the vertical position, depending on time 
	 * @param dt time elapsed
	 */
	public void update(float dt)
	{
		if(!repeat)
			positionY -= dt*scrollSpeed;
		else
			positionY -= dt*scrollSpeed/(RenderView.instance.getTopBounds()*2);
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
		if(repeat)
		{
			glTexParameterf(GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
	        glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			glMatrixMode(GL_TEXTURE);
		}
		glPushMatrix();
	
			if(!repeat && positionY + RenderView.instance.getTopBounds() <= 0)
			{
				textureLoopValue++;
				if(textureLoopValue == 6)
				{
					textureLoopValue=6;
					repeat = true;
				}
				positionY = 0;
			}
			
			glTranslatef(0, positionY, 0);
			
			if (!Settings.GLES11Supported) 
			{
				glTexCoordPointer(2, GL10.GL_FLOAT, 0,textures[textureLoopValue].texCoords);
				glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
				
				glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4); 
			} 
			else 
			{
				geometryManager.bindVBO(vboIDs[textureLoopValue]);
				glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
			}
			
			if(!repeat)
			{
				glTranslatef(0, RenderView.instance.getTopBounds(), 0);
									
				if (!Settings.GLES11Supported) 
				{
					glTexCoordPointer(2, GL10.GL_FLOAT, 0, textures[textureLoopValue].texCoords);
					glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
					
					glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4); 
				} 
				else 
				{
					geometryManager.bindVBO(vboIDs[textureLoopValue+1]);
					glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
				}
			}
			
			glPopMatrix();
			if(repeat)
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

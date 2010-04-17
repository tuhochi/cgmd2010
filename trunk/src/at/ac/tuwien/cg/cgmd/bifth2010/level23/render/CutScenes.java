package at.ac.tuwien.cg.cgmd.bifth2010.level23.render;

import static android.opengl.GLES10.*;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES11;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;

/**
 * The Class CutScenes handles cutscene rendering.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class CutScenes 
{
	/** The vertex buffer. */
	private FloatBuffer vertexBuffer;
	
	/** The texture coordinates buffer. */
	private FloatBuffer texCoordBufferIntro;
	
	/** The unique vertex buffer id. */
	private int vboId;
	
	/** The unique texture id for intro scene. */
	private int textureIdIntro;
	
	/** The current time in intro rendering. */
	private float introTime;
	
	/** The current scaling for intro quad. */
	private float introScale=50;
	
	/** The current alpha for fade quad. */
	private float fadeAlpha=1;
	
	/** The time a full fade takes in millseconds */
	private final float FADE_TIME = 3000;
	
	/**
	 * Loads the geometry
	 */
	public void preprocess()
	{
		GeometryManager geometryManager = GeometryManager.instance; 
		vertexBuffer = geometryManager.createVertexBufferQuad(1f,1f);
		texCoordBufferIntro = geometryManager.createTexCoordBufferQuad();
		
		if(Settings.GLES11Supported) 
		{
			vboId = geometryManager.createVBO(vertexBuffer, texCoordBufferIntro);
		}
	}
	
	/**
	 * Renders the introscene
	 * @param dt
	 */
	public void renderIntroScene(float dt) 
	{
		bindBuffers();
		
		introTime += dt *0.05f;
		introScale -= dt*0.05f;
		
		glPushMatrix();
		
		glScalef(introScale, introScale, 1f);
		glTranslatef((RenderView.instance.getRightBounds()/2f-introScale/2f)/introScale, 
				(RenderView.instance.getTopBounds()/2-introScale/2f)/introScale, 0f);		
		
		glBindTexture(GL10.GL_TEXTURE_2D, textureIdIntro);

		if (!Settings.GLES11Supported) 
		{
			glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBufferIntro);
		} 

		glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		glPopMatrix();
	}
	
	/**
	 * Fades the whole screen in FADE_TIME milliseconds
	 * @param dt
	 */
	public void renderFade(float dt)
	{
		bindBuffers();
		fadeAlpha -= dt*1/FADE_TIME;
			
		glDisable(GL_TEXTURE_2D);
		glColor4f(0, 0, 0, fadeAlpha);
		
		glPushMatrix();
		
			glScalef(RenderView.instance.getRightBounds(),
					RenderView.instance.getTopBounds(), 0f);	
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			
		glPopMatrix();
		glColor4f(1, 1, 1, 1);
		glEnable(GL_TEXTURE_2D);
	}
	
	/**
	 * Binds the vertex buffers
	 */
	private void bindBuffers()
	{
		if (!Settings.GLES11Supported) 
		{
			glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBufferIntro);
			glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		} 
		else 
		{
			GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, vboId);

			GLES11.glVertexPointer(3, GL_FLOAT, 0, 0);

			GLES11.glTexCoordPointer(2, GL_FLOAT, 0, 12 * 4); 
		}
	}
}

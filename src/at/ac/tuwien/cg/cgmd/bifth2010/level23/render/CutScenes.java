package at.ac.tuwien.cg.cgmd.bifth2010.level23.render;

import static android.opengl.GLES10.*;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES11;
import android.sax.StartElementListener;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TextureManager;

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
	
	private final float introStartScale=50;
	
	/** The current scaling for intro quad. */
	private float introScale=50;
	
	/** The current alpha for fade quad. */
	private float fadeAlpha=1;
	
	/** The time a full fade takes in millseconds */
	private final float FADE_TIME = 3000;
	
	public int introTexId;
	
	private float introTexShift=0;
	
	private boolean introInGoState=false;
	
	private float introTexScale = 1;
	
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
	public boolean renderIntroScene(float dt) 
	{
		bindBuffers();
		
		introTime += dt *0.05f;
		introScale -= dt*0.05f;
		
		if(introScale<=0)
		{
			if(!introInGoState)
			{
				introScale=introStartScale;
				introTexShift += 1;
			}
			else
			{
				return true;
			}

			
			if(introTexShift == 3)
			{
				introInGoState = true;
				introTexShift = 1.5f;
				introTexScale=2;
			}
		}
			glBindTexture(GL_TEXTURE_2D, introTexId);
			
			glMatrixMode(GL_TEXTURE);
			glPushMatrix();
			
				glScalef(introTexScale*(1f/5f), 1, 1);	
				glTranslatef(introTexShift, 0, 0);
					
			glMatrixMode(GL_MODELVIEW);
			
			glPushMatrix();
			
			glScalef(introScale, introScale, 1f);
			glTranslatef((RenderView.instance.getRightBounds()/2f-introScale/2f)/introScale, 
					(RenderView.instance.getTopBounds()/2-introScale/2f)/introScale, 0f);		
	
			if (!Settings.GLES11Supported) 
			{
				glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBufferIntro);
			} 
	
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			glMatrixMode(GL_TEXTURE);
			glPopMatrix();
			
			glMatrixMode(GL_MODELVIEW);
			glPopMatrix();
		
		return false;
	}
	
	/**
	 * @param dt
	 * @return
	 */
	public boolean renderFade(float dt)
	{
		fadeAlpha -= dt*1/FADE_TIME;
		
		if(fadeAlpha<=0)
			return true;
		
		glDisable(GL_TEXTURE_2D);
		glColor4f(0, 0, 0, fadeAlpha);
		
		glPushMatrix();
		
			glScalef(RenderView.instance.getRightBounds(),
					RenderView.instance.getTopBounds(), 0f);	
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			
		glPopMatrix();
		glColor4f(1, 1, 1, 1);
		glEnable(GL_TEXTURE_2D);
		
		return false;
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

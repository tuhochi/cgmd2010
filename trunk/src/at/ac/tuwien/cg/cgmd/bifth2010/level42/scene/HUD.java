package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import static android.opengl.GLES10.*;

import java.nio.FloatBuffer;

import android.opengl.GLES11;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Color4;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.MaterialManager.Material;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.OGLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.SceneLoader;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.TimeManager;

/**
 * The Class HUD.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class HUD
{
	/** The ogl manager. */
	private final OGLManager oglManager = OGLManager.instance;
	
	/** The time manager */
	private final TimeManager timeManager = TimeManager.instance;
	
	/** The material manager. */
	private final MaterialManager materialManager = MaterialManager.instance;
	
	/** The height. */
	private float width,height;
	
	/** The aspect. */
	private float aspect;
	
	/** The circle. */
	private final FloatBuffer circleVertices;
	private final FloatBuffer circleTexcoords;
	
	/** The circle num vertices. */
	private final int circleNumVertices;
	
	/** The circle num bytes. */
	private final int circleVerticesNumBytes;
	private final int circleTexcoordsNumBytes;
	
	/** The OpenGL rendermode for the circle (GL_TRIANGLES etc) */
	private final int circleRenderMode;
	
	/** The circle vb oid. */
	private int circleVBOid;
	
	/** The circle transformation (used for rendering) */
	private final Matrix44 circleTransformation;
	
	private final Vector2 circleCenter;
	private long circleStartMillis;
	
	/** whether a circle should be drawn after the next update */
	private boolean circleActive;
	
	/** whether a circle should drawn now */
	private boolean circleRender;
	
	/** The material. */
	private Material material;
	
	/** The initialized. */
	private boolean initialized;
	
	/**
	 * Instantiates a new HUD.
	 */
	public HUD()
	{
		/* circle (triangle fan) start */
//		int stepsize = 10;
//		float centerX = 0.0f;
//		float centerY = 0.0f;
//		float radius = 0.5f;
//		
//		circleNumVertices = (360/stepsize)+2;
//		
//		float[] vertices = new float[circleNumVertices*2];
//		int index = 0;
//		vertices[index++] = centerX;
//		vertices[index++] = centerY;
//		for(int i=0; i<(360+stepsize); i+=stepsize)
//		{
//			vertices[index++] = centerX + (float)Math.sin(Math.toRadians(i)) * radius;
//			vertices[index++] = centerY + (float)Math.cos(Math.toRadians(i)) * radius;
//		}
//		circleRenderMode = GL_TRIANGLE_FAN;
		/* circle (triangle fan) end */
		
		/* quad (triangles) start */
		float[] vertices = new float[] {
			-0.5f,-0.5f,	// upper left
			-0.5f, 0.5f,	// lower left
			 0.5f,-0.5f,	// upper right
			-0.5f, 0.5f,	// lower left
			 0.5f, 0.5f,	// lower right
			 0.5f,-0.5f		// upper right
		};
		float[] texcoords = new float[] {
			0.0f, 1.0f,		// upper left
			0.0f, 0.0f,		// lower left
			1.0f, 1.0f,		// upper right
			0.0f, 0.0f,		// lower left
			1.0f, 0.0f,		// lower right
			1.0f, 1.0f		// upper right
		};
		circleNumVertices = 6;
		circleRenderMode = GL_TRIANGLES;
		/* quad (triangles) end */
		
		circleVerticesNumBytes = circleNumVertices*8;
		circleTexcoordsNumBytes = circleNumVertices*8;
		circleVertices = SceneLoader.instance.arrayToBuffer(vertices);
		circleTexcoords = SceneLoader.instance.arrayToBuffer(texcoords);
		
		circleTransformation = new Matrix44();
		circleCenter = new Vector2();
		
		initialized = false;
		circleActive = false;
		circleRender = false;
		material = materialManager.getMaterial("HUDMaterial", new Color4(0.5f,0.5f,0.5f,0.5f), new Color4(0.5f,0.5f,0.5f,0.5f), new Color4(0.5f,0.5f,0.5f,0.5f), new Color4(0.5f,0.5f,0.5f,0.5f), 0.5f, "l42_stripebox");
	}
	
	/**
	 * Pre_render.
	 */
	private void pre_render()
	{	
		// switch to projection
		glMatrixMode(GL_PROJECTION);
		
		// push projection
		glPushMatrix();
		
		// reset projection
		glLoadIdentity();
		
		// make projection ortho (with = aspect, height = 1)
		glOrthof(0, aspect, 1, 0, 0, 1);
		
		// switch to modelview again
		glMatrixMode(GL_MODELVIEW);
		
		// reset modelview
		glLoadIdentity();
	}
	
	/**
	 * Render.
	 */
	public void render()
	{
		if(!initialized)
			init();

		if(!circleRender)
			return;
		
		pre_render();
		
		oglManager.clientState(true, false, true);
		
		materialManager.bindMaterial(material);
		
		glMultMatrixf(circleTransformation.getArray16(), 0);
		
		if(Config.GLES11)
		{
			oglManager.bindVBO(circleVBOid);
			GLES11.glVertexPointer(2, GL_FLOAT, 0, 0);
			GLES11.glTexCoordPointer(2, GL_FLOAT, 0, circleVerticesNumBytes);
		}
		else
		{
			glVertexPointer(2, GL_FLOAT, 0, circleVertices);
			glTexCoordPointer(2, GL_FLOAT, 0, circleTexcoords);
		}
		glDrawArrays(circleRenderMode, 0, circleNumVertices);
		
		post_render();
	}
	
	/**
	 * Post_render.
	 */
	private void post_render()
	{
		// switch to projection
		glMatrixMode(GL_PROJECTION);
		
		// pop projection
		glPopMatrix();
		
		// switch to modelview
		glMatrixMode(GL_MODELVIEW);
	}
	
	/**
	 * Update.
	 */
	public void update()
	{
		int[] viewport = oglManager.getViewport();
		width = viewport[2];
		height = viewport[3];
		aspect = width/height;
		circleRender = circleActive;
		
		float scale = ((float)Math.min(timeManager.getTimeOfLastFrame()-circleStartMillis,Config.MAX_LONG_PRESS_TIME))/1000.0f;
		circleTransformation.setScale(scale,scale,scale);
		circleTransformation.addTranslate(circleCenter.x, circleCenter.y, 0);
	}
	
	
	/**
	 * Sets the circle's position and size
	 *
	 * @param centerPixelsX the center_x in screen pixels
	 * @param centerPixelsY the center_y in screen pixels
	 */
	public void setCircle(float centerPixelsX, float centerPixelsY)
	{
		circleActive = true;
		circleCenter.x = ((float)centerPixelsX / width)*aspect;
		circleCenter.y = (float)centerPixelsY / height;
		circleStartMillis = timeManager.getTimeOfLastFrame();
	}
	
	/**
	 * @param circleActive the circleActive to set
	 */
	public void setCircleActive(boolean circleActive)
	{
		this.circleActive = circleActive;
	}

	/**
	 * Inits the HUD
	 */
	public void init()
	{
		if(!initialized)
		{
			int[] buffers = new int[1];
			GLES11.glGenBuffers(1, buffers, 0);
			circleVBOid = buffers[0];
			oglManager.bindVBO(circleVBOid);
			GLES11.glBufferData(GLES11.GL_ARRAY_BUFFER, circleVerticesNumBytes + circleTexcoordsNumBytes, null, GLES11.GL_STATIC_DRAW);
			GLES11.glBufferSubData(GLES11.GL_ARRAY_BUFFER, 0, circleVerticesNumBytes, circleVertices);
			GLES11.glBufferSubData(GLES11.GL_ARRAY_BUFFER, circleVerticesNumBytes, circleTexcoordsNumBytes, circleTexcoords);
			
			material.init();
			
			initialized = true;
		}
	}
	
	/**
	 * De-inits the HUD
	 */
	public void deInit()
	{
		initialized = false;
		material.deInit();
	}
}

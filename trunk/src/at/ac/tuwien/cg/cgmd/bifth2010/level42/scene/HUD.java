package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import static android.opengl.GLES10.*;

import java.nio.FloatBuffer;

import android.opengl.GLES11;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Color4;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.MaterialManager.Material;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.OGLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.SceneLoader;

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
	
	/** The material manager. */
	private final MaterialManager materialManager = MaterialManager.instance;
	
	/** The height. */
	private float width,height;
	
	/** The aspect. */
	private float aspect;
	
	/** The circle. */
	private final FloatBuffer circle;
	
	/** The circle num vertices. */
	private int circleNumVertices;
	
	/** The circle num bytes. */
	private int circleNumBytes;
	
	/** The circle vb oid. */
	private int circleVBOid;
	
	/** The circle transformation (used for rendering) */
	private Matrix44 circleTransformation;
	
	/** The circle transformation (updated by logic thread, copied to circleTransformation in the update() method) */
	private Matrix44 circleTransformationTemp;
	
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
		int stepsize = 10;
		circleNumVertices = 360/stepsize;
		circleNumBytes = circleNumVertices*8;
		
		float[] vertices = new float[circleNumVertices*2];
		int index = 0;
		for(int i=0; i<360; i+=stepsize)
		{
			vertices[index++] = 0.0f + (float)Math.sin(Math.toRadians(i)) * 0.5f;
			vertices[index++] = 0.0f + (float)Math.cos(Math.toRadians(i)) * 0.5f;
		}
		
		circle = SceneLoader.instance.arrayToBuffer(vertices);
		
		circleTransformation = new Matrix44();
		circleTransformationTemp = new Matrix44();
		
		initialized = false;
		circleActive = false;
		circleRender = false;
		material = materialManager.getMaterial("HUDMaterial", Color4.RED, Color4.RED, Color4.BLACK, Color4.BLACK, 0, null);
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
		
		oglManager.clientState(true, false, false);
		
		materialManager.bindMaterial(material);
		
		glMultMatrixf(circleTransformation.getArray16(), 0);
		
		if(Config.GLES11)
		{
			oglManager.bindVBO(circleVBOid);
			GLES11.glVertexPointer(2, GL_FLOAT, 0, 0);
		}
		else
		{
			glVertexPointer(3, GL_FLOAT, 0, circle);
		}
		glDrawArrays(GL_LINE_LOOP, 0, circleNumVertices);
		
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
		circleRender = circleActive;
		circleTransformation.copy(circleTransformationTemp);
		int[] viewport = oglManager.getViewport();

		width = viewport[2];
		height = viewport[3];
		aspect = width/height;
	}
	
	
	/**
	 * Sets the circle's position and size
	 *
	 * @param centerPixelsX the center_x in screen pixels
	 * @param centerPixelsY the center_y in screen pixels
	 * @param scale 1.0f means the diameter is as big as the screen's height (in landspace orientation)
	 */
	public void setCircle(float centerPixelsX, float centerPixelsY, float scale)
	{
		circleActive = true;
		float transX = ((float)centerPixelsX / width)*aspect;
		float transY = (float)centerPixelsY / height;
		circleTransformationTemp.setScale(scale, scale, scale);
		circleTransformationTemp.addTranslate(transX, transY, 0);
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
			GLES11.glBufferData(GLES11.GL_ARRAY_BUFFER, circleNumBytes, circle, GLES11.GL_STATIC_DRAW);
			
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

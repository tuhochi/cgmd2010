package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import static android.opengl.GLES10.*;

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
	
	/** The circle transformation (used for rendering) */
	private final Matrix44 circleTransformation;
	
	/** The center  of the current circle */
	private final Vector2 circleCenter;
	
	/** The timestamp of the current circles start */
	private long circleStartMillis;
	
	/** whether a circle should be drawn after the next update */
	private boolean circleActive;
	
	/** whether a circle should drawn now */
	private boolean circleRender;
	
	/** The circle. */
	private final Geometry circle;
	
	/** The material. */
	private Material material;
	
	/** The initialized. */
	private boolean initialized;
	
	/**
	 * Instantiates a new HUD.
	 */
	public HUD()
	{
		float[] vertices = new float[] {
			-0.5f,-0.5f, 0.0f,	// upper left
			-0.5f, 0.5f, 0.0f,	// lower left
			 0.5f,-0.5f, 0.0f,	// upper right
			-0.5f, 0.5f, 0.0f,	// lower left
			 0.5f, 0.5f, 0.0f,	// lower right
			 0.5f,-0.5f, 0.0f	// upper right
		};
		float[] texcoords = new float[] {
			0.0f, 0.0f,		// upper left
			0.0f, 1.0f,		// lower left
			1.0f, 0.0f,		// upper right
			0.0f, 1.0f,		// lower left
			1.0f, 1.0f,		// lower right
			1.0f, 0.0f		// upper right
		};
		int numVertices = 6;
		
		circle = new Geometry(
				SceneLoader.instance.arrayToBuffer(vertices),
				null,
				SceneLoader.instance.arrayToBuffer(texcoords),
				null,
				null,
				numVertices);
		
		circleTransformation = new Matrix44();
		circleCenter = new Vector2();
		
		initialized = false;
		circleActive = false;
		circleRender = false;
		material = materialManager.getMaterial("HUDMaterial", 
				new Color4(0.5f,0.5f,0.5f,0.1f), 
				new Color4(0.5f,0.5f,0.5f,0.1f), 
				new Color4(0.5f,0.5f,0.5f,0.1f), 
				new Color4(0.5f,0.5f,0.5f,0.1f), 
				0.5f, 
				"l42_circle");
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
	public void render(int rendermode)
	{
		if(!initialized)
			init();

		if(!circleRender)
			return;
		
		pre_render();
		
		materialManager.bindMaterial(material);
		
		glMultMatrixf(circleTransformation.getArray16(), 0);
		
		circle.render(rendermode);
		
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
			material.init();
			circle.init();
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
		circle.deInit();
	}
}

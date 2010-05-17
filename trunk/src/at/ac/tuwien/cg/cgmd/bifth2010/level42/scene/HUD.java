package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import static android.opengl.GLES10.*;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.AxisAlignedBox3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Color4;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Sphere;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.MaterialManager.Material;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.OGLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.SceneLoader;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.SoundManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.TimeManager;

/**
 * The Class HUD.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class HUD extends Model
{
	/** The ogl manager. */
	final OGLManager oglManager = OGLManager.instance;
	
	/** The time manager */
	final TimeManager timeManager = TimeManager.instance;
	
	final SoundManager soundManager = SoundManager.instance;
	
	/** The height. */
	private float width,height;
	
	/** The aspect. */
	private float aspect;
	
	/** The center  of the current circle */
	private final Vector2 circleCenter;
	
	/** The timestamp of the current circles start */
	private long circleStartMillis;
	
	/** whether a circle should be drawn after the next update */
	private boolean circleActive;
	
	/** whether a circle should drawn now */
	private boolean circleRender;
	
//	private MediaPlayer chargePlayer;
	
	/**
	 * Instantiates a new HUD.
	 */
	public HUD()
	{
		super();
		name = "HUD";
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
		
		Geometry circle = new Geometry(
				SceneLoader.instance.arrayToBuffer(vertices),
				null,
				SceneLoader.instance.arrayToBuffer(texcoords),
				new AxisAlignedBox3(new Vector3(-0.5f, -0.5f, 0.0f), new Vector3(0.5f, 0.5f, 0.0f)),
				new Sphere(new Vector3(0,0,0), 0.707106781f),
				numVertices);
		
		circleCenter = new Vector2();
		
		circleActive = false;
		circleRender = false;
		Material material = materialManager.getMaterial("HUDMaterial", 
				new Color4(1.0f,1.0f,1.0f,1.0f), 
				new Color4(0.0f,0.0f,0.0f,0.0f), 
				new Color4(0.0f,0.0f,0.0f,0.0f), 
				new Color4(0.0f,0.0f,0.0f,1.0f), 
				0.5f, 
				"l42_circle");
		
		add(circle, material);
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
		
		// make projection ortho (width = aspect, height = 1)
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
		if(!circleRender)
			return;
		
		pre_render();

		super.render(rendermode);
		
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
	public void update(Matrix44 sceneEntityTransformation)
	{
		int[] viewport = oglManager.getViewport();
		width = viewport[2];
		height = viewport[3];
		aspect = width/height;
		circleRender = circleActive;
		
		if(circleRender)
		{
			long circleTime = timeManager.getTimeOfLastFrame()-circleStartMillis;
			float circlePercent = (float)Math.min(circleTime,Config.MAX_LONG_PRESS_TIME) / (float)Config.MAX_LONG_PRESS_TIME;
			float scale = circlePercent * Config.MAX_FORCE_VIS_SIZE;
			float angle = circleTime * Config.FORCE_VIS_ROTATION_SPEED;
			transformation.setScale(scale,scale,scale);
			transformation.addRotateZ(angle);
			transformation.addTranslate(circleCenter.x, circleCenter.y, 0);
		}
//		else if(chargePlayer != null && chargePlayer.isPlaying())
//		{
//			chargePlayer.pause();
//			chargePlayer = null;
//		}
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
//		chargePlayer = soundManager.playSound(R.raw.l42_loadforce);
	}
	
	/**
	 * @param circleActive the circleActive to set
	 */
	public void disableCircle()
	{
		this.circleActive = false;
	}
}

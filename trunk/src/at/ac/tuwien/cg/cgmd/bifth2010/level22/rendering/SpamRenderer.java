package at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering;

import static android.opengl.GLES10.GL_PROJECTION;
import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.gamelogic.MailDataBase;


/**
 * This class is responsible for rendering and displaying the whole scene.
 * 
 * @author Sulix
 */
public class SpamRenderer extends GLSurfaceView implements GLSurfaceView.Renderer {

	/**
	 * Constructs a new renderer
	 * 
	 * @param context The current viewcontext, which this renderer will be attached to
	 */
	public SpamRenderer ( Context context )
	{
		
		super( context );
		setRenderer( this );
		SpamRenderer.context = context;
		SpamRenderer.activate();
	}
	@Override
	public void onDrawFrame(GL10 arg0) 
	{
	
		if ( isActive )
		{
			
			arg0.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			arg0.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
			arg0.glClearDepthf(1.0f);
			arg0.glEnable(GL10.GL_DEPTH_TEST);
			arg0.glDepthFunc(GL10.GL_LEQUAL);
			arg0.glShadeModel(GL10.GL_SMOOTH);
			arg0.glEnable(GL10.GL_TEXTURE_2D);
			arg0.glEnable(GL10.GL_CULL_FACE);
			
			cachedRenderContext = arg0;
			
			MailSceneObject.init( arg0, context );
			MailDataBase.displayMails( arg0 );
			
			arg0.glDisable( GL10.GL_TEXTURE_2D );
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) 
	{

		gl.glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective( gl, 30.0f, ( float ) width / height, 1.0f, 100.0f );
		glMatrixMode(GL_MODELVIEW);
		
		cachedRenderContext = gl;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Returns the current cached render context
	 * 
	 * @return the current render context
	 */
	public static GL10 getActContext ()
	{
		
		return cachedRenderContext;
	}
	
	/**
	 * Stops the renderer
	 */
	public static synchronized void deactivate ()
	{
		
		isActive = false;
	}
	
	/**
	 * Reactivates the renderer
	 */
	public static synchronized void activate ()
	{
		
		isActive = true;
	}

	private static GL10 cachedRenderContext;
	private static Context context;
	private static boolean isActive;
}

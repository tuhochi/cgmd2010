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
import at.ac.tuwien.cg.cgmd.bifth2010.R;
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
		
		width = 0.0f;
		height = 0.0f;
		fov = 30.0f;
		np = 1.0f;
		fp = 100.0f;
		
		setRenderer( this );
		SpamRenderer.context = context;
		SpamRenderer.activate();
		bg = new Sprite( 100.0f, R.drawable.l22_background );
		menu = new Sprite( 0.0f, R.drawable.l22_menubar );
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
			arg0.glEnable( GL10.GL_LIGHTING );
			arg0.glEnable( GL10.GL_LIGHT0 );
			arg0.glEnable( GL10.GL_RESCALE_NORMAL );
			float lightPos[] = { 0.0f, 0.0f, 0.0f, 1.0f };
			arg0.glLightfv( GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0 );
			
			cachedRenderContext = arg0;
			
			MailSceneObject.init( arg0, context );
			MailDataBase.displayMails( arg0 );
			
			arg0.glDisable( GL10.GL_LIGHTING );
			arg0.glDisable( GL10.GL_NORMALIZE );
			
			glMatrixMode( GL_PROJECTION );
			glLoadIdentity();
			GLU.gluOrtho2D( arg0, 0, width, 0, height );
			
			bg.draw( context, arg0 );
			menu.draw( context, arg0 );
			
			glLoadIdentity();
			GLU.gluPerspective( arg0, fov, (float) width / (float) height, np, fp );
			glMatrixMode( GL_MODELVIEW );
			
			arg0.glDisable( GL10.GL_TEXTURE_2D );
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) 
	{

		gl.glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		this.width = width;
		this.height = height;
		GLU.gluPerspective( gl, 30.0f, ( float ) width / height, 1.0f, 100.0f );
		glMatrixMode(GL_MODELVIEW);
		
		bg.changeSize( 0, width, 0, height - 45 );
		menu.changeSize( 0, width, height - 45, height );
		
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
	private static Sprite bg;
	private static Sprite menu;
	
	private float width;
	private float height;
	private float np;
	private float fp;
	private float fov;
}

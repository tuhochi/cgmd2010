package at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering;

import static android.opengl.GLES10.GL_PROJECTION;
import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLU.gluOrtho2D;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;


public class SpamRenderer extends GLSurfaceView implements GLSurfaceView.Renderer {

	public SpamRenderer ( Context context )
	{
		
		super( context );
		setRenderer( this );
	}
	@Override
	public void onDrawFrame(GL10 arg0) 
	{
	
		arg0.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		arg0.glClearColor(1.0f, 0.0f, 0.0f, 0.5f);
		arg0.glClearDepthf(1.0f);
		arg0.glEnable(GL10.GL_DEPTH_TEST);
		arg0.glDepthFunc(GL10.GL_LEQUAL);
		arg0.glShadeModel(GL10.GL_SMOOTH);
		
		// TODO : Draw my scene
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) 
	{

		gl.glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluOrtho2D( gl, 0, 0, width, height );
		glMatrixMode(GL_MODELVIEW);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		
	}

	
}

package at.ac.tuwien.cg.cgmd.bifth2010.level77;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import javax.microedition.khronos.egl.EGLConfig;

import android.opengl.GLSurfaceView.Renderer;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.Cylinder;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.Rectangle;

//one of the bloody things needed for getResources()
import android.content.Context;


public class L77Renderer implements Renderer {
	private float mAngle;
	private Cylinder mCube;
    private boolean mTranslucentBackground;
    private Context mContext;

    public L77Renderer(boolean useTranslucentBackground, Context aContext) {
        mTranslucentBackground = useTranslucentBackground;
        mContext = aContext;
    }

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Adapt CubeRend to perosnal stuff
		 /*
         * Usually, the first thing one might want to do is to clear
         * the screen. The most efficient way of doing this is to use
         * glClear().
         */

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        /*
         * Now we're ready to draw some 3D objects
         */

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -3.0f);
        gl.glRotatef(mAngle,        0, 1, 0);
        gl.glRotatef(mAngle*0.25f,  1, 0, 0);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        mCube.draw(gl);

        gl.glRotatef(mAngle*2.0f, 0, 1, 1);
        gl.glTranslatef(0.5f, 0.5f, 0.5f);

        mCube.draw(gl);

        mAngle += 1.2f;


	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Change default
        gl.glViewport(0, 0, width, height);

        /*
         * Set our projection matrix. This doesn't have to be done
         * each time we draw, but usually a new projection needs to
         * be set when the viewport is resized.
         */

        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Change default stuff
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        gl.glDisable(GL10.GL_DITHER);

        /*
         * Some one-time OpenGL initialization can be made here
         * probably based on features of this particular context
         */
         gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                 GL10.GL_FASTEST);

         if (mTranslucentBackground) {
             gl.glClearColor(0,0,0,0);
         } else {
             gl.glClearColor(1,1,1,1);
         }
         gl.glEnable(GL10.GL_CULL_FACE);
         gl.glShadeModel(GL10.GL_SMOOTH);
         gl.glEnable(GL10.GL_DEPTH_TEST);
         
        mCube = new Cylinder(0.5f, 0.10f, (short) 10);
 		mCube.setTexture(gl, mContext.getResources(), R.drawable.l00_coin);
 		mCube.setColor(0.85f, 0.68f, 0.22f, 1.f);
	}

}

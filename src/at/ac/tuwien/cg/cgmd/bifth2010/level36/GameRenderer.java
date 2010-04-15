package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import static android.opengl.GLU.gluLookAt;
import static android.opengl.GLU.gluPerspective;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//import android.R;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class GameRenderer  implements GLSurfaceView.Renderer {
    private boolean mTranslucentBackground;
    private Cube mCube;
    private Plane mPlane;
    private float mAngle;
    private TextureManager tm;
    private InputStream is;
	private Context context;
	private int texId;
	private GL10 gl;
	
    public GameRenderer(boolean useTranslucentBackground, Context context) {
        this.mTranslucentBackground = useTranslucentBackground;
        this.mCube = new Cube();
        this.mPlane = new Plane();
        this.context = context;
        //is = this.context.getResources().openRawResource(R.drawable.l36_los);
    }

    public void onDrawFrame(GL10 gl) {
        /*
         * Usually, the first thing one might want to do is to clear
         * the screen. The most efficient way of doing this is to use
         * glClear().
         */

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        //gl.glBindTexture(gl.GL_TEXTURE_2D, this.texId);
        
        /*
         * Now we're ready to draw some 3D objects
         */

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        //Camera
        gluLookAt(gl, 0, 0, -20, 0, 0, 0, 0, 1, 0  );
        //Camera Ende
//        gl.glTranslatef(0, 0, -3.0f);
//        gl.glRotatef(mAngle,        0, 1, 0);
//        gl.glRotatef(mAngle*0.25f,  1, 0, 0);

        //gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        //gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
        mPlane.draw(gl);
        //mCube.draw(gl);

//        gl.glRotatef(mAngle*2.0f, 0, 1, 1);
//        gl.glTranslatef(0.5f, 0.5f, 0.5f);
//
//        mPlane.draw(gl);
        //mCube.draw(gl);

        mAngle += 1.2f;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
         gl.glViewport(0, 0, width, height);

         /*
          * Set our projection matrix. This doesn't have to be done
          * each time we draw, but usually a new projection needs to
          * be set when the viewport is resized.
          */

         float ratio = (float) width / height;
         gl.glMatrixMode(GL10.GL_PROJECTION);
         gl.glLoadIdentity();
         //gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
         gluPerspective(gl, 45.0f, ratio, 0.1f, 100.0f);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        gl.glDisable(GL10.GL_DITHER);
//        this.tm = new TextureManager(this.context);
//        this.texId = tm.loadTexture(gl, R.drawable.l36_los);

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
        // gl.glEnable(GL10.GL_TEXTURE_2D);
        // gl.glBindTexture(GL10.GL_TEXTURE_2D, this.texId);
    }

}

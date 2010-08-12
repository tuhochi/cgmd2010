package at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.World;

/**
 * Our own GlRenderer
 * @author MaMa
 *
 */
public class JMRenderer implements GLSurfaceView.Renderer {

    private boolean mTranslucentBackground;
    private World mWorld;
    
    /**
     * Creates a new Renderer
     * @param useTranslucentBackground Should we render to a transparent backgprund
     * @param world The world
     */
    public JMRenderer(boolean useTranslucentBackground, World world) {
        mTranslucentBackground = useTranslucentBackground;
        mWorld = world;
    }

    public void onDrawFrame(GL10 gl) {

    	mWorld.draw(gl);
    }

    
    public int[] getConfigSpec() {
        if (mTranslucentBackground) {
                // We want a depth buffer and an alpha buffer
                int[] configSpec = {
                        EGL10.EGL_RED_SIZE,      8,
                        EGL10.EGL_GREEN_SIZE,    8,
                        EGL10.EGL_BLUE_SIZE,     8,
                        EGL10.EGL_ALPHA_SIZE,    8,
                        EGL10.EGL_DEPTH_SIZE,   16,
                        EGL10.EGL_NONE
                };
                return configSpec;
            } else {
                // We want a depth buffer, don't care about the
                // details of the color buffer.
                int[] configSpec = {
                        EGL10.EGL_DEPTH_SIZE,   16,
                        EGL10.EGL_NONE
                };
                return configSpec;
            }
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
    	
    	mWorld.onSurfaceChanged(gl, width, height);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
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
                 GL10.GL_NICEST);

         if (mTranslucentBackground) {
             gl.glClearColor(0,0,0,0);
         } else {
             gl.glClearColor(1.0f,0,0,1.0f);
         }
         gl.glDisable(GL10.GL_CULL_FACE);
         gl.glShadeModel(GL10.GL_SMOOTH);
         gl.glEnable(GL10.GL_DEPTH_TEST);
         gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA);
         
         mWorld.init(gl);
    }
}

package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import static android.opengl.GLES11.*;
//import static android.opengl.GLES11Ext.*;
import static android.opengl.GLU.*;
import static android.opengl.GLUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.opengles.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.opengl.GLSurfaceView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class GameRenderer implements GLSurfaceView.Renderer {
    private boolean mTranslucentBackground;
    private Cube mCube;
    private Plane mPlane;
    private Point mPoint;
    private float mAngle;
    private TextureManager tm;
    private InputStream is;
	private Context context;
	private int texId;
	private GL10 gl;
	private Camera cam;
	
    public GameRenderer(boolean useTranslucentBackground, Context context) {
        this.mTranslucentBackground = useTranslucentBackground;
        this.context = context;
        this.cam = new Camera();
       	this.cam.lookAt(0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        //is = this.context.getResources().openRawResource(R.drawable.l36_los);
    }

    public void onDrawFrame(GL10 gl) {
        /*
         * Usually, the first thing one might want to do is to clear
         * the screen. The most efficient way of doing this is to use
         * glClear().
         */
    	//System.out.println(gl.getClass());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glBindTexture(GL_TEXTURE_2D, this.texId);  
       
        /*
         * Now we're ready to draw some 3D objects
         */
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        //Camera
        glMultMatrixf(this.cam.getMatrix().getArray(), 0);
        //gluLookAt(gl, 0, 0, -20, 0, 0, 0, 0, 1, 0  );  
     
        //Camera Ende
//        gl.glTranslatef(0, 0, -3.0f);
//        gl.glRotatef(mAngle,        0, 1, 0);
//        gl.glRotatef(mAngle*0.25f,  1, 0, 0);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        //gl.glClientActiveTexture(GL10.GL_TEXTURE0);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
           
        mPlane.draw(gl);
        
        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        
        glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
        
        mPoint.draw(gl);
        //mCube.draw(gl);

//        gl.glRotatef(mAngle*2.0f, 0, 1, 1);
//        gl.glTranslatef(0.5f, 0.5f, 0.5f);
//
//        mPlane.draw(gl);
        //mCube.draw(gl);
        
        mAngle += 1.2f;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
    	
    	
    	glViewport(0, 0, width, height);
    	if (glIsEnabled(GL_VIEWPORT)) {
    		System.out.println("VIEWPORT IS ENABLED.");
    		int[] view = new int[4];
    		glGetIntegerv(GL_VIEWPORT, view, 0);
    		for (int i = 0; i < view.length; i++) {
    			System.out.println("View "+i+" "+view[i]);
    		}
    		
//    		IntBuffer ib = IntBuffer.allocate(4);
//    		glGetIntegerv(GL_VIEWPORT, view, 0);
//    		for (int i = 0; i < ib.array().length; i++) {
//    			System.out.println("View 2 "+i+" "+ib.get(i));
//    		}
    	}

         /*
          * Set our projection matrix. This doesn't have to be done
          * each time we draw, but usually a new projection needs to
          * be set when the viewport is resized.
          */

        float ratio = (float) width / height;
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        //gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
        gluPerspective(gl, 45.0f, ratio, 0.1f, 100.0f);
                  
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.mCube = new Cube();
        this.mPlane = new Plane();
        this.mPoint = new Point(gl);
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        glDisable(GL_DITHER);
//        this.tm = new TextureManager(this.context);
//        this.texId = tm.loadTexture(gl, R.drawable.l36_los);

        /*
         * Some one-time OpenGL initialization can be made here
         * probably based on features of this particular context
         */
         glHint(GL_PERSPECTIVE_CORRECTION_HINT,
        		 GL_FASTEST);

         if (mTranslucentBackground) {
             gl.glClearColor(0,0,0,0);
         } else {
             gl.glClearColor(1,1,1,1);
         }
         gl.glEnable(GL_CULL_FACE);
         gl.glShadeModel(GL_SMOOTH);
         gl.glEnable(GL_DEPTH_TEST);
      
         gl.glEnable(GL_TEXTURE_2D);
        
        InputStream is = this.context.getResources().openRawResource(R.drawable.l36_los);
 		Bitmap bitmap = null;
 		try
 		{
 			bitmap = BitmapFactory.decodeStream(is);
 		}
 		catch(Throwable t)
 		{
 			//Log.e("TextureLoader::loadTexture", "Could not load Texture.");
 			bitmap = null;
 		}
 		finally
 		{
 			//Always clear and close
 			try
 			{
 				is.close();
 				is = null;
 			}
 			catch (IOException e)
 			{
 			}
 		}
 		
 		//bitmap = Bitmap.createScaledBitmap(bitmap, 1024, 1024, true);
 		bitmap = bitmap.copy(Bitmap.Config.ARGB_4444, false);
 		
 		int width = bitmap.getWidth();
 		int height = bitmap.getHeight();
 		System.err.println("Loaded texture: " + width + "x" + height);

 		int[] textures = new int[1];
		glGenTextures(1, textures, 0);
		this.texId = textures[0];
		glBindTexture(GL_TEXTURE_2D, this.texId);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_GENERATE_MIPMAP, GL_TRUE);
		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

         //gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
         //GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
   	
         bitmap.recycle();
    }
    
    public Point getPoint() {
    	return this.mPoint;    	
    }

}

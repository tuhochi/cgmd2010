package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.os.SystemClock;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


public class TextureTest extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new GLSurfaceView(this);

        mRenderer = new TestRenderer(this);
        mGLView.setRenderer(mRenderer);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }


    private GLSurfaceView mGLView;
    private TestRenderer mRenderer;

}

class TestRenderer implements GLSurfaceView.Renderer {

	private Context mContext;
	private FloatBuffer mVertexBuffer, mTexBuffer;
	private int mTextureId;

    public TestRenderer(Context context) {
        mContext = context;


        ByteBuffer vbb = ByteBuffer.allocateDirect(4 * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();

        ByteBuffer tbb = ByteBuffer.allocateDirect(4 * 2 * 4);
        tbb.order(ByteOrder.nativeOrder());
        mTexBuffer = tbb.asFloatBuffer();

        mVertexBuffer.put(new float[] {

        		0.0f,0.0f,0.0f,
        		480.0f,0.0f,0.0f,
        		0.0f,854.0f,0.0f,
        		480.0f,854.0f,0.0f

        } );

        mVertexBuffer.position(0);

        mTexBuffer.put(new float[] {

        		0.0f,0.0f,
        		1.0f,0.0f,
        		0.0f,1.0f,
        		1.0f,1.0f

        } );

        mTexBuffer.position(0);

    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    	gl.glViewport(0, 0, 480, 854);

    	gl.glMatrixMode(GL10.GL_PROJECTION);
    	gl.glLoadIdentity();
    	GLU.gluOrtho2D(gl,0,480,854,0);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
    	gl.glLoadIdentity();

        gl.glEnable(GL10.GL_TEXTURE_2D);

	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

    	gl.glDisable(GL10.GL_DITHER);
    	gl.glDisable(GL10.GL_LIGHTING);
    	gl.glShadeModel(GL10.GL_FLAT);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);


        InputStream is = mContext.getResources().openRawResource(R.drawable.l00_icon);

        Bitmap bitmap;
		try {
		    bitmap = BitmapFactory.decodeStream(is);
		} finally {
		    try {
		        is.close();
		    } catch(IOException e) {
		    }
		}

		bitmap = Bitmap.createScaledBitmap(bitmap, 1024, 1024, true);
		//bitmap = bitmap.copy(Bitmap.Config.ARGB_4444, false);

		int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        mTextureId = textures[0];

        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);

        //gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);//NEAREST_MIPMAP_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_NEAREST);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,GL10.GL_REPLACE);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();

        gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);


    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
        gl.glEnable(GL10.GL_TEXTURE_2D);
    }

	private long lastTime;
	private int i;

    public void onDrawFrame(GL10 gl) {

		if (i == 100) {
    		i = 0;
    		Log.v("GL2D",""+(SystemClock.uptimeMillis()-lastTime)/100.0f);
    		lastTime = SystemClock.uptimeMillis();
    	}
		i++;

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gl.glActiveTexture(GL10.GL_TEXTURE0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

    }
}
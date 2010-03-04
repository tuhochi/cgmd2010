package at.ac.tuwien.cg.cgmd.bifth2010.level00;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;


public class GlTestActivity extends Activity {
	private static final String CLASS_TAG = GlTestActivity.class.getName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        GLSurfaceView sf = new GLSurfaceView(this);
        MyRenderer renderer = new MyRenderer(); 
        sf.setRenderer(renderer);
        
        
        FrameLayout fl = new FrameLayout(this);
        fl.addView(sf);
        setContentView(fl);
        
        
        
    }
    
    private class MyRenderer implements GLSurfaceView.Renderer {

		@Override
		public void onDrawFrame(GL10 gl) {
			
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			String strExtensions = gl.glGetString(GL10.GL_EXTENSIONS);
			Log.d(CLASS_TAG, "OpenGL extensions: "+strExtensions);
			
		}
    	
    };
    
   
}
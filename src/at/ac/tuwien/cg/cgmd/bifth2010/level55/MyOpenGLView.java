package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import java.util.StringTokenizer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;


public class MyOpenGLView extends GLSurfaceView {
	
    public MyOpenGLView(Context context) {
        super(context);
        Texture.setContext(context);
        Sound.setContext(context);
        MyRenderer.setContext(context);
    }

    public MyOpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    static boolean isExtensionSupported(GL10 gl, String extension) {
    	String extensions;
    	if (gl instanceof GL11) {
    		GL11 gl11=(GL11)gl;
    		extensions=gl11.glGetString(GL11.GL_EXTENSIONS);
    	} else {
    		extensions=gl.glGetString(GL10.GL_EXTENSIONS);
    	}
    	
    	StringTokenizer st = new StringTokenizer(extensions);
		while (st.hasMoreTokens()) {
			if (st.nextToken().equals(extension)) {
				return true;
			}
		}
    	
    	
		return false;
    }
}

package at.ac.tuwien.cg.cgmd.bifth2010.level55;

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
}

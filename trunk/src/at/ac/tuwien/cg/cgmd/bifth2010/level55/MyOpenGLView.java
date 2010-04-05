package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class MyOpenGLView extends GLSurfaceView {
	
    public MyOpenGLView(Context context) {
        super(context);
        Texture.setContext(context);
        Sound.setContext(context);
    }

    public MyOpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}

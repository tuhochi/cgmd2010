package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class RendererView extends GLSurfaceView {

	private RenderTask renderer; //< Renderer
	
	/**
	 * Ctor.
	 * @param context
	 */
	public RendererView(Context context) {
		super(context);
        setFocusable(true);
        
        GeometryFactory gf = new GeometryFactory();
		Geometry geom = gf.createQuad(1, 1);
        
        renderer = new RenderTask(geom);
        setRenderer(renderer);
           
        Thread updTask = new Thread(new UpdateTask(geom));
        updTask.start();
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            return true;
        }
        Log.i("Input", "Key down pressed");
        return super.onKeyDown(keyCode, event);
    }
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            return true;
        }
        Log.i("Input", "Key up pressed");
        return super.onKeyDown(keyCode, event);
    }

	public boolean  onTouchEvent(MotionEvent event) {
		Log.i("Input", "Touch event");
		return false;
	}
}

package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class LevelSurfaceView extends GLSurfaceView {

	public LevelSurfaceView(Context context) {
		super(context);
		mRenderer = new LevelRenderer(context);
        setRenderer(mRenderer);
        setFocusableInTouchMode(true);
	}
	
	public boolean onTouchEvent(final MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
	        queueEvent(new Runnable(){
	            public void run() {
	                mRenderer.movePlayer(5.0f,5.0f);
	            }});
        	return true;
		} else return false;
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN)
			switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
				queueEvent(new Runnable(){
					public void run() {
						mRenderer.movePlayer(0.0f,-5.0f);
	            }});
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				queueEvent(new Runnable(){
					public void run() {
						mRenderer.movePlayer(0.0f,5.0f);
	            }});
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				queueEvent(new Runnable(){
					public void run() {
						mRenderer.movePlayer(-5.0f,0.0f);
	            }});
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				queueEvent(new Runnable(){
					public void run() {
						mRenderer.movePlayer(5.0f,0.0f);
	            }});
				break;
			}
        return super.onKeyDown(keyCode, event);
		
    }

	
	LevelRenderer mRenderer;
}

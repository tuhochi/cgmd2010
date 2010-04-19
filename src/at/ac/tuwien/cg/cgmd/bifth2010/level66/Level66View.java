/**
 * Flight66 - a trip to hell
 * 
 * @author brm, dwi
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level66;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Level66View extends GLSurfaceView {

	private Level66Renderer _renderer;
	private float _x = 0;
    private float _y = 0;
	
	public Level66View(Context context) {
		super(context);
		
		//load test sound/s
        Sound.loadSound(context);
        
		_renderer = new Level66Renderer(context);
        setRenderer(_renderer);
        this.setFocusableInTouchMode(true);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            queueEvent(new Runnable() {
                public void run() {
                    _renderer.moveLeft();
                }});
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            queueEvent(new Runnable() {
                public void run() {
                    _renderer.moveRight();
                }});
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            queueEvent(new Runnable() {
                public void run() {
                    _renderer.moveUp();
                }});
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            queueEvent(new Runnable() {
                public void run() {
                    _renderer.moveDown();
                }});
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            queueEvent(new Runnable() {
                public void run() {
                    _renderer.moveSpecial();
                }});
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

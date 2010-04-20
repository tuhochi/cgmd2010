package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class LevelSurfaceView extends GLSurfaceView {
	
	LevelRenderer mRenderer;
	boolean keydown[] = {false, false, false, false};
	int score = 0;
	float x = 0, y = 0;

	public LevelSurfaceView(Context context) {
		super(context);
		mRenderer = new LevelRenderer(context);
		mRenderer.setPosition(x, y);
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			final int code = keyCode - KeyEvent.KEYCODE_DPAD_UP;
			if (code>=0 && code<=3
					&& !keydown[code]) {
				
				keydown[code]=true;
				queueEvent(new Runnable(){
					public void run() {
						mRenderer.movePlayer(code, 5.0f);
					}
				});
				return true;
			}
		}
        return super.onKeyDown(keyCode, event);
    }
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			final int code = keyCode - KeyEvent.KEYCODE_DPAD_UP;
			if (code>=0 && code<=3 && keydown[code]) {
				
				keydown[code]=false;
				queueEvent(new Runnable(){
					public void run() {
						mRenderer.movePlayer(code, 0.0f);
					}
				});
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
	public int getScore() {return score;}
	public void setScore(int score) {this.score = score+10;}
	public float getPositionX() {return mRenderer.getPositionX();}
	public float getPositionY() {return mRenderer.getPositionY();}
	public void setPosition(float x, float y) {this.x = x; this.y = y;}
	

}

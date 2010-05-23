package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Custom SurfaceView to take care of in game keyboard and touch screen input.
 * Initializes and manages the customized Renderer.
 * 
 * @author      Alexander Fritz
 * @author      Michael Benda
 */
public class LevelSurfaceView extends GLSurfaceView {
	
	LevelRenderer mRenderer;
	boolean keydown[] = {false, false, false, false};
	int score = 0;
	float x = 0, y = 0;

	/**
	 * Class constructor specifying the rendering context.
	 * Initializes the LevelRenderer
	 */
	public LevelSurfaceView(Context context) {
		super(context);
		mRenderer = new LevelRenderer(context);
        setRenderer(mRenderer);
        setFocusableInTouchMode(true);
	}
	
	/**
	 * Handles touch events to pass the event information to the LevelRenderer
	 * 
	 *  @param event	the motion event
	 */
	public boolean onTouchEvent(final MotionEvent event) {
//        queueEvent(new Runnable(){
//            public void run() {
                mRenderer.touchScreen(event.getAction(),event.getX(),event.getY());
//            }});
    	return true;
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			final int code = keyCode - KeyEvent.KEYCODE_DPAD_UP;
			if (code>=0 && code<=3 //up-down-left-right
					&& !keydown[code]) {
				
				keydown[code]=true;
				queueEvent(new Runnable(){
					public void run() {
						if (code == 0 && !mRenderer.jumping) {
//							mRenderer.jumping = true;
							mRenderer.movePlayer(code, 15.0f);
						} else if (code!=0){
							mRenderer.moving = true;
							mRenderer.movePlayer(code, 5.0f);
						}
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
				mRenderer.moving = false;
				if (keydown[2] == true)
					mRenderer.moving = true;
				if (keydown[3] == true)
					mRenderer.moving = true;
				queueEvent(new Runnable(){
					public void run() {
						if (code > 0) {
							mRenderer.movePlayer(code, 0.0f);
						}
					}
				});
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
	public int getScore() {return mRenderer.getScore();}
	public void setScore(int score) {mRenderer.setScore(score);}
	public float getPositionX() {return mRenderer.getPositionX();}
	public float getPositionY() {return mRenderer.getPositionY();}
	public void setPosition(float x, float y) {mRenderer.setPosition(x, y);}
	public String getCoinState() {return mRenderer.getCoinState();}
	public void setCoinState(String state) {mRenderer.setCoinState(state);}

	public void clear() {
		mRenderer.clear();
		
	}
	

}

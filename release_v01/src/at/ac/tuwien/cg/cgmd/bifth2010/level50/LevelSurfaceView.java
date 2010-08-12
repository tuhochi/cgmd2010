package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import android.content.Context;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
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
	 * @param	event	the motion event
	 * @return  always <code>true</code> since the function processes all touch events
	 */
	public boolean onTouchEvent(final MotionEvent event) {
//        queueEvent(new Runnable(){
//            public void run() {
                mRenderer.touchScreen(event.getAction(),event.getX(),event.getY());
//            }});
    	return true;
    }
	
	/**
	 * Handles key press events to pass the event information to the LevelRenderer
	 * Processes only directional pad inputs.
	 * 
	 * @param keyCode	the motion event action identifier
	 * @param event		the motion event
	 * @return          <code>true</code> if the event equals the usage of the directional pad
     * 					<code>false</code> otherwise. 
	 */
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
	
	/**
	 * Handles key release events to pass the event information to the LevelRenderer
	 * Processes only directional pad inputs.
	 * 
	 * @param keyCode	the motion event action identifier
	 * @param event		the motion event
	 * @return          <code>true</code> if the event equals the usage of the directional pad
     * 					<code>false</code> otherwise. 
	 */
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
	
	/**
	 * Returns current game score.
	 */
	public int getScore() {return mRenderer.getScore();}
	
	/**
	 * Sets current game score.
	 */
	public void setScore(int score) {mRenderer.setScore(score);}
	
	/**
	 * Returns current x position of the player.
	 */
	public float getPositionX() {return mRenderer.getPositionX();}
	
	/**
	 * Returns current y position of the player.
	 */
	public float getPositionY() {return mRenderer.getPositionY();}
	
	/**
	 * Sets current position of the player.
	 */
	public void setPosition(float x, float y) {mRenderer.setPosition(x, y);}
	
	/**
	 * Returns a <code>String</code> containing the coded positions of the coins.
	 * 
	 * @see #setCoinState(String)
	 */
	public String getCoinState() {return mRenderer.getCoinState();}
	/**
	 * Sets the positions of the coins using a <code>String</code> code.
	 * <p>
	 * The String contains the indices of the coins which are already replaced.
	 * To ensure the correctness of the String each index is preceded 
	 * by the number of digits of the index.
	 * <p>
	 * E.g. the coins with the indices
	 * 123, 42 and 3314 would be coded as "312324243314".
	 */
	public void setCoinState(String state) {mRenderer.setCoinState(state);}
	/** 
     * Performs cleanup of the LevelSufaceView and the LevelRenderer.
     * Releases any {@link MediaPlayer}.
     */
	public void clear() {
		mRenderer.clear();
		
	}
	

}

package at.ac.tuwien.cg.cgmd.bifth2010.level36.rendering;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.*;
import at.ac.tuwien.cg.cgmd.bifth2010.level36.sound.SoundManager;
import static android.view.KeyEvent.*;
import java.util.Random;


public class GameView_New extends GLSurfaceView {
	
	private GameRenderer gameRenderer;
	private Context context;
	private Random randomizer;
	private SoundManager sm;

	private boolean sound;
	private int shownNumber;

	public GameView_New(Context context, SoundManager sm) {
		super(context);
		this.context = context;
		this.sm = sm;
		this.sound = true;
		this.randomizer = new Random();
		int initRandNumber = getRandomNumber();
		gameRenderer = new GameRenderer(true, this.context, initRandNumber);
		//muss gesetzt werden ansonsten bekommt man keine key events
		//setFocusable(true);
		setRenderer(gameRenderer);
	}
	
	public void disableSound() {
		this.sound = false;
	}
	
	public int getActualCoinValue() {
		return this.shownNumber;
	}
	
	public int getRandomNumber() {
		this.shownNumber = this.randomizer.nextInt(10);
		return this.shownNumber;
	}
	
	public GameRenderer getGameRenderer() {
		return this.gameRenderer;
	}
		
	private void touch_start(float x, float y) {
		if (this.sound)
			sm.startPlayer();
		
		gameRenderer.setClickedXY(x, y);
		//this.setFocusable(true);
		//this.gameRenderer.getPoint().setXY(x, y);
		//gameRenderer.getPoint().setXY(0.5f, 0.5f);
		
//		gameRenderer.showNotification(" X: "+x+" Y: "+y);
//		int randNumber = getRandomNumber();
//		this.gameRenderer.setRandNumber(randNumber);
		
		//System.out.println("HERE");
		//mPath.reset();
		//mPath.moveTo(x, y);
		//mX = x;
		//mY = y;
	}

	private void touch_move(float x, float y) {
		//this.setFocusable(true);
		//this.gameRenderer.setInactivePlane(true);
		//gameRenderer.getPoint().setXY(0.5f, 0.5f);
		gameRenderer.setClickedXY(x, y);
		
		//this.gameRenderer.getPoint().setXY(x, y);
		//this.gameRenderer.getPoint().setXY(coords[0], coords[1]);
		//this.gameRenderer.getPoint().setXY(1.0f, 1.0f);
		
		
		//float dx = Math.abs(x - mX);
		//float dy = Math.abs(y - mY);
		//if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
		//	mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
		//	mX = x;
		//	mY = y;
		//}
	}

	private void touch_up() {
		if (this.sound)
			sm.pausePlayer();
		
		//this.setFocusable(true);
		//gameRenderer.getPoint().setXY(0.5f, 0.5f);
		//this.setFocusable(true);
        //float[] coords = getScreenToWorldCoords(x, y, 0);
		//this.gameRenderer.getPoint().setXY(coords[0], coords[1]);
		
		
		
		//mPath.lineTo(mX, mY);
		// commit the path to our offscreen
		//mCanvas.drawPath(mPath, LevelActivity.paint);
		// kill this so we don't double draw
		//mPath.reset();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		this.setFocusable(true);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		}
		return true;
	}
	
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		//if (keyCode == event.KEYCODE_0) {
//				int randNumber = getRandomNumber();
//				this.gameRenderer.setRandNumber(randNumber);
//		//}
//		return true;
//	}
	
//	@Override
//	public boolean onKeyUp(int keyCode, KeyEvent event) {
//		int number = -1;
//		switch(keyCode) {
//		case KEYCODE_0:
//			number = 0;
//			break;
//		case KEYCODE_1:
//			number = 1;
//			break;
//		case KEYCODE_2:
//			number = 2;
//			break;
//		case KEYCODE_3:
//			number = 3;
//			break;
//		case KEYCODE_4:
//			number = 4;
//			break;
//		case KEYCODE_5:
//			number = 5;
//			break;
//		case KEYCODE_6:
//			number = 6;
//			break;
//		case KEYCODE_7:
//			number = 7;
//			break;
//		case KEYCODE_8:
//			number = 8;
//			break;
//		case KEYCODE_9:
//			number = 9;
//			break;
//		}
//
//		if (this.shownNumber == number) {
//			int randNumber = getRandomNumber();
//			this.gameRenderer.setRandNumber(randNumber);
//		}
//		this.setFocusable(true);
//		return true;
//	}
}
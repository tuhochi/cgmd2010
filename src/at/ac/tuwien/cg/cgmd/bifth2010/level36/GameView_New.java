package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import static android.opengl.GLES10.glReadPixels;
import static android.opengl.GLES11.glGetFloatv;
import static android.opengl.GLES11.glGetIntegerv;
import static android.opengl.GLES11Ext.GL_DEPTH_COMPONENT16_OES;

import static android.opengl.GLU.gluUnProject;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;


public class GameView_New extends GLSurfaceView {
	
		private GameRenderer gameRenderer;
		private Context context;

		public GameView_New(Context context) {
			super(context);
			this.context = context;
			gameRenderer = new GameRenderer(true, this.context);
			setRenderer(gameRenderer);
		}
		
	private void touch_start(float x, float y) {
		this.gameRenderer.getPoint().setXY(x, y);
		//System.out.println("HERE");
		//mPath.reset();
		//mPath.moveTo(x, y);
		//mX = x;
		//mY = y;
	}

	private void touch_move(float x, float y) {
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
}
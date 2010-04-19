package at.ac.tuwien.cg.cgmd.bifth2010.level36.linus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class GameView extends View {
	private Bitmap mBitmap;
	private Bitmap background;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;
	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;
	private GewinnFeld feld;
	
	public GameView(Context c) {
		super(c);
		mBitmap = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
		background = BitmapFactory.decodeResource(getResources(), R.drawable.l36_los_h);
		mCanvas = new Canvas(mBitmap);
		mCanvas.drawBitmap(background, 0,0, mBitmapPaint);
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		feld = new GewinnFeld();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
	
//		canvas.drawText("gewonnen", 100, 100, mBitmapPaint);
		feld.drawFeld(canvas);
		// das bereits gezeichnete
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		// was jetzt gezeichnet wird
		canvas.drawPath(mPath, LevelActivity_Old.paint);
	}

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		// commit the path to our offscreen
		mCanvas.drawPath(mPath, LevelActivity_Old.paint);
		// kill this so we don't double draw
		mPath.reset();
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

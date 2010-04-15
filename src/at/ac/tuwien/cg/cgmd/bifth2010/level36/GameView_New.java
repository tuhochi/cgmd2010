//package at.ac.tuwien.cg.cgmd.bifth2010.level36;
//
//import static android.opengl.GLES10.*;
//import static android.opengl.GLU.gluPerspective;
//
//import java.util.LinkedList;
//
//import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.opengles.GL10;
//import javax.microedition.khronos.opengles.GL11;
//
//import android.content.Context;
//import android.opengl.GLSurfaceView;
//import android.opengl.GLSurfaceView.Renderer;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.view.View;
//import at.ac.tuwien.cg.cgmd.bifth2010.R;
////import at.ac.tuwien.cg.cgmd.bifth2010.level33.GameView;
////import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
////import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2i;
////import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.Camera;
////import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.SceneGraph;
//import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
//import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2i;
//
//public class GameView_New extends GLSurfaceView implements Renderer {
//	
//	private Camera cam;
//	private SceneGraph sceneGraph;
//	public static Vector2f resolution;
//	
//	private Bitmap mBitmap;
//	private Bitmap background;
//	private Canvas mCanvas;
//	private Path mPath;
//	private Paint mBitmapPaint;
//	private float mX, mY;
//	private static final float TOUCH_TOLERANCE = 4;
//	private GewinnFeld feld;
//	
//	public GameView_New(Context c) {
//		super(c);
//		mBitmap = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
//		background = BitmapFactory.decodeResource(getResources(), R.drawable.l36_los_h);
//		mCanvas = new Canvas(mBitmap);
//		mCanvas.drawBitmap(background, 0,0, mBitmapPaint);
//		mPath = new Path();
//		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
//		feld = new GewinnFeld();
//	}
//	
//	public void onDrawFrame(GL10 gl) {
//		sceneGraph.render(gl);
//	}
//
//	@Override
//	public void onSurfaceChanged(GL10 gl, int width, int height) {
//
//		if (height == 0) {
//			height = 1;
//		}
//
//		gl.glViewport(0, 0, width, height);
//		glMatrixMode(GL_PROJECTION);
//		glLoadIdentity();
//		gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
//		resolution = new Vector2f(width, height);
//		Log.d("onSurfaceChanged","jo");
//		
//		
//		
//		
//		
//		// set Game Frustum
//		double x =Camera.standardZoom/Math.sqrt(2)*(resolution.x/resolution.y);
//		double y = x/(resolution.x/resolution.y);
//		
//		SceneGraph.touchDim.set((float)x,(float)y);
//		
//		Log.d("Frustum: x/y",String.valueOf(x)+" "+String.valueOf(y) );
//		
//		Vector2i f= new Vector2i((int)Math.round(x/2+1.5),(int)Math.round(y/2+1.5));
//		SceneGraph.frustumDim.set(f.x,f.y);
//		
//		Log.d("old Frustum: ",String.valueOf(SceneGraph.frustumDim.x)+" "+String.valueOf(SceneGraph.frustumDim.y) );
//		Log.d("new Frustum: ",String.valueOf(f.x)+" "+String.valueOf(f.y) );
//		
//		
//
//	}
//
//	@Override
//	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//		SceneGraph.init(gl); // now init the Geometry VBO´s
//		Log.d("onSurfaceCreated","jo");
//	}
//
//	
//	
//	
//
//	@Override
//	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//		super.onSizeChanged(w, h, oldw, oldh);
//	}
//
//	@Override
//	protected void onDraw(Canvas canvas) {
//		canvas.drawColor(Color.WHITE);
//	
////		canvas.drawText("gewonnen", 100, 100, mBitmapPaint);
//		feld.drawFeld(canvas);
//		// das bereits gezeichnete
//		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
//		// was jetzt gezeichnet wird
//		canvas.drawPath(mPath, LevelActivity.paint);
//	}
//
//	private void touch_start(float x, float y) {
//		mPath.reset();
//		mPath.moveTo(x, y);
//		mX = x;
//		mY = y;
//	}
//
//	private void touch_move(float x, float y) {
//		float dx = Math.abs(x - mX);
//		float dy = Math.abs(y - mY);
//		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
//			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
//			mX = x;
//			mY = y;
//		}
//	}
//
//	private void touch_up() {
//		mPath.lineTo(mX, mY);
//		// commit the path to our offscreen
//		mCanvas.drawPath(mPath, LevelActivity.paint);
//		// kill this so we don't double draw
//		mPath.reset();
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		float x = event.getX();
//		float y = event.getY();
//
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			touch_start(x, y);
//			invalidate();
//			break;
//		case MotionEvent.ACTION_MOVE:
//			touch_move(x, y);
//			invalidate();
//			break;
//		case MotionEvent.ACTION_UP:
//			touch_up();
//			invalidate();
//			break;
//		}
//		return true;
//	}
//
//}
package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;

/**
 * 
 * @author Gruppe 36
 *
 */
public class LevelActivity extends Activity {
	//public static Paint paint;
	private GLSurfaceView mGLSurfaceView;
	private GameView_New gameView; 
	private Context context;
	private OnTouchListener touchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent e) {
			boolean b = cGestureDetector.onTouchEvent(e);
			try {
				Thread.sleep(35);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return b;
		}
	};
	private GestureDetector cGestureDetector = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("LevelActivity::onCreate", "Start the program.");
		super.onCreate(savedInstanceState);
		context = this;
		//EDITED 1504
        //mGLSurfaceView = new GLSurfaceView(this);
        //mGLSurfaceView.setRenderer(new GameRenderer(true, context));
        //setContentView(mGLSurfaceView);
		//EDITED 1504
		
		gameView = new GameView_New(this);
		setContentView(gameView);
		
		//set fullscreen
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Window window = getWindow();
		//window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		/*setContentView(new GameView_New(this));
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(40);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}
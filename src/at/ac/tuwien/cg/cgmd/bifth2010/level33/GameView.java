package at.ac.tuwien.cg.cgmd.bifth2010.level33;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.Camera;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.LevelHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.SceneGraph;

public class GameView extends GLSurfaceView implements OnGestureListener  {

	private GestureDetector gestureScanner; // needed for advanced Gestures
	public static GameRenderer renderer;	// Game's Renderer Loop
	public static SceneGraph sceneGraph;	// Graph with Game Objects
	public static Vector2f resolution;		// resolution of the screen
	public static Vector2f lastTouch = new Vector2f(1,1);	// coordinates of the last touch [0 1] 
	public static Vector2f diffTouch = new Vector2f();	// difference to the last touch
	public static boolean running = true;
	
	long stopTime = 0;
	
	public GameView(Context context) {
		super(context);
		gestureScanner = new GestureDetector(this);
		
		LevelHandler level = new LevelHandler();// init new Level here!
		sceneGraph = new SceneGraph(level);
		renderer = new GameRenderer();
		setRenderer(renderer);

	}

	/**
	 * the onTouchEvent handle the advanced Gesture Scanner
	 */
	public boolean onTouchEvent(final MotionEvent e) {
		
		lastTouch.set(e.getX() / getWidth(), e.getY()/ getHeight());
		
//		// if Level is zoomed out
//		if(SceneGraph.zoomOutView){
//			queueEvent(new Runnable() {
//				public void run() {
//						
//				}
//			});
//		}
//		// GamePlay
//		else{
//			
//			
//		}
		

		return gestureScanner.onTouchEvent(e);
	}


	@Override
	public boolean onDown(final MotionEvent e) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {

		SceneGraph.zoomOutView=!SceneGraph.zoomOutView;
		
		if(SceneGraph.zoomOutView)
			SceneGraph.camera.zoom=Camera.outZoom;
		else
			SceneGraph.camera.zoom=Camera.standardZoom;
		
		System.out.println("onLongPress");
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onShowPress(final MotionEvent e) {
	
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		
		System.out.println("onSingleTapUp");
		return true;
	}

	
	public void startGame() {
		// TODO Auto-generated method stub

		
		
	}

	
	
}



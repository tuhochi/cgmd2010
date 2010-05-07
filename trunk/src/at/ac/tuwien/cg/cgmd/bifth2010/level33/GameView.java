package at.ac.tuwien.cg.cgmd.bifth2010.level33;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.Camera;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.LevelHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.SceneGraph;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.tools.StopTimer;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Orbit;

public class GameView extends GLSurfaceView  {

//	private GestureDetector gestureScanner; // needed for advanced Gestures
//	public static GameRenderer renderer;	// Game's Renderer Loop
//	public static SceneGraph sceneGraph;	// Graph with Game Objects
//	public static Vector2f resolution;		// resolution of the screen
//	public static Vector2f lastTouch = new Vector2f(1,1);	// coordinates of the last touch [0 1] 
//	public static Vector2f lastTouchDown = new Vector2f(1,1);
//	public static Vector2f lastTouchUp = new Vector2f(1,1);
//	public static Vector2f diffTouch = new Vector2f();	// difference to the last touch
//	public static boolean running = true;
//	
//	
	
	public GameView(Context context) {
		super(context);
//		setFocusable(true);
//		requestFocus();
//		
//		gestureScanner = new GestureDetector(this);
//		
//		StopTimer t = new StopTimer();
//		LevelHandler level = new LevelHandler();// init new Level here!
//		t.logTime("Level Generierung dauerte:");
//		
//		sceneGraph = new SceneGraph(level,context);
//		renderer = new GameRenderer();
//		setRenderer(renderer);

	}
	
//
//	
//	
//	
//
//	/**
//	 * the onTouchEvent handle the advanced Gesture Scanner
//	 */
//	public boolean onTouchEvent(final MotionEvent e) {
//		
//		System.out.println("onTouchEvent");
//		lastTouch.set(e.getX() / getWidth(), e.getY()/ getHeight());
//		
//		if(SceneGraph.camera.zoom==SceneGraph.camera.standardZoom)
//			SceneGraph.level.steerTouchEvent(lastTouch);
//		
//		return gestureScanner.onTouchEvent(e);
//	}
//
//
//	@Override
//	public boolean onDown(final MotionEvent e) {
//		System.out.println("onDown");
//		return true;
//	}
//
//	@Override
//	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//			float velocityY) {
//		System.out.println("onFling");
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public void onLongPress(MotionEvent e) {
//		//SceneGraph.camera.switchZoom();
//		System.out.println("onLongPress");
//	}
//
//	@Override
//	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//			float distanceY) {
//		System.out.println("onScroll");
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public void onShowPress(final MotionEvent e) {
//		System.out.println("onShowPress");
//	}
//
//	@Override
//	public boolean onSingleTapUp(MotionEvent e) {
//		// TODO Auto-generated method stub
//		
//		System.out.println("onSingleTapUp");
//		return true;
//	}
//
//	@Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//		
//		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
//			queueEvent(new Runnable() {
//				public void run() {
//					SceneGraph.level.steerCharacterTo(true, -1);
//				}
//			});
//		else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
//			queueEvent(new Runnable() {
//				public void run() {
//					sceneGraph.level.steerCharacterTo(true, 1);
//				}
//			});
//
//		else if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
//			queueEvent(new Runnable() {
//				public void run() {
//					sceneGraph.level.steerCharacterTo(false, -1);
//				}
//			});
//
//		else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
//			queueEvent(new Runnable() {
//				public void run() {
//					sceneGraph.level.steerCharacterTo(false, 1);
//				}
//			});
//		
//		else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
//			queueEvent(new Runnable() {
//				public void run() {
//					sceneGraph.level.demomode=!sceneGraph.level.demomode;
//				}
//			});
//
//		return false;
//	}
//	
//	public void startGame() {
//		// TODO Auto-generated method stub
//
//		
//		
//	}

	
	
}



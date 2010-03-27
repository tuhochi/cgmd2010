package at.ac.tuwien.cg.cgmd.bifth2010.level33;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.Level;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.SceneGraph;

public class GameView extends GLSurfaceView {

	public static GameRenderer renderer;// Game's Renderer Loop
	public static SceneGraph sceneGraph;
	public static Vector2 lastTouch = new Vector2();
	public static boolean running = true;
	

	public GameView(Context context) {
		super(context);

		
		Level level = new Level();// init new Level here!
				
		sceneGraph = new SceneGraph(level);
		renderer = new GameRenderer();
		setRenderer(renderer);

	
	}

	public boolean onTouchEvent(final MotionEvent event) {
		queueEvent(new Runnable() {
			public void run() {
				lastTouch.set(event.getX() / getWidth(), event.getY()
						/ getHeight());
			}
		});
		return true;
	}

	public void startGame() {
		// TODO Auto-generated method stub

	}

}

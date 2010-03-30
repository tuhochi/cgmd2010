package at.ac.tuwien.cg.cgmd.bifth2010.level88;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.game.Game;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;

/**
 * @author Asperger, Radax
 *
 */
public class LevelActivity extends Activity{
	private GLSurfaceView glSurfaceView;
	private GLView normalModeView;
	private Game game;
	private Vector2 windowSize;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	Window window = getWindow();
	 	window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        windowSize = new Vector2((float)d.getWidth(), (float)d.getHeight());
	 	
	 	game = new Game(this);

	 	normalModeView = new GLView(this, game);
	 	glSurfaceView = normalModeView; 
        setContentView(glSurfaceView);

	}

	public void onPause() {
		super.onPause(); 
		glSurfaceView.onPause();
	}

	public void onResume() {
		super.onResume(); 
		glSurfaceView.onResume();
	}

	@Override
    public boolean onTouchEvent (MotionEvent event)
    {
    	if(event.getAction() == MotionEvent.ACTION_MOVE)
    	{
    		game.fingerMove(new Vector2(event.getX() / windowSize.x, event.getY() / windowSize.y));
    	}
    	else if(event.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		game.fingerDown(new Vector2(event.getX() / windowSize.x, event.getY() / windowSize.y));
    	}
    	else if(event.getAction() == MotionEvent.ACTION_UP)
    	{
    		game.fingerUp(new Vector2(event.getX() / windowSize.x, event.getY() / windowSize.y));
    	}
    	return super.onTouchEvent(event);
    }
}

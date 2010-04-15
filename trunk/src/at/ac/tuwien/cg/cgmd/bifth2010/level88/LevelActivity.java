package at.ac.tuwien.cg.cgmd.bifth2010.level88;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.camera.Camera;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.MotionManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.game.Game;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Asperger, Radax
 *
 */
public class LevelActivity extends Activity{
	public static final String TAG = "LevelActivity"; 
	
	private GLSurfaceView glSurfaceView;
	private GLView normalModeView;
	private Game game;
	private Vector2 windowSize;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		
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
        
        glSurfaceView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				game.touchEvent(new Vector2(event.getX() / windowSize.x, event.getY() / windowSize.y));
				return true;
			}
        });

	}
	
	public void onPause() {
		super.onPause(); 
		Log.d(TAG, "onPause()");
		glSurfaceView.onPause();
	}

	public void onResume() {
		super.onResume(); 
		Log.d(TAG, "onResume()");
		glSurfaceView.onResume();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.v(TAG, "onStart()");
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.v(TAG, "onRestart()");
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Log.v(TAG, "onStop()");
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.v(TAG, "onDestroy()");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Log.v(TAG,"onSaveInstanceState(" + outState + ")");
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		Log.v(TAG,"onRestoreInstanceState(" + savedInstanceState + ")");
	}
}

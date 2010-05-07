package at.ac.tuwien.cg.cgmd.bifth2010.level33;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.LevelHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.ProgressHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.SceneGraph;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.SoundHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.tools.StopTimer;

public class LevelActivity extends Activity implements OnGestureListener{
	
	/**
	 * the name of the preference file that stores global user settings 
	 */
	public static final String SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE = "l00_settings";

	/**
	 * global user setting of type boolean that determines wether sound and music is allowed 
	 */
	public static final String PREFERENCE_MUSIC = "music";
	
	public static boolean IS_MUSIC_ON = false;
	private GLSurfaceView openglview;
	private MediaPlayer mAudioPlayer = null;
	public static SoundHandler soundHandler = null;
	public static ProgressHandler progressHandler = null;
	public static Vibrator vibrator;
	
	private GestureDetector gestureScanner; // needed for advanced Gestures
	public static GameRenderer renderer;	// Game's Renderer Loop
	public static SceneGraph sceneGraph;	// Graph with Game Objects
	public static Vector2f resolution;		// resolution of the screen
	public static Vector2f lastTouch = new Vector2f(1,1);	// coordinates of the last touch [0 1] 
	public static Vector2f lastTouchDown = new Vector2f(1,1);
	public static Vector2f lastTouchUp = new Vector2f(1,1);
	public static Vector2f diffTouch = new Vector2f();	// difference to the last touch
	public static boolean running = true;
	public static int height=1; 
	public static int width=1; 
	
	private TextView tvLevelFps;
	private Thread uiThread;


	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{ 
		super.onCreate(savedInstanceState);
		

		
		
		
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.l33_level);
		
		
//		setFocusable(true);
//		requestFocus();
		
		gestureScanner = new GestureDetector(this);
		
		//init progressHandler
		progressHandler = new ProgressHandler();
		
		StopTimer t = new StopTimer();
		LevelHandler level = new LevelHandler();// init new Level here!
		t.logTime("Level Generierung dauerte:");
		
		sceneGraph = new SceneGraph(level,this);
		renderer = new GameRenderer();
        
        //Setting up the soundHandler and mainSoundSettings
        soundHandler = new SoundHandler(this);
        SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
		IS_MUSIC_ON = settings.getBoolean(PREFERENCE_MUSIC, true);
        
        //Setting up my GameRenderer 
     
        openglview = (GLSurfaceView) findViewById(R.id.l33_openglview);
        GameRenderer gameRenderer = new GameRenderer();
        openglview.setRenderer(gameRenderer);
        
        
		//Starts the sound
        if(IS_MUSIC_ON)
        	soundHandler.startLevelAudioPlayer();
        
        // setup vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        
        tvLevelFps = (TextView)findViewById(R.id.l33_level_fps);
	}
	
	
	

	/**
	 * the onTouchEvent handle the advanced Gesture Scanner
	 */
	public boolean onTouchEvent(final MotionEvent e) {
		
		System.out.println("onTouchEvent");
		lastTouch.set(e.getX() / width, e.getY()/ height);
		
		if(SceneGraph.camera.zoom==SceneGraph.camera.standardZoom)
			SceneGraph.level.steerTouchEvent(lastTouch);
		
		return gestureScanner.onTouchEvent(e);
	}


	@Override
	public boolean onDown(final MotionEvent e) {
		System.out.println("onDown");
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		System.out.println("onFling");
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		//SceneGraph.camera.switchZoom();
		System.out.println("onLongPress");
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		System.out.println("onScroll");
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onShowPress(final MotionEvent e) {
		System.out.println("onShowPress");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		
		System.out.println("onSingleTapUp");
		return true;
	}

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
		
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
//			queueEvent(new Runnable() {
//				public void run() {
					SceneGraph.level.steerCharacterTo(true, -1);
//				}
//			});
		else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
//			queueEvent(new Runnable() {
//				public void run() {
					sceneGraph.level.steerCharacterTo(true, 1);
//				}
//			});

		else if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
//			queueEvent(new Runnable() {
//				public void run() {
					sceneGraph.level.steerCharacterTo(false, -1);
//				}
//			});

		else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
//			queueEvent(new Runnable() {
//				public void run() {
					sceneGraph.level.steerCharacterTo(false, 1);
//				}
//			});
		
		else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
//			queueEvent(new Runnable() {
//				public void run() {
					sceneGraph.level.demomode=!sceneGraph.level.demomode;
//				}
//			});

		return false;
	}
	
	
	@Override
	protected void onDestroy() {
		
		//Stop music if it's running
		
		soundHandler.releaseLevelAudioPlayer();
		
		super.onDestroy();
		
	}





}

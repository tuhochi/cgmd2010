package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import static at.ac.tuwien.cg.cgmd.bifth2010.level83.Constants.*;
import android.content.DialogInterface.OnDismissListener;
/**
 * This class implements the {@link Renderer} and the {@link OnTouchListener}.
 * It draws the whole level and responds to user interaction.
 * @author Manuel Keglevic, Thomas Schulz
 */
public class MyRenderer implements Renderer, OnTouchListener,OnDismissListener {
	
	private static final String CLASS_TAG = MyRenderer.class.getName();
	AlertDialog alertDialog;
	
	//Private
	private int height, width;
	private long lastTime;
	private float deltaTime;
	private boolean scrolling;
	private MySprite lenny;

	private AnimationManager aniManager;
	private Context context;
	private MyHexagonGrid hexGrid;
	private ItemQueue items;
	private boolean firstStart = true;
	
	/**Thread for Animation*/
	private Thread aniThread;
	private boolean startAnithread = false;
	private Vibrator vib;
	private GestureDetector cGestureDetector;
	private OnGestureListener gestureListener = new OnGestureListener() {

		@Override
		public boolean onDown(MotionEvent e) {
			//Log.d(CLASS_TAG, "onDown");
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
//			Log.d(CLASS_TAG, "onFling");
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
//			Log.d(CLASS_TAG, "onLongPress");
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
//			Log.d(CLASS_TAG, "onScroll");
			if (e1.getX() < width - items.itemWidth - 20 || e1.getX() > width || 
					e1.getY() < 0 || e1.getY() > items.itemHeight + 20) {
//				Log.d(CLASS_TAG, "X:" + e1.getX() + " - Y:" + e1.getY());
				return true;
			}
			scrollingHandler.sendEmptyMessage(1);
			items.centerItem(e2.getX(), e2.getY());
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
//			Log.d(CLASS_TAG, "onShowPress");
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
//			Log.d(CLASS_TAG, "onSingleTapUp");
			return true;
		}
	};
	
	/**
	 * Handler for setting the <code>scrolling</code> boolean from different 
	 * Threads.
	 */
	private Handler scrollingHandler = new Handler(){
    	
    	@Override
    	public void handleMessage(Message msg){
    		super.handleMessage(msg);
    		if (msg.what == 0)
    			scrolling = false;
    		else if (msg.what == 1)
    			scrolling = true;
    	}
    };
	
	//Public
    /**
     * Current frames per second.
     */
	public float fps;
	
	/**
	 * Constructor for the renderer. Context is needed to access the resources.
	 * 
	 * @param context Context to access the resources
	 */
	public MyRenderer(Context context) {
		this.context = context;
		
		cGestureDetector = new GestureDetector(gestureListener);
	}
	
	/**
	 * Renders the level and everything else.
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		//Start anithread if boolean is set
		if(startAnithread){
			aniThread.start();
			startAnithread = false;
		}
		
		//Calculate Time
		long time = System.nanoTime();
		deltaTime = (time - lastTime) / 1000000000f;
		lastTime = time;
	
		fps = 1f/deltaTime;
		
		//Animate - notify Animate Thread
		synchronized(aniThread){
			aniThread.notifyAll();
		}
		
		//Draw
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		hexGrid.Draw(gl);
		lenny.Draw(gl);
		items.Draw(gl);
		gl.glFlush();
	}

	/**
	 * Initializes viewport dependent classes and objects like {@link MyHexagonGrid}.
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
//		Log.d("Renderer", "OnSurfaceChanged");
		this.width = width;
		this.height = height;
		gl.glViewport(0, 0, width, height);
		
		Constants.setViewPort(height, 9);
		
		lenny.width = height/18;
		lenny.height = height/12;
		
		hexGrid.setWidth(width,height);
		
		items.updateViewport(width, height);

		//Create Animator for Lenny and add it to the animation manager
		LennyAnimator lennyAnimator = new LennyAnimator();
		lenny.setAnimator(lennyAnimator);
		aniManager.addAnimatable(lennyAnimator);
		
		hexGrid.resumeGame();
		
//		Log.d("Renderer","Element width="+GRID_ELEMENT_WIDTH);
//		Log.d("Viewport-SurfaceChanged","Size: "+width+"x"+height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
//		Log.d("Renderer", "OnSurfaceCreated");
		//OpenGL initializations
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_DITHER);
        gl.glDisable(GL10.GL_LIGHTING);
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0.0f, width, 0.0f, height, 0.0f, 1.0f);
        
        gl.glShadeModel(GL10.GL_FLAT);
        
        gl.glClearColor(0.349f, 0f, 0.349f, 1f);
        
//        Log.d("GL","onSurfaceCreated");
        
        //Reload TextureManager of build level from scratch
        if(MyTextureManager.singleton != null)
        	MyTextureManager.singleton.reload(gl);
        else
        	init(gl);
       
        //Animation Thread
        aniThread = new DaemonThread(
    			
    			new Runnable() {
    				
    				@Override
    				public void run() {
    					
						try {
    						
    						while(true){
								synchronized (aniThread) {
									aniThread.wait();
								}
    							aniManager.animate(deltaTime);
    						}	
						} catch (InterruptedException e) {
							Log.d("Anithread","Interrupt");
						}
    					
    				}
    			}
    		);
        
        //Start Animation Thread
        if(!firstStart)
        	startAnithread = true;
	}
	
	/**
	 * Resumes the game by setting the required boolean. The game is resumed
	 * in the next {@link MyRenderer#onDrawFrame(GL10)}.
	 */
	public void resumeGame(){
		lastTime = System.nanoTime();
		startAnithread = true;
	}
	
	/**
	 * This function is called when the level is started for the first time and
	 * creates all objects which are necessary for rendering the level.
	 * 
	 * @param gl
	 */
	private void init(GL10 gl){
		Log.d("Renderer", "Init");
		
		//Initialize TextureManager
        new MyTextureManager(context, 30);
	        
		lenny = new MySprite(TEXTURE_LENNY, 20f*R_HEX/2f, 2f*RI_HEX-DIF_HEX-RI_HEX, 15, 21, gl);
		hexGrid = new MyHexagonGrid( TEXTURE_HEXAGON, gl, context,TEXTURE_MAP);
		
		aniManager = new AnimationManager(5);
		
		aniManager.addAnimatable(hexGrid);
		hexGrid.setCharacterControl(lenny);
		hexGrid.setVibrator(vib);

		items = new ItemQueue(4, gl);
		items.put(ItemQueue.WALL);
		items.put(ItemQueue.BOMB);
		items.put(ItemQueue.BOMB);
		items.put(ItemQueue.DELETEWALL);
        
        //Init time
		lastTime = System.nanoTime();
	}

	/**
	 * This method should be called, when the game/application is paused, to 
	 * stop running threads.
	 */
	public void onPause() {
		if (hexGrid != null) hexGrid.pauseGame();
		
		if (aniThread != null)
			aniThread.interrupt();
	}

	/**
	 * This method should be called, when the game/application is stopped, to 
	 * free resources.
	 */
	public void onStop() {
	}
	
	/**
	 * This method should be called, when the game/application is resumed.
	 */
	public void onResume() {
//		aniThread.start();
		
	}
	
	/**
	 * Destroys the {@link MyTextureManager}, {@link MyHexagonGrid} and {@link AnimationManager}.
	 */
	public void onDestroy(){
		MyTextureManager.singleton = null;
		aniManager = null;
		hexGrid = null;
	}
	
	/**
	 * Sets the vibrator
	 * @param v Vibrator
	 */
	public void setVibrator(Vibrator v) {
		vib = v;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		//pass on the touch event to the GestureDetector
    	if (!cGestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {

    		if (scrolling) {
//				Log.d(CLASS_TAG, "! onUp ! - X:" + event.getX() + " / Y:" + event.getY());
				
    			//set scrolling to false
				scrollingHandler.sendEmptyMessage(0);
				if (!hexGrid.useItem(event.getX(), event.getY(), items.getItemType()))
					items.resetItem();
				else
					items.put(ItemQueue.RANDOM);
    		}
    	}
    	try {
    		//this hack prevents the frame rate from dropping dramatically during interaction.
    		//described at: 
    		//http://groups.google.com/group/android-developers/browse_frm/thread/39eea4d7f6e6dfca
    		Thread.sleep(35);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	return true;
	}
	
	/**
	 * DaemonThread class for animation thread.
	 * @author horm
	 */
	private class DaemonThread extends Thread {
		public DaemonThread(Runnable r) {
			super(r);
			setDaemon(true);
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		firstStart = false;
		resumeGame();
	}
}

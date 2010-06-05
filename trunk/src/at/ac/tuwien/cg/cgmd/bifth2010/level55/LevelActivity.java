package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import java.util.Timer;
import java.util.TimerTask;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * The Level Activity
 * @author Wolfgang Knecht
 *
 */
public class LevelActivity extends Activity {
	
	//Player player=new Player();
	
	MyRenderer myRenderer;
	TextView textTime;
	TextView textPoints;
	
	int seconds;
	String timeString;
	
	int cash;
	int returnPoints;
	Sound coinSound;
	Sound coinSound2;
	
	SessionState sessionState;

	/**
	 * Creates the GLSurfaceView
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("Activity", "onCreate");
		
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	     requestWindowFeature(Window.FEATURE_NO_TITLE);
	     
		mGLSurfaceView = new MyOpenGLView(this);
        //mGLSurfaceView.setRenderer(new MyRenderer(player));
		myRenderer=new MyRenderer();
		mGLSurfaceView.setRenderer(myRenderer);
		setContentView(R.layout.l55_level);
		
		FrameLayout mFrameLayout = (FrameLayout) findViewById(R.id.l55_FrameLayout);
		mFrameLayout.addView(mGLSurfaceView,0);
		
		textPoints=(TextView)findViewById(R.id.l55_TextCash);
		
		textTime=(TextView)findViewById(R.id.l55_TextTime);
		
		seconds=120;
		cash=100;
		returnPoints=0;
		
		coinSound=new Sound();
		coinSound.create(R.raw.l00_gold01);
		
		coinSound2=new Sound();
		coinSound2.create(R.raw.l00_gold03);
		
		Timer timeUpdateTimer=new Timer();
		timeUpdateTimer.schedule(new TimerTask() {
			public void run() {
				seconds-=1;
				if (seconds<=0) {
					LevelActivity.this.finish();
				}
				int minutes=(int)seconds/60;
				timeString=Integer.toString(minutes)+":";
				if (seconds-minutes*60<10) {
					timeString+="0";
				}
				timeString+=Integer.toString(seconds-minutes*60);
				handleUIChanges.sendEmptyMessage(0);
			}
		}, 1000, 1000);
		
		sessionState = new SessionState();
		//we set the progress the user has made (must be between 0-100)
		sessionState.setProgress(0);
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, sessionState.asIntent());
	}
	
	public Handler handleUIChanges = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what==0) {	// update time
				textTime.setText(timeString);
			}
			if (msg.what==1) {	// update points
				returnPoints-=msg.arg1;
				sessionState.setProgress(Math.min(Math.max(0, returnPoints),100));
				setResult(Activity.RESULT_OK, sessionState.asIntent());
				
				cash+=msg.arg1;
				textPoints.setText(Integer.toString(cash));
				
				if (msg.arg1<0) {
					coinSound.start();
				} else if (msg.arg1>0) {
					coinSound2.start();
				}
			}
			if (msg.what==2) {
				LevelActivity.this.finish();
			}
		}
	};
	
	
	@Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
		
		Log.d("Activity", "onResume");
		
        mGLSurfaceView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        mGLSurfaceView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onStop() {
    	Log.d("Activity", "onStop");
    	super.onStop();
    }
   
    /**
     * {@inheritDoc}
     */
    public boolean onTouchEvent(MotionEvent me) {	
    	float x=MyRenderer.numTilesHorizontal/MyRenderer.resX*me.getX();
    	float y=MyRenderer.numTilesVertical/MyRenderer.resY*me.getY();
    	
		if (myRenderer.player!=null && myRenderer.ui!=null) {
			if (me.getAction()==MotionEvent.ACTION_DOWN || me.getAction()==MotionEvent.ACTION_MOVE) {
				myRenderer.player.moveLeft(false);
				myRenderer.player.moveRight(false);
				myRenderer.player.jump(false);
				
				// Jump
				/*if (x>myRenderer.ui.gap && x<myRenderer.ui.gap+myRenderer.ui.fieldSize &&
						y>myRenderer.ui.screenHeight-myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						y<myRenderer.ui.screenHeight-myRenderer.ui.gap) {
					myRenderer.player.jump(true);
				}
				
				// Left
				if (x>myRenderer.ui.screenWidth-2.0f*myRenderer.ui.gap-2.0f*myRenderer.ui.fieldSize &&
						x<myRenderer.ui.screenWidth-2.0f*myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						y>myRenderer.ui.screenHeight-myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						y<myRenderer.ui.screenHeight-myRenderer.ui.gap) {
					myRenderer.player.moveLeft(true);
				}
				
				// Right
				if (x>myRenderer.ui.screenWidth-myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						x<myRenderer.ui.screenWidth-myRenderer.ui.gap &&
						y>myRenderer.ui.screenHeight-myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						y<myRenderer.ui.screenHeight-myRenderer.ui.gap) {
					myRenderer.player.moveRight(true);
				}
				*/
				
				// Jump
				if (x>myRenderer.ui.screenWidth-2.0f*myRenderer.ui.gap-2.0f*myRenderer.ui.fieldSize &&
						x<myRenderer.ui.screenWidth-myRenderer.ui.gap &&
						y>myRenderer.ui.screenHeight-myRenderer.ui.gap*1.5f-myRenderer.ui.fieldSize*1.5f &&
						y<myRenderer.ui.screenHeight-myRenderer.ui.gap-myRenderer.ui.fieldSize) {
					myRenderer.player.jump(true);
				}
				
				// Left + Jump
				if (x>myRenderer.ui.screenWidth-2.0f*myRenderer.ui.gap-2.0f*myRenderer.ui.fieldSize &&
						x<myRenderer.ui.screenWidth-3.0f*myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						y>myRenderer.ui.screenHeight-myRenderer.ui.gap*1.5f-myRenderer.ui.fieldSize*1.5f &&
						y<myRenderer.ui.screenHeight-myRenderer.ui.gap) {
					myRenderer.player.moveLeft(true);
				}
				
				// Right + Jump
				if (x>myRenderer.ui.screenWidth-myRenderer.ui.fieldSize &&
						x<myRenderer.ui.screenWidth-myRenderer.ui.gap &&
						y>myRenderer.ui.screenHeight-myRenderer.ui.gap*1.5f-myRenderer.ui.fieldSize*1.5f &&
						y<myRenderer.ui.screenHeight-myRenderer.ui.gap) {
					myRenderer.player.moveRight(true);
				}
				
				// Left
				if (x>myRenderer.ui.screenWidth-2.0f*myRenderer.ui.gap-2.0f*myRenderer.ui.fieldSize &&
						x<myRenderer.ui.screenWidth-2.0f*myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						y>myRenderer.ui.screenHeight-myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						y<myRenderer.ui.screenHeight-myRenderer.ui.gap) {
					myRenderer.player.moveLeft(true);
				}
				
				// Right
				if (x>myRenderer.ui.screenWidth-myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						x<myRenderer.ui.screenWidth-myRenderer.ui.gap &&
						y>myRenderer.ui.screenHeight-myRenderer.ui.gap-myRenderer.ui.fieldSize &&
						y<myRenderer.ui.screenHeight-myRenderer.ui.gap) {
					myRenderer.player.moveRight(true);
				}
			}
			
			if (me.getAction()==MotionEvent.ACTION_CANCEL || me.getAction()==MotionEvent.ACTION_UP) {
				myRenderer.player.moveLeft(false);
				myRenderer.player.moveRight(false);
				myRenderer.player.jump(false);
			}
		}
    	
    	synchronized(this) {
	    	try {
				this.wait(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	return true;
	}
    
    /**
     * {@inheritDoc}
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (myRenderer.player!=null) {
	    	if (keyCode==KeyEvent.KEYCODE_A) {
	    		myRenderer.player.moveLeft(true);
	    		return true;
	    	}
	    	if (keyCode==KeyEvent.KEYCODE_D) {
	    		myRenderer.player.moveRight(true);
	    		return true;
	    	}
	    	if (keyCode==KeyEvent.KEYCODE_W) {
	    		myRenderer.player.jump(true);
	    		return true;
	    	}
    	}
		return super.onKeyDown(keyCode, event);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	if (keyCode==KeyEvent.KEYCODE_A) {
    		myRenderer.player.moveLeft(false);
    		return true;
    	}
    	if (keyCode==KeyEvent.KEYCODE_D) {
    		myRenderer.player.moveRight(false);
    		return true;
    	}
    	if (keyCode==KeyEvent.KEYCODE_W) {
    		myRenderer.player.jump(false);
    		return true;
    	}
    	return super.onKeyUp(keyCode, event);
    }
	
	private MyOpenGLView mGLSurfaceView;
}

package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.opengl.GLSurfaceView;
import android.os.Bundle;
//import android.view.Display;
//import android.view.GestureDetector;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
//import android.widget.LinearLayout;
import android.widget.TextView;
//import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
//import at.ac.tuwien.cg.cgmd.bifth2010.level13.MyRenderer;
//import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;

public class LevelActivity extends Activity {
	private LevelSurfaceView glv;
	//private LevelRenderer lr;
//	private int winWidth, winHeight;
	private TextView txtView;
	private static final String CLASS_TAG = LevelSurfaceView.class.getName();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        Display d = wm.getDefaultDisplay();
//        winWidth = (int)d.getWidth();
//        winHeight = (int)d.getHeight();
//        Log.d(CLASS_TAG, "started");
        glv = new LevelSurfaceView(this);
    	//	lr = new LevelRenderer(this);
    	//	glv.setRenderer(lr);
		setContentView(glv);
		
		//LinearLayout lay = new LinearLayout(this);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
		//addContentView(lay,params);
		txtView = new TextView(this);
		this.addContentView(txtView,params);
		txtView.setText("jemand mag dich.");
//    		Log.d(CLASS_TAG, "text written");
		return;
	}
	
	SessionState state;
	
    @Override
    public void onPause() {
        super.onPause();
        state = glv.pause();
        setResult(Activity.RESULT_OK, state.asIntent());
    }

    @Override
    public void onResume() {
        super.onResume();
        glv.resume();
    }
    
	 @Override
	    protected void onDestroy() {
	    	super.onDestroy();
	    	state = glv.getState();
	    	setResult(Activity.RESULT_OK, state.asIntent());
	    //	finish();
	    }
	
    ////on finish
	//setResult(Activity.RESULT_OK, s.asIntent());
	//finish();
}

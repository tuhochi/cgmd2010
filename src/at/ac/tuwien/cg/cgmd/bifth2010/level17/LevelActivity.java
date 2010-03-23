package at.ac.tuwien.cg.cgmd.bifth2010.level17;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLView;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;

/**
 * Wrapper activity demonstrating the use of {@link GLSurfaceView}, a view
 * that uses OpenGL drawing into a dedicated surface.
 */
public class LevelActivity extends Activity {
	
	private Vector2 mWindowSize;
	private static final int INSERT_ID = Menu.FIRST;
	private TextView mOutputText;
	private GLView mNormalModeView;
	private final Handler mHandler = new Handler();
	private NormalModeWorld mWorld;
	private List<String> mTasks = new ArrayList<String>();
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mWorld = new NormalModeWorld(this, mHandler);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        mWindowSize = new Vector2((float)d.getWidth(), (float)d.getHeight());
        
        
        
        mNormalModeView = new GLView(this, mWindowSize, mWorld);
        mGLSurfaceView = mNormalModeView;
        setContentView(mGLSurfaceView);
        
        mOutputText = new TextView(this);
        mOutputText.setText("");
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        addContentView(mOutputText, params);
        loadTasks("tasks.txt");
    }

    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mGLSurfaceView.onPause();
    }
    
    @Override
    public boolean onTouchEvent (MotionEvent event)
    {
    	if(event.getAction() == MotionEvent.ACTION_MOVE)
    	{
    		mNormalModeView.fingerMove(event.getX() / mWindowSize.x, event.getY() / mWindowSize.y);
    	}
    	else if(event.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		mNormalModeView.fingerDown(event.getX() / mWindowSize.x, event.getY() / mWindowSize.y);
    	}
    	else if(event.getAction() == MotionEvent.ACTION_UP)
    	{
    		mNormalModeView.fingerUp(event.getX() / mWindowSize.x, event.getY() / mWindowSize.y);
    	}
    	
    	return super.onTouchEvent(event);
    }
    
    public void bottleStopped()
    {
    	int rand = (int)Math.floor(Math.random() * mTasks.size());
    	mOutputText.setText(mTasks.get(rand));
    }
    
    public void loadTasks(String fileName)
    {
    	InputStream is;
		try {
			is = getAssets().open(fileName);
	        is.available();
	        InputStreamReader isr = new InputStreamReader(is);            
	        BufferedReader textReader = new BufferedReader(isr);
	        String line = textReader.readLine();
	        while (line != null)
	        {
	        	mTasks.add(line);
	        	line = textReader.readLine();
	        }
            textReader.close();
            isr.close();
            is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

    private GLSurfaceView mGLSurfaceView;
}

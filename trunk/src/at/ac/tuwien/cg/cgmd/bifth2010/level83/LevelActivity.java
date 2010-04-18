package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import java.util.Timer;
import java.util.TimerTask;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

public class LevelActivity extends Activity {
	
	GLSurfaceView surfaceView;
	MyRenderer renderer;
	TextView fps;
	String fpsString = "";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        surfaceView = new GLSurfaceView(this);	//GLSurfaceView erzeugen
        
        renderer = new MyRenderer(this);			//eigenen Renderer binden
        surfaceView.setRenderer(renderer);
        surfaceView.setOnTouchListener(renderer);
        
        FrameLayout fl = new FrameLayout(this);	//SurfaceView anzeigen
        fl.addView(surfaceView);
        
        fps = new TextView(this);
        fps.setText("FPS: ");
        fps.setTextColor(0xFF4DA3B7);
       
        fl.addView(fps);
        
        this.setContentView(fl);		
        
        Timer fpsUpdate = new Timer();
        
        fpsUpdate.schedule(new TimerTask() {
			
			@Override
			public void run() {
				fpsString = "FPS: "+ renderer.fps;
				fpsUpdateHandler.sendEmptyMessage(0);
			}
		}, 1000, 1000);
        	
    }
    
    private Handler fpsUpdateHandler = new Handler(){
    	
    	@Override
    	public void handleMessage(Message msg){
    		super.handleMessage(msg);
    		fps.setText(fpsString);
    	}
    };
    
    @Override
    protected void onPause() {
    	super.onPause();
    	surfaceView.onPause();
    	renderer.onPause();
    	
    	Log.d("Main","OnPause");
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	renderer.onStop();
    	
    	Log.d("Main","OnStop");
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	//MyTextureManager.singleton.reload(this, gl);
    	surfaceView.onResume();
    	renderer.onResume();
    	Log.d("Main","OnResume");
    }
}
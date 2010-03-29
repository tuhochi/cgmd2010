package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class LevelActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	     requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mGLSurfaceView = new MyOpenGLView(this);
        mGLSurfaceView.setRenderer(new Renderer());        
        setContentView(mGLSurfaceView);
	}
	
	private MyOpenGLView mGLSurfaceView;
}

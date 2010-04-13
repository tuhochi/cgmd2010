package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class LevelActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView view = new GLSurfaceView(this);
   		view.setRenderer(new LevelRenderer());
   		setContentView(view);
    }

    @Override
    public void onPause() {
        super.onPause();
        //mGLView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mGLView.onResume();
    }

    //private GLView mGLView;

}

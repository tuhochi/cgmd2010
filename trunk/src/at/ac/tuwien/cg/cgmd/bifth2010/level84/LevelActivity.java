package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class LevelActivity extends Activity {


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GLSurfaceView view = new GLSurfaceView(this);
		view.setRenderer(new L84RenderManager());
		setContentView(view);
	}
		
}

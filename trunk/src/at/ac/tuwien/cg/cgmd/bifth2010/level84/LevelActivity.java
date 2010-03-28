package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.FrameLayout;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class LevelActivity extends Activity {


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setContentView(R.layout.l84_level);
		//FrameLayout mFrameLayout = (FrameLayout) findViewById(R.id.l84_FrameLayout01);

		//GLSurfaceView openglview = (GLSurfaceView) findViewById(R.id.l84_openglview); 
		GLSurfaceView openglview = new GLSurfaceView(this);
		openglview.setRenderer(new L84RenderManager());
		setContentView(openglview);
		
	}
		
}

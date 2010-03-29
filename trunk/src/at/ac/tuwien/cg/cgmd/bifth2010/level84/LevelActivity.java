package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class LevelActivity extends Activity {


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		GLSurfaceView openglview = new GLSurfaceView(this);
		openglview.setRenderer(new L84RenderManager());
		//addContentView(openglview,null);
		setContentView(openglview);
//		setContentView(R.layout.l84_level);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//TODO: surfaceview on pause
	}

	@Override
	protected void onResume() {
		super.onResume();
		//TODO: surfaceview on resume
	}
		
}

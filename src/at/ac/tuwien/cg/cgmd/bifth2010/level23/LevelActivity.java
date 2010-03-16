package at.ac.tuwien.cg.cgmd.bifth2010.level23;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.Renderer;

public class LevelActivity extends Activity {

	private GLSurfaceView mGLSurfaceView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Renderer renderer = new Renderer(this);
        setContentView(renderer);

	}
}
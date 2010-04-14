package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class LevelSurfaceView extends GLSurfaceView {

	public LevelSurfaceView(Context context) {
		super(context);
		mRenderer = new LevelRenderer(context);
        setRenderer(mRenderer);

	}
	LevelRenderer mRenderer;
}

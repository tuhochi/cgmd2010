package at.ac.tuwien.cg.cgmd.bifth2010.framework;


import android.content.Context;
import android.opengl.GLSurfaceView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class MapView extends GLSurfaceView {

	public MapView(Context context) {
		super(context);
		
		//Scene scene = new Scene();
		//Rectangle map = new Rectangle(1, 3);	
		//Circle iconLevel01 = new Circle(0.25f, 100);
		//iconLevel01.setTexture(gl, getResources(), R.drawable.l00_map_512);
		mMapRenderer = new MapRenderer(getResources());
		
		setRenderer(mMapRenderer);	
	}
	
	public float getFps() {
		return mMapRenderer.getFps();
	}
	
	public float getFilteredFps() {
		return mMapRenderer.getFilteredFps();
	}
	
	
	MapRenderer mMapRenderer;
}

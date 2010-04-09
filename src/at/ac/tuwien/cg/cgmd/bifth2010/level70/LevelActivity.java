package at.ac.tuwien.cg.cgmd.bifth2010.level70;

import android.app.Activity;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer.RendererView;

public class LevelActivity extends Activity {

	RendererView rendererView; //< Renderer view
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
   		rendererView = new RendererView(this);
   		setContentView(rendererView);
	}
}
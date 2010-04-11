package at.ac.tuwien.cg.cgmd.bifth2010.level70;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.NormalModeWorld;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLView;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer.RendererView;

public class LevelActivity extends Activity {

	RendererView rendererView; //< Renderer view
		
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		//set fullscreen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       
        //get display
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        
        //create
   		rendererView = new RendererView(this,d.getWidth(),d.getHeight());
   		
   		//set
   		setContentView(rendererView);
	}
}
package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import android.app.Activity;

//import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

import android.content.Context;

/**
 * This activity demonstrates the basic interaction with the framework. When the Finish button is pressed the a result is set and the activity is finished. 
 * @author Peter
 *
 */
public class LevelActivity extends Activity
{
	private GameView gameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		gameView = new GameView(this);
		setContentView( gameView );		
	}
	

    @Override
	protected void onPause() {
		super.onPause();
		gameView.onPause();		
	}

	@Override
	protected void onResume() {
		super.onResume();
		gameView.onResume();
	}
}

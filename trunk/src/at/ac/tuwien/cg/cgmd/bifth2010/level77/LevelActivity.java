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
 * Entry point for the Level 77 BunnyBlock
 * @author Gerd Katzenbeisser
 *
 */
public class LevelActivity extends Activity
{
	private GameView gameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setResult(RESULT_CANCELED);
		
		Callback<Integer> gameEnded = new Callback<Integer>()
		{
			
			@Override
			public void onSucces(Integer result)
			{
				SessionState s = new SessionState();
				s.setProgress(result);				
				setResult(RESULT_OK, s.asIntent());	
				LevelActivity.this.finish();
			}
			
			@Override
			public void onFailure(Throwable caught)
			{
				caught.printStackTrace();
			}
		};
		
		gameView = new GameView(this, gameEnded);
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

package at.ac.tuwien.cg.cgmd.bifth2010.level22;

import android.app.Activity;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering.SpamRenderer;

public class LevelActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		SpamRenderer renderer = new SpamRenderer( this );
		setContentView( renderer );
		
		//GameLogic.init( this, 1000, 100 );
	}
}

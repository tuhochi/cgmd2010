package at.ac.tuwien.cg.cgmd.bifth2010.level22;

import android.app.Activity;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * The help activity of level 22. Starts up the description of the level.
 * 
 * @author Sulix
 *
 */
public class HelpActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate( Bundle passedState )
	{
		
		super.onCreate( passedState );
        setContentView( R.layout.l22_help );
		
	}
}
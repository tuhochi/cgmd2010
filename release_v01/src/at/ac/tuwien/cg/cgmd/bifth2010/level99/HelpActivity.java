package at.ac.tuwien.cg.cgmd.bifth2010.level99;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class HelpActivity extends Activity {

	public void onCreate( Bundle passedState )
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate( passedState );
        setContentView( R.layout.l99_help );
		
	}
}
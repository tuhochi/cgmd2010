package at.ac.tuwien.cg.cgmd.bifth2010.level66;

import android.app.Activity;
import android.os.Bundle;

public class LevelActivity extends Activity {

	private Level66View _level66View;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		_level66View = new Level66View(this);
        setContentView(_level66View);
	}
	
}

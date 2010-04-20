package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {

	private static final String LOG_TAG = GameActivity.class.getSimpleName();
    private GameView _gameView;
    
    private Level _level;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
        _gameView = new GameView(this);
        setContentView(_gameView);
        
       // _level = new Level();
        //_level.start();
        
    }
    
    
    @Override
	protected void onPause() {
		super.onPause();
		_gameView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		_gameView.onResume();
	}

	
}

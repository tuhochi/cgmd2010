package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {

	private static final String LOG_TAG = GameActivity.class.getSimpleName();
    private GameView _gameView;
    public Level _level;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set to fullscreen and no bars
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
    	// create level
    	_level = new Level(480, 320);
    	
        _gameView = new GameView(this);
        setContentView(_gameView);
        
      
        _level.start();
         
        
    }
    
    public Level getLevel() {
    	return _level;
    }
    
    
    @Override
    protected void onResume() {
		//_level.pause(false);
		//_level.resume();
        _gameView.onResume();
		super.onResume();
    }

    @Override
    protected void onPause() {
        _gameView.onPause();
		super.onPause();
    }
    
    @Override
	protected void onStart() {
    	_gameView.onStart();
		super.onStart();
	}

	@Override
	protected void onStop() {
		_gameView.onStop();
		super.onStop();
	}

}

package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import android.app.Activity;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class GameActivity extends Activity {

	private static final String LOG_TAG = GameActivity.class.getSimpleName();
    private GameView _gameView;
    public Level _level;
	private TextView _textTimeLeft;
	private TextView _textTreasureLeft;
	private int _result = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set to fullscreen and no bars
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	this.setRequestedOrientation(0); 
    	
    	
    	// create level
    	_level = new Level(480, 320);
    	
        _gameView = new GameView(this);
        setContentView(_gameView);
    	
    	LinearLayout llayout = new LinearLayout(this);
        llayout.setGravity(Gravity.TOP | Gravity.LEFT);
        llayout.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(llayout, params);
       
    	
        _textTimeLeft = new TextView(this);
        _textTimeLeft.setText("2:00");
        _textTimeLeft.setTextSize(25);
        _textTimeLeft.setGravity(Gravity.LEFT);
        _textTimeLeft.setTextColor(Color.BLACK);  
         
         
        _textTreasureLeft = new TextView(this);
        _textTreasureLeft.setText("0");
        _textTreasureLeft.setTextSize(25);
        _textTreasureLeft.setGravity(Gravity.CENTER);
        _textTreasureLeft.setTextColor(Color.BLACK);
         
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams.setMargins(10, 10, 10, 10);
        llayout.addView(_textTimeLeft, layoutParams);
        
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams2.setMargins(10, 10, 10, 10);
        llayout.addView(_textTreasureLeft, layoutParams2); 


    	
        _level.start();
    }
    
    public Level getLevel() {
    	return _level;
    }
    
    
    @Override
    protected void onResume() {
		_level.pause(false);
        _gameView.onResume();
		super.onResume();
    }

    @Override
    protected void onPause() {
    	_level.pause(true);
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
	
	@Override
	public void finish() {
		SessionState s = new SessionState();
		//we set the progress the user has made (must be between 0-100)
		s.setProgress(Math.min(Math.max(_result, 0), 100));
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
		_level._isActive = false;
		super.finish();
	}
	
	public void setTextTimeLeft(float f) {
		
		float minutes = f % 60;
		
		String s = Float.toString(minutes)+":"+Float.toString(Math.round(f-(minutes*60.f)));
		_textTimeLeft.setText("afwf");
		
		if (f <= 0.0f) {
			finish();
		} 
	}
	
	public void setTextTreasureSpent(float f) {
		_result = (int)f;
		_textTreasureLeft.setText(Integer.toString(_result));
		
		if (_result >= 100)
		{
			finish();
		}
	}

}

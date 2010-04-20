package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.gamelogic.GameLogic;

public class GameActivity extends Activity {

	private static final String LOG_TAG = GameActivity.class.getSimpleName();
    private GameView _gameView;
    public Level _level;
	private TextView _textTimeLeft;
	private TextView _textTreasureLeft;
	private int _result = 0;
	
	private Vector2 _displayResolution;
    
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set to fullscreen and no bars
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	this.setRequestedOrientation(0); 
    	
       	WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        _displayResolution = new Vector2((float)display.getWidth(), (float)display.getHeight());
        
    	
    	// create level
    	_level = new Level(_displayResolution.x, _displayResolution.y);
    	
        _gameView = new GameView(this, _displayResolution);
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
    
    @Override
    protected void onResume() {
		_level.resume_level();
        _gameView.onResume();
		super.onResume();
    }

    @Override
    protected void onPause() {
    	_level.pause_level();
        _gameView.onPause();
		super.onPause();
    }
    
    @Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
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
	
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		
		GameLogic.makePersistentState( outState );
		outState.putBoolean( "isLoadable", true );
		
		super.onSaveInstanceState(outState);
	}
	
    /**
     * returns Level Object
     * @return
     */
    public Level getLevel() {
    	return _level;
    }
	
    /**
     * updates text to show how much time in the level is left
     * @param f
     */
	public void setTextTimeLeft(float f) {
		
		float minutes = f % 60;
		
		String s = Float.toString(minutes)+":"+Float.toString(Math.round(f-(minutes*60.f)));
		_textTimeLeft.setText("afwf");
		
		if (f <= 0.0f) {
			finish();
		} 
	}
	
	
	/**
	 * updates treasure grabbed text
	 * @param f
	 */
	public void setTextTreasureGrabbed(float f) {
		_result = (int)f;
		_textTreasureLeft.setText(Integer.toString(_result));
		
		if (_result >= 100)
		{
			finish();
		}
	}

	/**
	 * sets result (grabbed treasure) 
	 * @param f
	 */
	public void setResult(float f) {
		_result = (int)f;
	}
}

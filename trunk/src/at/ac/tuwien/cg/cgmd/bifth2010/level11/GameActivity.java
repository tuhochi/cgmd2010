package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import java.util.Iterator;
import java.util.LinkedList;

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
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class GameActivity extends Activity {

	private static final String LOG_TAG = GameActivity.class.getSimpleName();
    private GameView _gameView;
    public Level _level;
	private TextView _textTimeLeft;
	private TextView _textTreasureLeft;
	private int _result = 0;
	private int textUpdateTime = 100;
	private Vector2 _displayResolution;

	private Handler handler = new Handler();

	private Runnable updateTimeTask = new Runnable() {
	public void run() {
		updateText();
		handler.postDelayed(this, textUpdateTime);
	}
	};

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		//System.out.println("Create");
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
        _textTimeLeft.setText("");
        _textTimeLeft.setTextSize(25);
        _textTimeLeft.setGravity(Gravity.LEFT);
        _textTimeLeft.setTextColor(Color.BLACK);  
         
         
        _textTreasureLeft = new TextView(this);
        _textTreasureLeft.setText("");
        _textTreasureLeft.setTextSize(25);
        _textTreasureLeft.setGravity(Gravity.CENTER);
        _textTreasureLeft.setTextColor(Color.BLACK);
        
      /*  _textIntro = new TextView(this);
        _textIntro.setText("");
        _textIntro.setTextSize(25);
        _textIntro.setGravity(Gravity.CENTER);
        _textIntro.setTextColor(Color.BLACK);*/
         
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams.setMargins(10, 10, 10, 10);
        llayout.addView(_textTimeLeft, layoutParams);
        
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams2.setMargins(10, 10, 10, 10);
        llayout.addView(_textTreasureLeft, layoutParams2); 
        
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams3.setMargins(10, 10, 10, 10);
        //llayout.addView(_textIntro, layoutParams3);
        /*if(savedInstanceState.containsKey("isLoadable"))
        	this.onRestoreInstanceState(savedInstanceState);*/
        //_level.start();
        handler.removeCallbacks(updateTimeTask);
        handler.postDelayed(updateTimeTask, textUpdateTime);
        //_level.start();
        //toast.show();
        if(savedInstanceState == null){
        	new IntroDialog(this).show();
        }else{
        	_level.start();
        }
        //IntroDialog introDialog = new IntroDialog(this);
        //introDialog.show();
    }
    
    @Override
    protected void onResume() {
		//System.out.println("Resume");
		_level.resume_level();
        _gameView.onResume();
		super.onResume();
		handler.removeCallbacks(updateTimeTask);
		handler.postDelayed(updateTimeTask, textUpdateTime);
    }

    @Override
    protected void onPause() {
		//System.out.println("Pause");
    	_level.pause_level();
        _gameView.onPause();
		super.onPause();
    }
    
    @Override
	protected void onStart() {
		//System.out.println("Start");
		super.onStart();
	}

	@Override
	protected void onStop() {
		//System.out.println("Stop");
		super.onStop();
		handler.removeCallbacks(updateTimeTask);
	}

	@Override
	protected void onDestroy() {
		//System.out.println("Destroy");
		super.onDestroy();
		if ( handler != null )
			handler.removeCallbacks(updateTimeTask);
			handler = null; 
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
		//System.out.println("Save");
		Timing timing = this._level.getTiming();
		timing.pause();
		outState.putFloat("startTimeStamp", timing.getStartTime());
		outState.putFloat("pauseTimeStamp", timing.getPauseTimeStamp());
		outState.putFloat("pauseTime", timing.getPausedTime());
		outState.putFloat("grabbedTreasureValueOfDeletedTreasures", this._level.getGrabbedTreasureValueOfDeletedTreasures());
		LinkedList<Pedestrian>pl = _level.getPedestrianList();
		outState.putInt("pedestrianListSize", pl.size());
		for(int i = 0; i < pl.size(); i++){
			outState.putFloat("pedestrian"+i+"PosX", pl.get(i).getPosition().x);
			outState.putFloat("pedestrian"+i+"PosY", pl.get(i).getPosition().y);
		}
		
		LinkedList<Treasure>tl = _level.getTreasureList();
		outState.putInt("pedestrianListSize", tl.size());
		for(int i = 0; i < tl.size(); i++){
			outState.putFloat("treasure"+i+"PosX", tl.get(i).getPosition().x);
			outState.putFloat("treasure"+i+"PosY", tl.get(i).getPosition().y);
			outState.putFloat("treasure"+i+"Value", tl.get(i).getValue());
			outState.putFloat("treasure"+i+"StartingValue", tl.get(i).getStartingValue());
		}
		outState.putBoolean("isLoadable", true);
		//super.onSaveInstanceState(outState);
		//System.out.println(outState);
	}
    @Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		//System.out.println("Restore");
		//super.onRestoreInstanceState(savedInstanceState);
		
		Timing timing = new Timing();
		timing.setStartTime(savedInstanceState.getFloat("startTimeStamp"));
		timing.setPauseTimeStamp(savedInstanceState.getFloat("pauseTimeStamp"));
		timing.setPausedTime(savedInstanceState.getFloat("pauseTime"));
		this._level.setTiming(timing);
		this._level.setGrabbedTreasureValueOfDeletedTreasures(savedInstanceState.getFloat("grabbedTreasureValueOfDeletedTreasures"));
		LinkedList<Pedestrian>pl = new LinkedList<Pedestrian>();
		int plSize = savedInstanceState.getInt("pedestrianListSize");
		for(int i = 0; i < plSize; i++){
			Pedestrian p = new Pedestrian(this._level.getGL(), this);
			Vector2 pos = new Vector2();
			pos.x = savedInstanceState.getFloat("pedestrian"+i+"PosX");
			pos.y = savedInstanceState.getFloat("pedestrian"+i+"PosY");
			p.setPosition(pos);
			pl.add(p);
		}
		this._level.setPedestrianList(pl);
		
		LinkedList<Treasure>tl = new LinkedList<Treasure>();
		int tlSize = savedInstanceState.getInt("pedestrianListSize");
		for(int i = 0; i < tlSize; i++){
			Vector2 pos = new Vector2();
			pos.x = savedInstanceState.getFloat("treasure"+i+"PosX");
			pos.y = savedInstanceState.getFloat("treasure"+i+"PosY");
			float value = savedInstanceState.getFloat("treasure"+i+"Value");
			float startingValue = savedInstanceState.getFloat("treasure"+i+"StartingValue");
			tl.add(new Treasure(value, startingValue, pos));
		}
		this._level.setTreasureList(tl);
		//System.out.println(savedInstanceState);
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
	/*public void setTextTimeLeft(float f) {
		
		float minutes = f % 60;
		
		String s = Float.toString(minutes)+":"+Float.toString(Math.round(f-(minutes*60.f)));
		_textTimeLeft.setText("test");
		
		if (f <= 0.0f) {
			finish();
		} 
	}*/
	
	
	/**
	 * updates treasure grabbed text
	 * @param f
	 */
	/*public void setTextTreasureGrabbed(float f) {
		_textTreasureLeft.setText(Float.toString(f));
		
		_result+=f;
		if (_result >= 100)
		{
			_result = 100;
		}
	}*/

	/**
	 * sets result (grabbed treasure) 
	 * @param f
	 */
	public void setResult(float f) {
		_result = Math.min(100, (int)f);
	}
	public void updateText(){
		if(_level._isStarted){
			if(!_level._isFinished){
				_textTreasureLeft.setText(Integer.toString(Math.round(_level.getGrabbedTreasureValue())));
				_textTimeLeft.setText(Integer.toString(Math.round(_level.getRemainigTime())));
			}
		}
	}
}

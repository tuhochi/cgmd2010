package at.ac.tuwien.cg.cgmd.bifth2010.level88;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.game.Game;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;

/**
 * Class for the general level activity of level 88
 * @author Asperger, Radax
 */
public class LevelActivity extends Activity{
	public static final String TAG = "LevelActivity"; 
	
	private final Handler handler = new Handler();
	private GLSurfaceView glSurfaceView;
	private GLView normalModeView;
	private Game game;
	private Vector2 windowSize;
	private TextView goldText;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	 	Window window = getWindow();
	 	window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        windowSize = new Vector2((float)d.getWidth(), (float)d.getHeight());
	 	
	 	game = new Game(this, handler);

	 	normalModeView = new GLView(this, game);
	 	glSurfaceView = normalModeView; 
        setContentView(glSurfaceView);
        
        LinearLayout llayout = new LinearLayout(this);
        llayout.setGravity(Gravity.TOP | Gravity.LEFT);
        llayout.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(llayout, params);
        
        goldText = new TextView(this);
        goldText.setTextSize(25);
        goldText.setGravity(Gravity.CENTER);
        goldText.setTextColor(Color.WHITE);  
        goldText.setBackgroundResource(R.drawable.l88_text);
        
        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        llparams.weight = 0;
        llparams.setMargins(10, 10, 10, 10);
        llayout.addView(goldText, llparams);
        
        glSurfaceView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				game.touchEvent(new Vector2(event.getX() / windowSize.x, event.getY() / windowSize.y));
				return true;
			}
        });

        updateTexts();
        
       //the SessionState is a convenience class to set a result
		SessionState s = new SessionState();
		//we set the progress the user has made (must be between 0-100)
		s.setProgress(100-game.gold);
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
	}
	
	
	/**
	 * End of the level 
	 */
	public void endLevel() {
		//the SessionState is a convenience class to set a result
		SessionState s = new SessionState();
		//we set the progress the user has made (must be between 0-100)
		s.setProgress(100-game.gold);
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
		LevelActivity.this.finish();
	}
	
	
	/**
	 * Update the screen texts 
	 */
	public void updateTexts() {
   		goldText.setText(" " + getString(getResources().getIdentifier("l88_gold_text", "string", "at.ac.tuwien.cg.cgmd.bifth2010")) + game.gold + " ");
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	public void onPause() {
		super.onPause(); 
		Log.d(TAG, "onPause()");
		glSurfaceView.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	public void onResume() {
		super.onResume(); 
		Log.d(TAG, "onResume()");
		glSurfaceView.onResume();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	protected void onStart()
	{
		super.onStart();
		Log.v(TAG, "onStart()");
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.v(TAG, "onRestart()");
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Log.v(TAG, "onStop()");
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.v(TAG, "onDestroy()");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		//Save the position
		outState.putIntArray("Position", game.bunny.getPosition());
		//Save the direction of the movement
		outState.putString("Direction", game.bunny.moveStatus.toString());
		//waiting time of the bunny
		outState.putFloat("Waiting", game.bunny.getWaitingTime());
		//Save the amount of gold left
		outState.putInt("Gold", game.getGold());
		
		//Save the number of police
		outState.putInt("CntPolice", game.getCntPolice());
		//Iterate over the number of police
		for(int i = 0; i < game.getCntPolice(); i++){
			String temp = new String(i + "Position");
			//Save the position of the police
			outState.putIntArray(temp, game.police.get(i).getPosition());
			temp = new String(i + "Time");
			//Save the waiting time
			outState.putFloat(temp, game.police.get(i).getWaitingTime());
			temp = new String(i + "Move");
			//Save the movement
			outState.putString(temp, game.police.get(i).moveStatus.toString());
		}
		
		//Save the number of stashes
		outState.putInt("CntStash", game.getCntStash());
		for(int i = 0; i < game.getCntStash(); i++){
			String temp = new String(i + "STime");
			outState.putFloat(temp, game.stashes.get(i).getHideTime());
		}
		
		super.onSaveInstanceState(outState);
		Log.v(TAG,"onSaveInstanceState(" + outState + ")");
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		//Restore the position
		int[] pos = savedInstanceState.getIntArray("Position");
		game.bunny.setPosition(pos[0], pos[1]);
		//Restore the direction
		String move = savedInstanceState.getString("Direction");
		game.bunny.setMovement(move);
		//Restore the waiting time of the bunny
		game.bunny.setWaitingTime(savedInstanceState.getFloat("Waiting"));
		//Restore the amount of gold left
		game.setGold(savedInstanceState.getInt("Gold"));
		
		//Restore the police
		int cntpolice = savedInstanceState.getInt("CntPolice");
		//Iterate over the number of police
		for(int i = 0; i < cntpolice; i++){
			String temp = new String(i + "Position");
			pos = savedInstanceState.getIntArray(temp);
			game.police.get(i).setPosition(pos[0], pos[1]);
			game.map.movePolice(pos[0], pos[1], pos[0], pos[1]);
			temp = new String(i + "Time");
			game.police.get(i).setWaitingTime(savedInstanceState.getFloat(temp));
			temp = new String(i + "Move");
			game.police.get(i).setMovement(savedInstanceState.getString(temp));
		}
		
		//Restore the stashes
		int cntstash = savedInstanceState.getInt("CntStash");
		for(int i = 0; i < cntstash; i++){
			String temp = new String(i + "STime");
			game.stashes.get(i).setHideTime(savedInstanceState.getFloat(temp));
		}

		Log.v(TAG,"onRestoreInstanceState(" + savedInstanceState + ")");
	}
    

}

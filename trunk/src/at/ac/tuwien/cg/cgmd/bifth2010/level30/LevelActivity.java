package at.ac.tuwien.cg.cgmd.bifth2010.level30;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level30.GameWorld.TransactionType;
import at.ac.tuwien.cg.cgmd.bifth2010.level30.math.Vector2;
import java.util.TimerTask;
import java.util.Timer;


//i shamelessly use the vector math and opengl classes from group 17
//


/*
 * Implements the activity for level 30
 */
public class LevelActivity extends Activity implements OnClickListener {

	//gui specific variables
	private Vector2 windowSize;
	private ViewGL view;
	private GameWorld gameWorld;	
	private TextView uiScoreText;
	private TextView uiBlinkingText;
	private TextView uiInstructionText;
	private Button[] uiButtonBuy;
	private int buttonWidth;
	Bundle bundle = null;
	
	//current money
	private int money;
	
	//for background music
	private MediaPlayer mp;
	

	/*
	 * Handler for callbacks.
	 */
	private final Handler handler = new Handler();
	
	
	/* Create the activity, initialize UI and gameworld.
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
   
		bundle = savedInstanceState;
		
		//Log.d("l30","onCreate");
        
        //play sounds?
		Intent callingIntent = getIntent();
		SessionState s = new SessionState(callingIntent.getExtras());
        gameWorld = new GameWorld(this, handler, savedInstanceState, s.isMusicAndSoundOn());
        
        //create windows
	    super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        windowSize = new Vector2((float)d.getWidth(), (float)d.getHeight());
        
        //add ui elements
        buttonWidth = d.getWidth()/4;
        view = new ViewGL(this, windowSize, gameWorld);
        setContentView(view);
        
        //layout for texts
        LinearLayout topLayout = new LinearLayout(this);
        topLayout.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        topLayout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(topLayout, params);
           
        uiScoreText = new TextView(this);
        uiScoreText.setText("1000000$");
        uiScoreText.setTextSize(29);
        uiScoreText.setGravity(Gravity.CENTER);
        uiScoreText.setTextColor(Color.BLACK);
        
        uiInstructionText = new TextView(this);
        uiInstructionText.setText(R.string.l30_intro);
        uiInstructionText.setTextSize(35);
        uiInstructionText.setPadding(10, 80, 80, 10);
        uiInstructionText.setGravity(Gravity.RIGHT);
        uiInstructionText.setTextColor(Color.BLACK);
        
        topLayout.addView(uiScoreText);       
        topLayout.addView(uiInstructionText);
        
        //layout for blinking big text
        LinearLayout topLayout2 = new LinearLayout(this);
        topLayout2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        topLayout2.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(topLayout2, params2);
        
        uiBlinkingText = new TextView(this);
        uiBlinkingText.setText("");
        uiBlinkingText.setTextSize(100);
        uiBlinkingText.setGravity(Gravity.CENTER);        
        uiBlinkingText.setTextColor(Color.RED);
        uiBlinkingText.setVisibility(View.INVISIBLE);
        
        topLayout2.addView(uiBlinkingText);        
        
        //create buttons&layouts for them
        uiButtonBuy = new Button[4];
        
        LinearLayout bottomLayout = new LinearLayout(this);
        bottomLayout.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        bottomLayout.setOrientation(LinearLayout.VERTICAL);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(bottomLayout, params);
        
        LinearLayout buttonLayoutBuy = new LinearLayout(this);
        buttonLayoutBuy.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        buttonLayoutBuy.setOrientation(LinearLayout.HORIZONTAL);
        bottomLayout.addView(buttonLayoutBuy);
        
        //add "buy" buttons
        for (int i=0; i<4; i++)
        {
        	uiButtonBuy[i] = new Button(this);
        	uiButtonBuy[i].setText(R.string.l30_buy);
        	uiButtonBuy[i].setTextSize(25);
        	uiButtonBuy[i].setGravity(Gravity.CENTER);
        	uiButtonBuy[i].setTextColor(Color.BLACK);
        	uiButtonBuy[i].setWidth(buttonWidth);  	
        	buttonLayoutBuy.addView(uiButtonBuy[i]);
        	uiButtonBuy[i].setTag((Integer)i);       	
        	uiButtonBuy[i].setOnClickListener(this);
        	uiButtonBuy[i].setVisibility(View.INVISIBLE);
        }
            
        uiButtonBuy[0].setVisibility(View.VISIBLE);
    		
        //init game state
		s.setProgress(0);		
		setResult(Activity.RESULT_OK, s.asIntent());
		money = 1000000;
				
		gameWorld.start();
		
		//start music
	    mp = MediaPlayer.create(this, R.raw.l00_menu);
	    mp.setLooping(true);
	    setMusicVolume(0.8f);
	    playMusic();
	    
	}
	
	/* "buy/sell" Button was clicked
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@SuppressWarnings("unchecked")
	public void onClick(View v)
	{		
		if (v instanceof Button)
		{		
			Button b = (Button)v;
		
			//tag of button stores graph number
			if (b.getTag() instanceof  Integer)
			{
				int num = (Integer)v.getTag() ;
				
				String buy = (String)getString(R.string.l30_buy);
				String sell = (String)getString(R.string.l30_sell);

				
				if (b.getText()==buy)
				{				
					//inform gameworld of transaction
					gameWorld.StockMarketTransaktion(num, TransactionType.BUY);
		
					//change button type
					b.setTextColor(Color.MAGENTA);
					b.setText(R.string.l30_sell);
					b.setWidth(buttonWidth);
				}
				else if (b.getText()==sell)
				{				
					gameWorld.StockMarketTransaktion(num, TransactionType.SELL);
					b.setTextColor(Color.BLACK);
					b.setText(R.string.l30_buy);
					b.setWidth(buttonWidth);
				}
			}
		}
	

	}
	

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
	    super.onResume();
	    view.onResume();
	    playMusic();
	    //Log.d("l30","onResume");
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		StopBlinkingText();
	    super.onPause();
	    view.onPause();
	    pauseMusic();
	    //Log.d("l30","onPause");
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		view.onStart();
		playMusic();
		//Log.d("l30","onStart");
	}
	

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		StopBlinkingText();
		pauseMusic();
		
		view.onStop();
		super.onStop();
		//Log.d("l30","onStop");
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		//do nothing for now. maybe later camera control
		
		/*if(event.getAction() == MotionEvent.ACTION_MOVE)
		{
			
		}
		else if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			
		}
		else if(event.getAction() == MotionEvent.ACTION_UP)
		{
			
		}*/
		
		return super.onTouchEvent(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//Log.d("l30","onSaveInstanceState");
	}
	
	@Override
	protected void  onRestoreInstanceState(Bundle state)
	{		
		onCreate(state);
		//Log.d("l30","onRestoreInstanceState");
	}
	
	@Override
	protected void onDestroy() {
		releaseMusic();		
		super.onDestroy();
		//Log.d("l30","onDestroy");
	}
	
	
	/*
	 * Called from gameworld through handler. Set current money.
	 */
    public void playerMoneyChanged(float _money)
    {
    	money = (int)_money;
    	uiScoreText.setText(Integer.toString(money)+"$"); 
    	
    	if (money<0)
    	{
    		uiScoreText.setText(R.string.l30_finished);
    	}
    }

    /*
     * Delayed finish
     */
    private Runnable delayedFinish = new Runnable() {
 	   public void run() { 	      
 			pauseMusic();
 			finish();
 			//Log.d("l30","finish");
 			}
 	   };
 	
	/*
	 * Quit the activity
	 */
    @Override
	public void finish() {
    		pauseMusic();
 			super.finish();
	}
    
	/*
	 * Called from gameworld through handler. Initiate ending
	 */
	public void prepareFinish() {
    	blinkingTextQuitMode = 1;    	
    	StopBlinkingText();
    	pauseMusic();
    	
    	uiBlinkingText.setText(R.string.l30_gameover);
    	uiBlinkingText.setTextSize(50);		
    	updateProgressResult();	
		
    	if (money>0.0f) //game over? display gameover screen longer
    		handler.postDelayed(delayedFinish, 4000);
    	else
    		handler.postDelayed(delayedFinish, 0);
	}
	
	/*
	 * Set the session state
	 */
    private void updateProgressResult()
    {
    	
		//the SessionState is a convenience class to set a result
		SessionState s = new SessionState(bundle);
		//we set the progress the user has made (must be between 0-100)
		s.setProgress((int)Math.min(Math.max( (1000000.0f - money)/10000.0f, 0.0f), 100.0f));
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
    }
    
    //handle blinking texts
    private boolean blinkingTextShouldQuit = false;
    private int blinkingTextQuitMode = 0;
    private Runnable blinkingTextRunnable = new Runnable() {
    	   public void run() {
    	       
    		   //blink by changing visibility
	    		if (uiBlinkingText.getVisibility()==View.INVISIBLE)
	    		{
	    			//Log.d("l30","BlinkingTaskVis");
	    			uiBlinkingText.setVisibility(View.VISIBLE);
	    		}
	    		else
	    		{
	    			//Log.d("l30","BlinkingTaskInVis");
	    			uiBlinkingText.setVisibility(View.INVISIBLE);
	    		}
	    		
	    		if (blinkingTextShouldQuit==true)
	    		{
	    			//should the text be visible after blinking stops?
	    			if (blinkingTextQuitMode==0)
	    				uiBlinkingText.setVisibility(View.INVISIBLE);
	    			else
	    				uiBlinkingText.setVisibility(View.VISIBLE);
	    			
	    			return;
	    		}
	    			    
	    		//enqueue next blink event
    	       handler.postDelayed(this, 200);
    	   }
    	};
    	
    
    /*
    * Start blinking big texts
    */
    private void StartBlinkingText(String text)
    {
    	blinkingTextQuitMode = 0;
    	blinkingTextShouldQuit = false;
    	//Log.d("l30","startBlink");
    	uiBlinkingText.setText(text);
    	handler.post(blinkingTextRunnable);   	

    }
    
    /*
     * Stop the blinking.
     */
    private void StopBlinkingText()
    {
    	//Log.d("l30","stopBlink");    	
    	blinkingTextShouldQuit = true;
    	
    }
    
	/*
	 * Change the level state
	 */
    public void changeLevelState(int state)
    {
    	if (state == 1)
    	{    
    		uiInstructionText.setVisibility(View.INVISIBLE); 
    		//start blinking "more"
    		StartBlinkingText(getString(R.string.l30_more));
    	
    	}
    	else if (state == 2)
    	{   
    		
    		StopBlinkingText();
    		//start the "more" modus
    		
            for (int i=0; i<4; i++)
            {
            	uiButtonBuy[i].setVisibility(View.VISIBLE);
            }    		
    	
    	}    
    	else if (state == 3)
    	{       		
    		//start blinking "faster"
    		StartBlinkingText(getString(R.string.l30_faster));
    	
    	}    
    	else if (state == 4)
    	{
    		//start "faster" modus       		
    	    mp = MediaPlayer.create(this, R.raw.l30_musicfast);
    	    mp.setLooping(true);
    	    setMusicVolume(0.8f);    	    
    	    playMusic();
    	    
    		StopBlinkingText();
    		 	
    	}
    	else if (state == 5)
    	{    
    		//start blinking "still money left"
    		StartBlinkingText(getString(R.string.l30_hurry));    	
    	}        	
    	
    }
	
    /*
     * Start music
     */
    public void playMusic() {
		
    	//sounds enabled?
		Intent callingIntent = getIntent();
		SessionState s = new SessionState(callingIntent.getExtras());		
		if (s.isMusicAndSoundOn()==false)
			return;
		
        if (!mp.isPlaying()) {
        	mp.seekTo(0);
        	mp.start();
        }
    }
    
    /*
     * Set music volume
     */
    public void setMusicVolume(float volumeTarget) {
        if (mp.isPlaying())
        	{
    	    
        		mp.setVolume(volumeTarget,volumeTarget);
        	}
    }
    
    /*
     * pause music
     */
    public void pauseMusic() {
        if (mp.isPlaying()) mp.pause();
    }

    /*
     * stop and release music
     */
    public void releaseMusic() {
    
    	mp.stop();
    	mp.release();
    }
}

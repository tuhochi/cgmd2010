package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.ImageView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLView;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.sound.SoundManager;

/**
 * Wrapper activity demonstrating the use of {@link GLSurfaceView}, a view
 * that uses OpenGL drawing into a dedicated surface.
 */
public class LevelActivity extends Activity {
	
	private Vector2 mWindowSize;
	private GLView mNormalModeView;
	private final Handler mHandler = new Handler();
	private NormalModeWorld mWorld;
	private TextView mHealthText;
	private TextView mSpacer;
	private TextView mPointsText;
	private ImageView mHealthIcon;
	private ImageView mGoldIcon;
	private ImageView mTimeIcon;
	private TextView mTimeText;
	private TextView mStartText;
	private Vibrator mVibrator;
	private int mCurrentMoney; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mWorld = new NormalModeWorld(this, mHandler, savedInstanceState);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        mWindowSize = new Vector2((float)d.getWidth(), (float)d.getHeight());

        mNormalModeView = new GLView(this, mWindowSize, mWorld);
        setContentView(mNormalModeView);
        
        LinearLayout llayout = new LinearLayout(this);
        llayout.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        llayout.setOrientation(LinearLayout.HORIZONTAL);
        
        LinearLayout llayout2 = new LinearLayout(this);
        llayout2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        llayout2.setOrientation(LinearLayout.HORIZONTAL);
        
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(llayout, params);
        addContentView(llayout2, params);
        
        mHealthIcon = new ImageView(this);
        mHealthIcon.setImageResource(R.drawable.l17_heart);
        
        mHealthText = new TextView(this);
        mHealthText.setText("5");
        mHealthText.setTextSize(25);
        mHealthText.setGravity(Gravity.CENTER);
        mHealthText.setTextColor(Color.BLACK);  
        mHealthText.setBackgroundResource(R.drawable.l17_text_bg);
        
        mSpacer = new TextView(this);
        
        mGoldIcon = new ImageView(this);
        mGoldIcon.setImageResource(R.drawable.l17_coin);
        
        mPointsText = new TextView(this);
        mPointsText.setText("100");
        mPointsText.setTextSize(25);
        mPointsText.setGravity(Gravity.CENTER);
        mPointsText.setTextColor(Color.BLACK);
        mPointsText.setBackgroundResource(R.drawable.l17_text_bg);
        
        mTimeIcon = new ImageView(this);
        mTimeIcon.setImageResource(R.drawable.l17_time);
        
        mTimeText = new TextView(this);
        mTimeText.setText("3:00");
        mTimeText.setTextSize(25);
        mTimeText.setGravity(Gravity.CENTER);
        mTimeText.setTextColor(Color.BLACK);
        mTimeText.setBackgroundResource(R.drawable.l17_text_bg);
        
        mStartText = new TextView(this);
        mStartText.setText(R.string.l17_start_text);
        mStartText.setTextSize(35);
        mStartText.setGravity(Gravity.CENTER);
        mStartText.setTextColor(Color.BLACK);
        mStartText.setBackgroundResource(R.drawable.l17_text_bg);

        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        llparams.weight = 0;
        llparams.setMargins(10, 10, 10, 10);
        LinearLayout.LayoutParams llparams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        llparams3.weight = 0;
        llparams3.setMargins(10, 25, 10, 10);
        llayout.addView(mHealthIcon, llparams3);
        llayout.addView(mHealthText, llparams);
        LinearLayout.LayoutParams llparams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        llparams2.weight = 1;
        llparams2.setMargins(10, 10, 10, 10);
        llayout.addView(mSpacer, llparams2);
        llayout.addView(mTimeIcon, llparams3);
        llayout.addView(mTimeText, llparams);
        llayout.addView(mGoldIcon, llparams3);
        llayout.addView(mPointsText, llparams);
        llayout2.addView(mStartText, llparams);
        
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);  
        mCurrentMoney = 0;
    }

    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mNormalModeView.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mNormalModeView.onPause();
    }
    
    @Override
	protected void onStart() {
    	mNormalModeView.onStart();
		super.onStart();
	}

    @Override
	protected void onStop() {
		mNormalModeView.onStop();
		SoundManager.getInstance().releaseAudioResources();
		super.onStop();
    }

	@Override
	public void finish() {
		updateProgressResult();
		super.finish();
	}

	@Override
    public boolean onTouchEvent (MotionEvent event)
    {
		if (mStartText.getVisibility() == View.VISIBLE)
			mStartText.setVisibility(View.INVISIBLE);
    	if(event.getAction() == MotionEvent.ACTION_MOVE)
    	{
    		mNormalModeView.fingerMove(event.getX() / mWindowSize.x, event.getY() / mWindowSize.y);
    	}
    	else if(event.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		mNormalModeView.fingerDown(event.getX() / mWindowSize.x, event.getY() / mWindowSize.y);
    	}
    	else if(event.getAction() == MotionEvent.ACTION_UP)
    	{
    		mNormalModeView.fingerUp(event.getX() / mWindowSize.x, event.getY() / mWindowSize.y);
    	}
    	try {
			Thread.sleep(16);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return super.onTouchEvent(event);	
    }
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mWorld.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	/**
	 * Is called when the health of the player changes
	 * @param hp The new health of the player
	 */
    public void playerHPChanged(float hp)
    {
    	if(hp > 0)
    		mHealthText.setText(Integer.toString((int)hp));
    	else
    		finish();
    		//mHealthText.setText(getString(getResources().getIdentifier("l17_gameover_text", "string", "at.ac.tuwien.cg.cgmd.bifth2010")));
    	  
    	// 1. Vibrate for 1000 milliseconds  
    	long milliseconds = 70;  
    	mVibrator.vibrate(milliseconds);  
    }    
    
    public void updatePlayTime(float playTime)
    {
    	int maxTime = 180;
    	int remainingTime = maxTime - (int)playTime;
    	String minutes = Integer.toString(remainingTime/60);
    	String seconds = Integer.toString(remainingTime%60);
    	if (seconds.length() == 1)
    		seconds = "0" + seconds;
    	mTimeText.setText(minutes + ":" + seconds);
    	if (playTime >= maxTime)
    		finish();
    }
    
	/**
	 * Is called when the money of the player changes
	 * @param hp The new money of the player
	 */
    public void playerMoneyChanged(int money, boolean vibrate)
    {
    	mPointsText.setText(Integer.toString(100-money)); 
    	mCurrentMoney = money; 
    	long milliseconds = 30;  
    	if (vibrate)
    		mVibrator.vibrate(milliseconds); 
    	if (money >= 100)
    		finish();
    }

    
	/**
	 * stores the current player progress in a SessionState variable and sets it as the activity result.
	 * @param progess The current progress (0-100)
	 */
    private void updateProgressResult()
    {
		//the SessionState is a convenience class to set a result
		SessionState s = new SessionState();
		//we set the progress the user has made (must be between 0-100)
		s.setProgress(Math.min(Math.max(mCurrentMoney, 0), 100));
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
    }
}

package at.ac.tuwien.cg.cgmd.bifth2010.level30;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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

//i shamelessly use the vector math and opengl classes from group 17
//


/*
 * Implements the activity for level 30
 */
public class LevelActivity extends Activity implements OnClickListener {

	private Vector2 windowSize;
	private ViewGL view;
	private GameWorld gameWorld;	
	private TextView uiScoreText;
	private Button[] uiButtonBuy;
	private int buttonWidth;
	private int money;

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
   
	    super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameWorld = new GameWorld(this, handler, savedInstanceState);
        
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        windowSize = new Vector2((float)d.getWidth(), (float)d.getHeight());
        
        buttonWidth = d.getWidth()/6;

        view = new ViewGL(this, windowSize, gameWorld);
        setContentView(view);
        
        //add ui elements        
        LinearLayout topLayout = new LinearLayout(this);
        topLayout.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        topLayout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(topLayout, params);
           
        uiScoreText = new TextView(this);
        uiScoreText.setText("1000000$");
        uiScoreText.setTextSize(25);
        uiScoreText.setGravity(Gravity.CENTER);
        uiScoreText.setTextColor(Color.BLACK);
        
        topLayout.addView(uiScoreText);
        
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
        	uiButtonBuy[i].setText("BUY");
        	uiButtonBuy[i].setTextSize(25);
        	uiButtonBuy[i].setGravity(Gravity.CENTER);
        	uiButtonBuy[i].setTextColor(Color.BLACK);
        	uiButtonBuy[i].setWidth(buttonWidth);  	
        	buttonLayoutBuy.addView(uiButtonBuy[i]);
        	uiButtonBuy[i].setTag((Integer)i);       	
        	uiButtonBuy[i].setOnClickListener(this);
        }
            
    		
		SessionState s = new SessionState();
		s.setProgress(0);		
		setResult(Activity.RESULT_OK, s.asIntent());	   
	    
		money = 1000000;
		
		gameWorld.start();
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
				
				if (b.getText()=="BUY")
				{				
					//inform gameworld of transaction
					gameWorld.StockMarketTransaktion(num, TransactionType.BUY);
		
					//change button type
					b.setTextColor(Color.MAGENTA);
					b.setText("SELL");
					b.setWidth(buttonWidth);
				}
				else if (b.getText()=="SELL")
				{				
					gameWorld.StockMarketTransaktion(num, TransactionType.SELL);
					b.setTextColor(Color.BLACK);
					b.setText("BUY");
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
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
	    super.onPause();
	    view.onPause();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		view.onStart();
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		view.onStop();
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
	 * Called from gameworld through handler. Save state&Quit
	 */
	public void finish() {	
		Log.d("L30", "LevelActivity.finish");
		updateProgressResult();			
		super.finish();
	}
	
	/*
	 * Set the session state
	 */
    private void updateProgressResult()
    {
		//the SessionState is a convenience class to set a result
		SessionState s = new SessionState();
		//we set the progress the user has made (must be between 0-100)
		s.setProgress((int)Math.min(Math.max( (1000000.0f - money)/10000.0f, 0.0f), 100.0f));
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
    }
	

}

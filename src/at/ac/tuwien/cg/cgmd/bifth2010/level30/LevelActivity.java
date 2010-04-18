package at.ac.tuwien.cg.cgmd.bifth2010.level30;

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

class Pair<A,B>
{
	Pair(A f, B s)
	{
		first = f;
		second = s;
	}
	public A first;
	public B second;
}

public class LevelActivity extends Activity implements OnClickListener {

	private Vector2 windowSize;
	private ViewGL view;
	private GameWorld gameWorld;
	
	private TextView uiScoreText;
	private Button[] uiButtonBuy;
	private Button[] uiButtonSell;
	
	private final Handler handler = new Handler();
	
	int buttonWidth;
	
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
        uiButtonSell = new Button[4];
        
        LinearLayout bottomLayout = new LinearLayout(this);
        bottomLayout.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        bottomLayout.setOrientation(LinearLayout.VERTICAL);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(bottomLayout, params);
        
        LinearLayout buttonLayoutBuy = new LinearLayout(this);
        buttonLayoutBuy.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        buttonLayoutBuy.setOrientation(LinearLayout.HORIZONTAL);
        bottomLayout.addView(buttonLayoutBuy);
        
       
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
            
        /* for (int i=0; i<4; i++)
        {        	
        	uiButtonSell[i] = new Button(this);
        	uiButtonSell[i].setText("Sell");
        	uiButtonSell[i].setTextSize(25);
        	uiButtonSell[i].setGravity(Gravity.CENTER);
        	uiButtonSell[i].setTextColor(Color.WHITE);
        	uiButtonSell[i].setTag(new Pair<Integer,Integer>(1,i));        	
        	buttonLayoutSell.addView(uiButtonSell[i]);
        	uiButtonSell[i].setOnClickListener(this);        	
        }*/
        

		
		SessionState s = new SessionState();
		s.setProgress(0);		
		setResult(Activity.RESULT_OK, s.asIntent());	   
	    
	}
	
	@SuppressWarnings("unchecked")
	public void onClick(View v)
	{		
		if (v instanceof Button)
		{
		
			Button b = (Button)v;
			
			if (b.getTag() instanceof  Integer)
			{
				int num = (Integer)v.getTag() ;
				
				if (b.getText()=="BUY")
				{				
					gameWorld.StockMarketTransaktion(num, TransactionType.BUY);
		
					b.setText("SELL");
					b.setWidth(buttonWidth);
				}
				else if (b.getText()=="SELL")
				{				
					gameWorld.StockMarketTransaktion(num, TransactionType.SELL);
					b.setText("BUY");
					b.setWidth(buttonWidth);
				}
			}
		}
	

	}
	
	@Override
	protected void onResume() {

	    super.onResume();
	    view.onResume();
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    view.onPause();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		view.onStart();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		view.onStop();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_MOVE)
		{
			
		}
		else if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			
		}
		else if(event.getAction() == MotionEvent.ACTION_UP)
		{
			
		}
		
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}  
        
	
    public void playerMoneyChanged(float money)
    {
    	uiScoreText.setText(Integer.toString((int)money)+"$"); 
    }

	

}

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
   
	    super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameWorld = new GameWorld();
        
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        windowSize = new Vector2((float)d.getWidth(), (float)d.getHeight());

        view = new ViewGL(this, windowSize, gameWorld);
        setContentView(view);
        
        LinearLayout topLayout = new LinearLayout(this);
        topLayout.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        topLayout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(topLayout, params);
           
        uiScoreText = new TextView(this);
        uiScoreText.setText("TEST");
        uiScoreText.setTextSize(25);
        uiScoreText.setGravity(Gravity.CENTER);
        uiScoreText.setTextColor(Color.WHITE);
        
        topLayout.addView(uiScoreText);
        
        uiButtonBuy = new Button[4];
        uiButtonSell = new Button[4];
        
        LinearLayout buttonLayoutBuy = new LinearLayout(this);
        buttonLayoutBuy.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        buttonLayoutBuy.setOrientation(LinearLayout.HORIZONTAL);
        topLayout.addView(buttonLayoutBuy);
        
        LinearLayout buttonLayoutSell = new LinearLayout(this);
        buttonLayoutSell.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        buttonLayoutSell.setOrientation(LinearLayout.HORIZONTAL);
        topLayout.addView(buttonLayoutSell);
        
        for (int i=0; i<4; i++)
        {
        	uiButtonBuy[i] = new Button(this);
        	uiButtonBuy[i].setText("BUY");
        	uiButtonBuy[i].setTextSize(25);
        	uiButtonBuy[i].setGravity(Gravity.CENTER);
        	uiButtonBuy[i].setTextColor(Color.WHITE);
        	buttonLayoutBuy.addView(uiButtonBuy[i]);
        	uiButtonBuy[i].setTag(new Pair<Integer,Integer>(0,i));       	
        	uiButtonBuy[i].setOnClickListener(this);
        }
            
         for (int i=0; i<4; i++)
        {        	
        	uiButtonSell[i] = new Button(this);
        	uiButtonSell[i].setText("Sell");
        	uiButtonSell[i].setTextSize(25);
        	uiButtonSell[i].setGravity(Gravity.CENTER);
        	uiButtonSell[i].setTextColor(Color.WHITE);
        	uiButtonSell[i].setTag(new Pair<Integer,Integer>(1,i));        	
        	buttonLayoutSell.addView(uiButtonSell[i]);
        	uiButtonSell[i].setOnClickListener(this);        	
        }
        
		
		SessionState s = new SessionState();
		s.setProgress(0);		
		setResult(Activity.RESULT_OK, s.asIntent());	   
	    
	}
	
	@SuppressWarnings("unchecked")
	public void onClick(View v)
	{		
		
		if (v.getTag() instanceof  Pair<?,?>)
		{
			String test = "";
			Pair<Integer,Integer> tag = (Pair<Integer,Integer>)v.getTag();
			if (tag.first == 0)
				test += "BUY";
			else
				test += "SELL";
			
			test+=" ";
			test+=tag.second;
			
			uiScoreText.setText(test);
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
        

	

}

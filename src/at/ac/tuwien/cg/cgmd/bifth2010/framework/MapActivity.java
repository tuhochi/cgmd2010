package at.ac.tuwien.cg.cgmd.bifth2010.framework;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.Path.PathPoint;

/**
 * 
 * @author Peter Rautek
 * This Activity displays a map showing the different levels.
 * It also visualizes the user's progress 
 *    
 */

public class MapActivity extends Activity {
	
	private static final String CLASS_TAG = MapActivity.class.getName();
	
	private TextView mDebugTextView;
	//private String mDebugText;
	private Path mPath = new Path();
	private float mProgress = 0;
	private ImageView mPlayer = null;
	private RelativePositionLayout mLayout = null;
	
	
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //make fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        
        // set the layout
        setContentView(R.layout.l00_map);
        
        // get the frame layout
        FrameLayout fl = (FrameLayout) findViewById(R.id.l00_FrameLayout);
        
        AbsoluteLayout absoluteLayout = (AbsoluteLayout) findViewById(R.id.l00_AbsoluteLayout);
        mLayout = new RelativePositionLayout(this);
  
        mLayout.setBackgroundResource(R.drawable.l00_map_landscape);
        Drawable d = mLayout.getBackground();;
        int iSize = absoluteLayout.getChildCount();

        // we convert the layouting coordinates into relative coordinates and exchange the absolute view with a relativepositioning view 
        float fW = 480;
        float fH = 270;
        String s ="";
        for(int i=0; i<iSize; i++) {
        	View v = absoluteLayout.getChildAt(0);
        	AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) v.getLayoutParams(); 
        	float fX = ((float)lp.x) / fW * 100.f;
        	float fY = ((float)lp.y) / fH * 100.f;
        	
        	s += "Point p = new PathPoint(" + Float.toString(fX) + ", " + Float.toString(fY) + ")";  
        	
        	absoluteLayout.removeView(v);
        	lp.x = (int) fX;
        	lp.y = (int) fY;
        	if(! ( (v.getId()==R.id.l00_ImageButtonWayPoint01) || (v.getId()==R.id.l00_ImageButtonWayPoint02) || (v.getId()==R.id.l00_ImageButtonWayPoint03) || (v.getId()==R.id.l00_ImageButtonWayPoint04) ) ) {
        		mLayout.addView(v);
        	}
        }
        Log.d(CLASS_TAG, s);
        
        fl.removeView(absoluteLayout);
        fl.addView(mLayout);
        
        
        mPlayer = (ImageView) findViewById(R.id.l00_ImageButtonPlayer);
        
        ImageButton l00 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel00);
        ImageButton l01 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel01);
        ImageButton l02 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel02);
        ImageButton l03 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel03);
        ImageButton l04 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel04);
        ImageButton l05 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel05);
        ImageButton l06 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel06);
        ImageButton l07 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel07);
        
        mLayout.findViewById(R.id.l00_ImageButtonLevel01).setBackgroundResource(R.drawable.l00_coin64);
        
        l00.setOnClickListener(mLevelClickListener);
        l01.setOnClickListener(mLevelClickListener);
        l02.setOnClickListener(mLevelClickListener);
        l03.setOnClickListener(mLevelClickListener);
        l04.setOnClickListener(mLevelClickListener);
        l05.setOnClickListener(mLevelClickListener);
        l06.setOnClickListener(mLevelClickListener);
        l07.setOnClickListener(mLevelClickListener);
        
        // get the debug text view
        mDebugTextView = (TextView) findViewById(R.id.l00_TextViewFps);
    }
    
    private Handler mUiUpdateHandler = new Handler(){
    	
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what==2) {
    			mLayout.findViewById(R.id.l00_ImageButtonLevel02).setBackgroundResource(R.drawable.l00_coin64);
    		}
    		else if(msg.what==3) {
    			mLayout.findViewById(R.id.l00_ImageButtonLevel03).setBackgroundResource(R.drawable.l00_coin64);
    		}
    		else if(msg.what==4) {
    			mLayout.findViewById(R.id.l00_ImageButtonLevel04).setBackgroundResource(R.drawable.l00_coin64);
    		}
    		else if(msg.what==5) {
    			mLayout.findViewById(R.id.l00_ImageButtonLevel05).setBackgroundResource(R.drawable.l00_coin64);
    		}
    		else if(msg.what==6) {
    			mLayout.findViewById(R.id.l00_ImageButtonLevel06).setBackgroundResource(R.drawable.l00_coin64);
    		} 
    		else if(msg.what==7) {
    			mLayout.findViewById(R.id.l00_ImageButtonLevel07).setBackgroundResource(R.drawable.l00_coin64);
    		}
    		
    		PathPoint p = mPath.interpolate(mProgress);
    		AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) mPlayer.getLayoutParams();
        	lp.x = (int) p.mX;
        	lp.y = (int) p.mY;
        	mDebugTextView.setText("x: " + p.mX + ",y: "+ p.mY);
        	mPlayer.invalidate();
    	};
    };
   
    
    private OnClickListener mLevelClickListener = new OnClickListener(){

    	@Override
		public void onClick(View v) {
    		int iMaxAllowedLevel = (int)mProgress+1;
    		mUiUpdateHandler.sendEmptyMessage(0);
    		
			int iId = v.getId();
			Intent levelIntent = new Intent();
			
			switch(iId) {
			case R.id.l00_ImageButtonLevel00:
				levelIntent.setClassName(Constants.PACKAGE_NAME, at.ac.tuwien.cg.cgmd.bifth2010.framework.GlTestActivity.class.getName());
				startActivityForResult(levelIntent, 0);
				break;
				
			case R.id.l00_ImageButtonLevel01:
				if(iMaxAllowedLevel>=1) {
					levelIntent.setClassName(Constants.PACKAGE_NAME, at.ac.tuwien.cg.cgmd.bifth2010.level01.GLActivity.class.getName());
					startActivityForResult(levelIntent, 1);
				} 
				break;
			case R.id.l00_ImageButtonLevel02:
				if(iMaxAllowedLevel>=2) {
					levelIntent.setClassName(Constants.PACKAGE_NAME, at.ac.tuwien.cg.cgmd.bifth2010.level02.GlActivity.class.getName());
					startActivityForResult(levelIntent, 2);
				} else {
					Toast.makeText(getApplicationContext(), R.string.l00_unallowedLevel, Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.l00_ImageButtonLevel03:
				if(iMaxAllowedLevel>=3) {
					levelIntent.setClassName(Constants.PACKAGE_NAME, at.ac.tuwien.cg.cgmd.bifth2010.level03.GlActivity.class.getName());
					startActivityForResult(levelIntent, 3);
				} else {
					Toast.makeText(getApplicationContext(), R.string.l00_unallowedLevel, Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.l00_ImageButtonLevel04:
				if(iMaxAllowedLevel>=4) {
					levelIntent.setClassName(Constants.PACKAGE_NAME, at.ac.tuwien.cg.cgmd.bifth2010.level04.GlActivity.class.getName());
					startActivityForResult(levelIntent, 4);
				} else {
					Toast.makeText(getApplicationContext(), R.string.l00_unallowedLevel, Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.l00_ImageButtonLevel05:
				if(iMaxAllowedLevel>=5) {
					levelIntent.setClassName(Constants.PACKAGE_NAME, at.ac.tuwien.cg.cgmd.bifth2010.level05.GlActivity.class.getName());
					startActivityForResult(levelIntent, 5);
				} else {
					Toast.makeText(getApplicationContext(), R.string.l00_unallowedLevel, Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.l00_ImageButtonLevel06:
				if(iMaxAllowedLevel>=6) {
					levelIntent.setClassName(Constants.PACKAGE_NAME, at.ac.tuwien.cg.cgmd.bifth2010.level06.GlActivity.class.getName());
					startActivityForResult(levelIntent, 6);
				} else {
					Toast.makeText(getApplicationContext(), R.string.l00_unallowedLevel, Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.l00_ImageButtonLevel07:
				if(iMaxAllowedLevel>=7) {
					levelIntent.setClassName(Constants.PACKAGE_NAME, at.ac.tuwien.cg.cgmd.bifth2010.level07.GlActivity.class.getName());
					startActivityForResult(levelIntent, 7);
				} else {
					Toast.makeText(getApplicationContext(), R.string.l00_unallowedLevel, Toast.LENGTH_LONG).show();
				}
				break;
			};
		}
    };
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode==Activity.RESULT_OK) {
    		//get the points the user got in this level
    		//points must always be in the range 0-100
    		int iPoints = data.getIntExtra(Constants.RETURN_VALUE_POINTS, 0);
    		if(iPoints<0) iPoints = 0;
    		if(iPoints>100) iPoints = 100;
    		int iLastLevel = (int)mProgress;
    		mProgress += ((float)iPoints)/100;
    		int iNewLevel = (int)mProgress;
    		if (iNewLevel == Constants.NUMBER_OF_LEVELS)
    		{
    			//user won
    			//TODO
    			//finish game    			
    		} else if (iNewLevel>iLastLevel) {
    			
    		}
    	}
    }
    
}
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

	private static final String CLASS_TAG = MapActivity.class.getSimpleName();
	public static final String EXTRA_STARTNEW = "startnew";
	//TODO remove textview
	private TextView mDebugTextView;

	private Path mPath = new Path();
	private int mProgress2 = 0;
	private int mMaxAllowedLevel2 = 1;
	private ImageView mPlayer = null;
	private RelativePositionLayout mLayout = null;



	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent callingIntent = getIntent();
		if((callingIntent!=null)&&callingIntent.hasExtra(EXTRA_STARTNEW)) {
			if(callingIntent.getBooleanExtra(EXTRA_STARTNEW, false)){
				//TODO
				//initGameState();
				Log.d(CLASS_TAG, "Initializing new game state!");
			} else {
				//TODO resume game state every time the activity is started by the user
				//resumeGameState();
				Log.d(CLASS_TAG, "Resuming game state!");
			}
		} else {
			//TODO resume game state every time the activity is restarted by the system
			//resumeGameState();
		}



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

		mLayout.findViewById(R.id.l00_ImageButtonLevel01).setBackgroundResource(R.drawable.l00_coin40);

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
				mLayout.findViewById(R.id.l00_ImageButtonLevel02).setBackgroundResource(R.drawable.l00_coin40);
				// TODO play new level sound
			}
			else if(msg.what==3) {
				mLayout.findViewById(R.id.l00_ImageButtonLevel03).setBackgroundResource(R.drawable.l00_coin40);
				// TODO play new level sound
			}
			else if(msg.what==4) {
				mLayout.findViewById(R.id.l00_ImageButtonLevel04).setBackgroundResource(R.drawable.l00_coin40);
				// TODO play new level sound
			}
			else if(msg.what==5) {
				mLayout.findViewById(R.id.l00_ImageButtonLevel05).setBackgroundResource(R.drawable.l00_coin40);
				// TODO play new level sound
			}
			else if(msg.what==6) {
				mLayout.findViewById(R.id.l00_ImageButtonLevel06).setBackgroundResource(R.drawable.l00_coin40);
				// TODO play new level sound
			} 
			else if(msg.what==7) {
				//TODO remove this (there is no level 7)??? 
				mLayout.findViewById(R.id.l00_ImageButtonLevel07).setBackgroundResource(R.drawable.l00_coin40);
				// TODO play new level sound
			}

			float fProgress = (float)(mMaxAllowedLevel2-1)  + ((float)mProgress2  / (100.0f)); 
			PathPoint p = mPath.interpolate(fProgress);
			AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) mPlayer.getLayoutParams();
			lp.x = (int) p.mX;
			lp.y = (int) p.mY;
			mDebugTextView.setText("x: " + p.mX + ",y: "+ p.mY);
			mPlayer.invalidate();
		};
	};

	@Override
	protected void onStart() {
		mUiUpdateHandler.sendEmptyMessage(0);
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		// TODO play music if sound is allowed
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Pause music if it's running
		super.onPause();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// TODO store the state everytime the activity is killed   
		// storeGameState();
	}

	private OnClickListener mLevelClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {

			int iLevel = 0;
			int iId = v.getId();
			switch(iId) {
			case R.id.l00_ImageButtonLevel00:
				//TODO anything here???
				break;
			case R.id.l00_ImageButtonLevel01:
				iLevel = 1; 
				break;
			case R.id.l00_ImageButtonLevel02:
				iLevel = 2;
				break;
			case R.id.l00_ImageButtonLevel03:
				iLevel = 3;
				break;
			case R.id.l00_ImageButtonLevel04:
				iLevel = 4;
				break;
			case R.id.l00_ImageButtonLevel05:
				iLevel = 5;
				break;
			case R.id.l00_ImageButtonLevel06:
				iLevel = 6;
				break;
			case R.id.l00_ImageButtonLevel07:
				iLevel = 7;
				break;
			};

			if(iLevel<=mMaxAllowedLevel2) {
				SessionState s = new SessionState();
				s.setLevel(iLevel);
				s.setProgress(mProgress2);
				//TODO set appropriate level to start
				Intent levelIntent = new Intent(getApplicationContext(), at.ac.tuwien.cg.cgmd.bifth2010.level00.TestActivity.class);
				levelIntent.putExtras(s.asBundle());
				startActivityForResult(levelIntent, 1);
			} else { 
				//TODO play unallowed sound
				Toast.makeText(getApplicationContext(), R.string.l00_unallowedLevel, Toast.LENGTH_LONG).show();
			}

		}

		
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK) {
			//get the points the user got in this level
			//points must always be in the range 0-100
			SessionState s = new SessionState(data.getExtras());
			int iPoints = s.getProgress();
			int iLastLevel=mMaxAllowedLevel2;
			increaseProgress(iPoints);
			
			if (mMaxAllowedLevel2-1 > Constants.NUMBER_OF_LEVELS_TO_PLAY)
			{
				//user won
				//finish game
				Toast.makeText(this, "Congratulations - You lost all your wealth! Do you already feel your newly gained freedom!", Toast.LENGTH_LONG).show();
				//TODO show about activity???
				finish();
			} else if (mMaxAllowedLevel2>iLastLevel) {
				//tell the ui to show a new level icon 
				mUiUpdateHandler.sendEmptyMessage(mMaxAllowedLevel2);
			} else {
				String message = "";
				if(iPoints>0){
					message+="Great! ";
				}
				message+="You lost "+iPoints+" gold!";
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
				//tell the ui to update the progress 
				mUiUpdateHandler.sendEmptyMessage(0);
			}
		}
		// TODO store the state everytime it changes  
		// storeGameState();
		Log.d(CLASS_TAG, "Storing game state!");

	}

	
	private void increaseProgress(int iDelta) {
		mProgress2+=iDelta;
		if(mProgress2>=100){
			mProgress2=0;
			mMaxAllowedLevel2++;
		}		
	}


}
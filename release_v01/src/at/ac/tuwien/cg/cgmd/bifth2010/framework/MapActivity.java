package at.ac.tuwien.cg.cgmd.bifth2010.framework;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
 * 
 * This Activity displays a map showing the different levels.
 * It also visualizes the user's progress 
 *    
 * @author Peter Rautek
 */

public class MapActivity extends Activity implements ShakeListener {

	///////////////////////////////////////////////
	//Constants
	///////////////////////////////////////////////
	private static final String CLASS_TAG = MapActivity.class.getSimpleName();

	/* deprecated for release: 
	 * Extra data of type int[6] that can be passed to the activity. 
	 * If this extra is passed to the activity the game is initialized that the 
	 * icons on the map are the levels with the corresponding ids.  
	 */
	//deprecated for release: public static final String EXTRA_DEBUG_LEVELASSIGNMENT = "firstlevel";


	/* deprecated for release: 
	 * Extra data of type boolean that can be passed to the activity. 
	 * If true a new game is started (the game state is initialized).
	 * If false the last game state is tried to be loaded
	 */
	// public static final String EXTRA_STARTNEW = "startnew";

	/**
	 * Extra data of type boolean that can be passed to the activity. 
	 * If true sound and music will be used 
	 */
	public static final String EXTRA_MUSIC_ON = "music";

	/**
	 * The Uri of the audio-loop
	 */
	private static final String LOOP_URI = "android.resource://at.ac.tuwien.cg.cgmd.bifth2010/" + R.raw.l00_map;

	/**
	 * preference file saving the current state of the game
	 */
	private static final String SHAREDPREFERENCES_FRAMEWORK_GAMESTATE_FILE = "l00_state";

	/**
	 * preference saving the progress of the player
	 */
	private static final String PREFERENCE_PROGRESS = "progress";

	/**
	 * preference saving the maximum level
	 */
	private static final String PREFERENCE_MAXLEVEL = "maxlevel";

	/*deprecated for release: 
	 * is used to save the level to icon assignment
	 * levelassignment_i stores the level id assigned to icon i 
	 */
	//deprecated for release: private static final String PREFERENCE_LEVELASSIGNMENT = "levelassignment_";

	/*deprecated for release: 
	 * used to save the max played level
	 */
	//deprecated for release: private static final String PREFERENCE_MAXPLAYEDLEVEL = "maxplayedlevel";

	/* deprecated for release: 
	 * message id for ui updates
	 */
	//private static final int MESSAGE_UPDATE_UI = 0;

	/**
	 * The default for the maxAllowedLevel variable
	 */
	private static final int DEFAULT_MAXLEVEL = 3;

	///////////////////////////////////////////////


	///////////////////////////////////////////////
	//Private Variables
	///////////////////////////////////////////////
	/**
	 * TextView that shows the progress of the player
	 */
	private TextView mStateTextView = null;

	/**
	 * The path interpolates the players position
	 */
	private Path mPath = new Path();

	/**
	 * Boolean Variable if music and sound shall be played or not
	 */
	private boolean mMusicOn = false;

	/**
	 * The ImageView showing the player icon. This is used/moved to visualize progress 
	 */
	private ImageView mPlayer = null;

	/**
	 * The custom layout that arranges level icons  
	 */
	private RelativePositionLayout mLayout = null;

	/**
	 * MediaPlayer used for playing the audio loop
	 */
	private MediaPlayer mLoopPlayer = null;

	/**
	 * shake detector  
	 */
	private ShakeDetector mShakeDetector = null;

	
	/* deprecated for release: 
	 * is this game restarted or a new one
	 */
	//deprecated for release: private String INSTANCESTATE_IS_RESTARTING = "restarting";
	///////////////////////////////////////////////

	///////////////////////////////////////////////
	//Private Variables that constitute the game state
	///////////////////////////////////////////////
	/**
	 * The progress to reach the next level (0-700)
	 */
	private int mProgress = 0;

	/**
	 * The level the user already reached. At the start the reached level is 3 (i.e., the first three levels can be played)
	 *  
	 */
	private int mMaxAllowedLevel = DEFAULT_MAXLEVEL;



	/* deprecated for release: 
	 * The assignment of level icons ( 0 - 5 ) to group/level ids ( 1 - number of groups )
	 */
	//deprecated for release: private int[] mLevelAssignment = null;

	//deprecated for release: private int mMaxPlayedLevel = 0;

	///////////////////////////////////////////////


	///////////////////////////////////////////////
	//Nested classes
	///////////////////////////////////////////////

	/**
	 * Waits for the MediaManager to prepare the audio loop
	 */
	private OnPreparedListener mSoundPreparedListener = new OnPreparedListener(){
		@Override
		public void onPrepared(MediaPlayer mp) {
			mLoopPlayer.setLooping(true);
			if(mMusicOn){
				//play music if sound is allowed
				mLoopPlayer.start();
			}
		}
	};



	/**
	 * Listens for clicks on the individual level icons.
	 */
	private OnClickListener mLevelClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {

			int iLevel = 0;
			int iId = v.getId();
			switch(iId) {
			case R.id.l00_ImageButtonLevel00:
				Toast.makeText(MapActivity.this, getResources().getString(R.string.l00_map_01), Toast.LENGTH_LONG).show();;
				return;
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
				Toast.makeText(MapActivity.this, getResources().getString(R.string.l00_map_02), Toast.LENGTH_LONG).show();
				mShakeDetector.resume();
				TimerTask tt = new TimerTask(){
					@Override
					public void run() {
						mShakeDetector.pause();
					}
				};
				
				Timer t = new Timer();
				t.schedule(tt, 3500);

				return;
			};

			if((iLevel<=mMaxAllowedLevel)&&(iLevel>=1)&&(iLevel<=Constants.NUMBER_OF_LEVELS_TO_PLAY)) {
				SessionState s = new SessionState();
				s.setLevel(iLevel);
				s.setProgress(mProgress);
				s.setMusicAndSoundOn(mMusicOn);
				mShakeDetector.resume();
				//set appropriate level to start
				String sAction = Constants.getLevelActionString(getLevelAssignment(iLevel-1));
				Intent levelIntent = new Intent();
				levelIntent.setAction(sAction);
				levelIntent.putExtras(s.asBundle());
				try{
					startActivityForResult(levelIntent, 1);
				} catch(ActivityNotFoundException e) {
					Toast.makeText(MapActivity.this, getResources().getString(R.string.l00_map_03), Toast.LENGTH_SHORT).show();
				}
			} else { 
				if(mMusicOn) {
					//play "level-unallowed"-sound
					MediaPlayer soundPlayer = MediaPlayer.create(MapActivity.this, R.raw.l00_unallowed);
					try {
						if(soundPlayer!=null){
							soundPlayer.setOnCompletionListener(new OnCompletionListener(){
								@Override
								public void onCompletion(MediaPlayer mp) {
									mp.release();
								}

							});

							soundPlayer.start();
						}

					} catch (IllegalStateException e) {
						//ignore this case
					} 				
				}
				Toast.makeText(getApplicationContext(), R.string.l00_unallowedLevel, Toast.LENGTH_LONG).show();
			}
		}
	};

	/**
	 * Handler for UI updates
	 */
	private Handler mUiUpdateHandler = new Handler(){

		public void handleMessage(android.os.Message msg) {

						//show level icons for levels <=mMaxAllowedLevel
			if(mMaxAllowedLevel>=1){
				//get hardcoded values for level icon resources
				int iLevelIconResource = Constants.getLevelIconResource(getLevelAssignment(0));
				mLayout.findViewById(R.id.l00_ImageButtonLevel01).setBackgroundResource(iLevelIconResource);
			}
			if(mMaxAllowedLevel>=2){
				//get hardcoded values for level icon resources
				int iLevelIconResource = Constants.getLevelIconResource(getLevelAssignment(1));
				mLayout.findViewById(R.id.l00_ImageButtonLevel02).setBackgroundResource(iLevelIconResource);
			}
			if(mMaxAllowedLevel>=3){
				//get hardcoded values for level icon resources
				int iLevelIconResource = Constants.getLevelIconResource(getLevelAssignment(2));
				mLayout.findViewById(R.id.l00_ImageButtonLevel03).setBackgroundResource(iLevelIconResource);
			}
			if(mMaxAllowedLevel>=4){
				//get hardcoded values for level icon resources
				int iLevelIconResource = Constants.getLevelIconResource(getLevelAssignment(3));
				mLayout.findViewById(R.id.l00_ImageButtonLevel04).setBackgroundResource(iLevelIconResource);
			}
			if(mMaxAllowedLevel>=5){
				//get hardcoded values for level icon resources
				int iLevelIconResource = Constants.getLevelIconResource(getLevelAssignment(4));
				mLayout.findViewById(R.id.l00_ImageButtonLevel05).setBackgroundResource(iLevelIconResource);
			}
			if(mMaxAllowedLevel>=6){
				//get hardcoded values for level icon resources
				int iLevelIconResource = Constants.getLevelIconResource(getLevelAssignment(5));
				mLayout.findViewById(R.id.l00_ImageButtonLevel06).setBackgroundResource(iLevelIconResource);
			}

			//animate players progress
			int iCounter = msg.what;
			
			//if player is passing by a icon play a sound
			if ((iCounter==100)||(iCounter==200)||(iCounter==300)||(iCounter==400)||(iCounter==500)||(iCounter==600)) {
				if(mMusicOn) {
					MediaPlayer soundPlayer = MediaPlayer.create(MapActivity.this, R.raw.l00_newlevel);
					try {
						if(soundPlayer!=null){
							soundPlayer.setOnCompletionListener(new OnCompletionListener(){
								@Override
								public void onCompletion(MediaPlayer mp) {
									mp.release();
								}

							});
							soundPlayer.start();
						}

					} catch (IllegalStateException e) {
						//ignore this case
					} 				
				}
			} 

			
			float fProgress = (float)iCounter / 100.0f; 
			PathPoint p = mPath.interpolate(fProgress);
			mLayout.setRelativePosition(mPlayer, p.mX, p.mY);
			int iGold = 700 - iCounter;
			mStateTextView.setText(iGold+getResources().getString(R.string.l00_map_04));
			mPlayer.invalidate();
		};
	};

	
	///////////////////////////////////////////////



	///////////////////////////////////////////////
	//Methods
	///////////////////////////////////////////////
	/** 
	 * @see android.app.Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mShakeDetector = new ShakeDetector(this, this, 100, 3000, 5);
		mShakeDetector.pause();


		/* deprecated for release: 
		 boolean bRestart = false;
		 if(savedInstanceState!=null){
			bRestart = savedInstanceState.getBoolean(INSTANCESTATE_IS_RESTARTING, false);
		}*/
		Intent callingIntent = getIntent();
		
		/* deprecated for release:
		if(callingIntent!=null) {
			 
			if((!bRestart)&&(callingIntent.getBooleanExtra(EXTRA_STARTNEW, false))){
				//init the game state
				//int[] iLevelAssignment = callingIntent.getIntArrayExtra(EXTRA_DEBUG_LEVELASSIGNMENT);
				initGameState();
				Log.d(CLASS_TAG, "Initializing new game state!");
			} else {
				//resume game state 
				resumeGameState();
				Log.d(CLASS_TAG, "Resuming game state!");
			}
			
			

			if(callingIntent.getBooleanExtra(EXTRA_MUSIC_ON, false)){
				mMusicOn=true;
			} else {
				mMusicOn=false;
			}
		} else {
			Log.d(CLASS_TAG, "Map activity should not be started directly!");
			//resume game state every time the activity is restarted by the system
			resumeGameState();
		}
		*/
		
		if(callingIntent!=null) {
			if(callingIntent.getBooleanExtra(EXTRA_MUSIC_ON, false)){
				mMusicOn=true;
			} else {
				mMusicOn=false;
			}
		}
		
		if(mProgress>=700){
			//user won the game already and is starting it again -> initialize variables
			mProgress = 0;
			mMaxAllowedLevel = DEFAULT_MAXLEVEL;
			storeGameState();
		} else {
			resumeGameState();
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

		mLayout.setBackgroundResource(R.drawable.l00_map);
		//Drawable d = mLayout.getBackground();;
		int iSize = absoluteLayout.getChildCount();

		// For layouting we used an AbsoluteLayout that cannot be used because of different screen sizes.
		// Instead we use a RelativePositionLayout.
		// Therefore, we convert the coordinates from layouting coordinates to relative coordinates (and exchange the AbsoluteLayout with a RelativePositionLayout). 
		float fW = 480;
		float fH = 270;
		absoluteLayout.measure((int)fW, (int)fH);
		absoluteLayout.layout(0, 0, (int)fW, (int)fH);
		for(int i=0; i<iSize; i++) {
			View v = absoluteLayout.getChildAt(0);
			AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) v.getLayoutParams(); 
			float fX = ((float)lp.x+(float)v.getMeasuredWidth()*0.5f) / fW;
			float fY = ((float)lp.y+(float)v.getMeasuredHeight()*0.5f) / fH;
			absoluteLayout.removeView(v);
			if(! ( (v.getId()==R.id.l00_ImageButtonWayPoint01) || (v.getId()==R.id.l00_ImageButtonWayPoint02) || (v.getId()==R.id.l00_ImageButtonWayPoint03) || (v.getId()==R.id.l00_ImageButtonWayPoint04) ) ) {
				mLayout.addView(v, fX, fY);
			}
		}

		fl.removeView(absoluteLayout);
		fl.addView(mLayout);

		mPlayer = (ImageView) findViewById(R.id.l00_ImageButtonPlayer);
		Animation animation = new AlphaAnimation(0.9f,0.6f);
		animation.setInterpolator(new AccelerateInterpolator());
		animation.setRepeatMode(Animation.REVERSE);
		animation.setRepeatCount(Animation.INFINITE);
		animation.setDuration(800);

		mPlayer.setAnimation(animation);
		animation.start();

		ImageButton l00 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel00);
		ImageButton l01 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel01);
		ImageButton l02 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel02);
		ImageButton l03 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel03);
		ImageButton l04 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel04);
		ImageButton l05 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel05);
		ImageButton l06 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel06);
		ImageButton l07 = (ImageButton) findViewById(R.id.l00_ImageButtonLevel07);

		mLayout.findViewById(R.id.l00_ImageButtonLevel01).setBackgroundResource(R.drawable.l00_coin);

		l00.setOnClickListener(mLevelClickListener);
		l01.setOnClickListener(mLevelClickListener);
		l02.setOnClickListener(mLevelClickListener);
		l03.setOnClickListener(mLevelClickListener);
		l04.setOnClickListener(mLevelClickListener);
		l05.setOnClickListener(mLevelClickListener);
		l06.setOnClickListener(mLevelClickListener);
		l07.setOnClickListener(mLevelClickListener);

		// get the text view
		mStateTextView = (TextView) findViewById(R.id.l00_TextViewFps);

		mLoopPlayer = new MediaPlayer();
		mLoopPlayer.setOnPreparedListener(mSoundPreparedListener );
		mLoopPlayer.setOnErrorListener(new OnErrorListener(){
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				//on any error we create a new media player
				mLoopPlayer.release();
				mLoopPlayer = MediaPlayer.create(MapActivity.this, R.raw.l00_map);
				mLoopPlayer.setOnPreparedListener(mSoundPreparedListener );
				mLoopPlayer.setOnErrorListener(this);
				return true;
			}
		});
		mLoopPlayer.setLooping(true);

	}



	@Override
	protected void onStart() {
		resumeGameState();		
		super.onStart();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/* deprecated for release: 
	 
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(INSTANCESTATE_IS_RESTARTING, true);
		super.onSaveInstanceState(outState);
	}
	*/

	@Override
	protected void onResume() {
		if((mLoopPlayer !=null)&&(mMusicOn)){
			//prepare mediaplayer for audio replay
			mLoopPlayer.reset();
			Uri uri = Uri.parse(LOOP_URI);
			try {
				mLoopPlayer.setDataSource(this, uri);
				mLoopPlayer.prepareAsync();
			} catch (IllegalArgumentException e) {
				Toast.makeText(this, getResources().getString(R.string.l00_error_audio), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (SecurityException e) {
				Toast.makeText(this, getResources().getString(R.string.l00_error_audio), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IllegalStateException e) {
				Toast.makeText(this, getResources().getString(R.string.l00_error_audio), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(this, getResources().getString(R.string.l00_error_audio), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}

		super.onResume();
	}

	@Override
	protected void onPause() {
		//Stop music if it's running
		if(mLoopPlayer!=null){
			if(mLoopPlayer.isPlaying()){
				mLoopPlayer.pause();
			}
		}
		if(mShakeDetector!=null){
			mShakeDetector.pause();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		//Stop music if it's running
		if(mLoopPlayer!=null){
			if(mLoopPlayer.isPlaying()){
				mLoopPlayer.stop();
			}

			mLoopPlayer.release();
			mLoopPlayer=null;
		}
		super.onDestroy();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK) {
			//get the points the user got in this level
			//points must always be in the range 0-100
			SessionState s = new SessionState(data.getExtras());
			int iPoints = s.getProgress();
			
			increaseProgress(iPoints);

			storeGameState();

			if(iPoints > 0){
				
				String message = "";
				message+=getResources().getString(R.string.l00_map_06)+iPoints+getResources().getString(R.string.l00_map_07);
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
				if(mMusicOn) {
					MediaPlayer soundPlayer = MediaPlayer.create(MapActivity.this, R.raw.l00_gold03);
					try {
						if(soundPlayer!=null){
							soundPlayer.setOnCompletionListener(new OnCompletionListener(){
								@Override
								public void onCompletion(MediaPlayer mp) {
									mp.release();
								}

							});
							soundPlayer.start();
						}
					} catch (IllegalStateException e) {
						//ignore this case
					} 
				}
			}
			
			

			if (mProgress >= 700)
			{
				//user won
				//finish game
				Toast.makeText(this, getResources().getString(R.string.l00_map_08), Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClassName("at.ac.tuwien.cg.cgmd.bifth2010", AboutActivity.class.getCanonicalName());
				startActivity(intent);
				finish();
			} 
		}
		
		Log.d(CLASS_TAG, "Storing game state!");

	}


	private void storeGameState() {
		SharedPreferences state = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_GAMESTATE_FILE, 0);
		SharedPreferences.Editor editor = state.edit();
		editor.putInt(PREFERENCE_PROGRESS, mProgress);
		editor.putInt(PREFERENCE_MAXLEVEL, mMaxAllowedLevel);
		//deprecated for release: editor.putInt(PREFERENCE_MAXPLAYEDLEVEL, mMaxPlayedLevel);
		//deprecated for release: although the level assignment is part of the game state it doesn't need to be stored each time the game state changes  
		editor.commit();

	}

	private void resumeGameState() {
		SharedPreferences state = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_GAMESTATE_FILE, 0);
		mProgress = state.getInt(PREFERENCE_PROGRESS, 0);
		mMaxAllowedLevel = state.getInt(PREFERENCE_MAXLEVEL, DEFAULT_MAXLEVEL);
		if(mProgress>=700){
			mProgress = 0;
			mMaxAllowedLevel = DEFAULT_MAXLEVEL;
		}
		//deprecated for release: mMaxPlayedLevel = state.getInt(PREFERENCE_MAXPLAYEDLEVEL, 0);
		//deprecated for release: set level assignment of current session
		/* deprecated for release: mLevelAssignment = getNextLevelAssignment(mMaxPlayedLevel);
		for(int i=0; i<Constants.NUMBER_OF_LEVELS_TO_PLAY; i++) {
			mLevelAssignment[i]=state.getInt(PREFERENCE_LEVELASSIGNMENT+(i+1), mLevelAssignment[i]);
		}*/
		mUiUpdateHandler.sendEmptyMessage(mProgress);
	}

	/* deprecated for release: 
	private void initGameState() {
		SharedPreferences state = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_GAMESTATE_FILE, 0);
		SharedPreferences.Editor editor = state.edit();
		mProgress=0;
		mMaxAllowedLevel=DEFAULT_MAXLEVEL;
		mMaxPlayedLevel = state.getInt(PREFERENCE_MAXPLAYEDLEVEL, mMaxPlayedLevel);
		if(mMaxPlayedLevel>=Constants.LEVELIDS.length){
			mMaxPlayedLevel=mMaxPlayedLevel-Constants.LEVELIDS.length;
		}
		editor.putInt(PREFERENCE_PROGRESS, mProgress);
		editor.putInt(PREFERENCE_MAXLEVEL, mMaxAllowedLevel);
		editor.putInt(PREFERENCE_MAXPLAYEDLEVEL, mMaxPlayedLevel);
		mLevelAssignment = getNextLevelAssignment(mMaxPlayedLevel);
		if(levelAssignment!=null) {
			for(int i=0; i<Constants.NUMBER_OF_LEVELS_TO_PLAY; i++) {
				if(levelAssignment.length>i){
					if(levelAssignment[i]>=0) {
						mLevelAssignment[i]=levelAssignment[i];
					}
				}

			}
		} 
		for(int i=0; i<Constants.NUMBER_OF_LEVELS_TO_PLAY; i++) {
			editor.putInt(PREFERENCE_LEVELASSIGNMENT+(i+1),mLevelAssignment[i]);
		}
		editor.commit();
	}
*/

	/* deprecated for release: 
	private int[] getNextLevelAssignment(int iStartLevel) {
		int[] assignment = new int[6];
		int iCounter = 0;
		int iLevel = iStartLevel;
		while(iCounter < 6){
			if(iLevel>=Constants.LEVELIDS.length){
				iLevel = 0;
			}
			assignment[iCounter] = Integer.parseInt(Constants.LEVELIDS[iLevel]);
			iLevel++;
			iCounter++;
		}
		return assignment;
	}
	*/

	private int getLevelAssignment(int iIndex){
		return Integer.parseInt(Constants.LEVELIDS[iIndex]);
	}



	private void increaseProgress(int iDelta) {
		int iOldProgress = mProgress;
		int iOldHundrets = iOldProgress / 100;

		mProgress+=iDelta;
		int iNewHudrets = mProgress / 100;

		if(iOldHundrets != iNewHudrets){
			mMaxAllowedLevel++;
		}		
		
		
		//tell the ui to update the progress
		
		TimerTask task = new AnimationTask(iOldProgress, mProgress);				
		Timer timer = new Timer();
		timer.schedule(task, 0, 100);
	}
	
	private class AnimationTask extends TimerTask{
		
		int mCounter = 0;
		int mEnd = 0;
		
		public AnimationTask(int iStart, int iEnd){
			mCounter = iStart;
			mEnd = iEnd;
		}
		
		@Override
		public void run() {
			mCounter++;
			mUiUpdateHandler.sendEmptyMessage(mCounter);
			if(mCounter>=mEnd){
				this.cancel();
			}
		}
	}




	@Override
	public void onShakeDetected() {
		Toast.makeText(this, "Ooohhhh!", Toast.LENGTH_LONG).show();
		/* deprecated for release: 
		mMaxPlayedLevel++;
		if(mMaxPlayedLevel >= Constants.LEVELIDS.length){
			mMaxPlayedLevel = 0;
		}*/

		mMaxAllowedLevel++;


		if(mMusicOn) {
			//play "level-unallowed"-sound
			MediaPlayer soundPlayer = MediaPlayer.create(MapActivity.this, R.raw.l00_gold01);
			try {
				if(soundPlayer!=null){
					soundPlayer.setOnCompletionListener(new OnCompletionListener(){
						@Override
						public void onCompletion(MediaPlayer mp) {
							mp.release();
						}

					});

					soundPlayer.start();
				}
			} catch (IllegalStateException e) {
				//ignore this case
			} 				
		}

		SharedPreferences state = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_GAMESTATE_FILE, 0);
		SharedPreferences.Editor editor = state.edit();

		/* deprecated for release: 
		editor.putInt(PREFERENCE_MAXPLAYEDLEVEL, mMaxPlayedLevel);
		mLevelAssignment = getNextLevelAssignment(mMaxPlayedLevel);
		for(int i=0; i<Constants.NUMBER_OF_LEVELS_TO_PLAY; i++) {
			editor.putInt(PREFERENCE_LEVELASSIGNMENT+(i+1),mLevelAssignment[i]);
		}
		editor.commit();*/

		editor.putInt(PREFERENCE_MAXLEVEL, mMaxAllowedLevel);
		editor.commit();

		mUiUpdateHandler.sendEmptyMessage(mProgress);
		storeGameState();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, getResources().getString(R.string.l00_menu_05));
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(featureId==0){
			Intent intentHelp = new Intent(getApplicationContext(), HelpActivity.class);
			startActivity(intentHelp);

		}
		return super.onMenuItemSelected(featureId, item);
	}



}
package at.ac.tuwien.cg.cgmd.bifth2010.framework;


import java.io.IOException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
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

	///////////////////////////////////////////////
	//Constants
	///////////////////////////////////////////////
	private static final String CLASS_TAG = MapActivity.class.getSimpleName();

	/**
	 * Extra data of type int[6] that can be passed to the activity. 
	 * If this extra is passed to the activity the game is initialized that the 
	 * icons on the map are the levels with the corresponding ids.  
	 */
	public static final String EXTRA_DEBUG_LEVELASSIGNMENT = "firstlevel";

	
	/**
	 * Extra data of type boolean that can be passed to the activity. 
	 * If true a new game is started (the game state is initialized).
	 * If false the last game state is tried to be loaded
	 */
	public static final String EXTRA_STARTNEW = "startnew";

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
	
	/**
	 * is used to save the level to icon assignment
	 * levelassignment_i stores the level id assigned to icon i 
	 */
	private static final String PREFERENCE_LEVELASSIGNMENT = "levelassignment_";


	private static final int MESSAGE_UPDATE_UI = 0;

	


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
	///////////////////////////////////////////////

	///////////////////////////////////////////////
	//Private Variables that constitute the game state
	///////////////////////////////////////////////
	/**
	 * The progress to reach the next level (0-100)
	 */
	private int mProgress = 0;
	/**
	 * The reached level. At the start the reached level is 1 (i.e., only level one can be played)
	 *  
	 */
	private int mMaxAllowedLevel = 1;
	
	/**
	 * The assignment of level icons ( 0 - 5 ) to group/level ids ( 1 - number of groups )
	 */
	private int[] mLevelAssignment = null;
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
				//TODO anything here???
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
				//TODO anything here???
				return;
			};

			if((iLevel<=mMaxAllowedLevel)&&(iLevel>=1)&&(iLevel<=Constants.NUMBER_OF_LEVELS_TO_PLAY)) {
				SessionState s = new SessionState();
				s.setLevel(iLevel);
				s.setProgress(mProgress);
				//set appropriate level to start
				String sAction = Constants.getLevelActionString(mLevelAssignment[iLevel-1]);
				Intent levelIntent = new Intent();
				levelIntent.setAction(sAction);
				levelIntent.putExtras(s.asBundle());
				startActivityForResult(levelIntent, 1);
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
			
			//if(msg.what==MESSAGE_UPDATE_UI){
			
				if(mMaxAllowedLevel>=1){
					//get hardcoded values for level icon resources
					int iLevelIconResource = Constants.getLevelIconResource(mLevelAssignment[0]);
					mLayout.findViewById(R.id.l00_ImageButtonLevel01).setBackgroundResource(iLevelIconResource);
				}
				if(mMaxAllowedLevel>=2){
					//get hardcoded values for level icon resources
					int iLevelIconResource = Constants.getLevelIconResource(mLevelAssignment[1]);
					mLayout.findViewById(R.id.l00_ImageButtonLevel02).setBackgroundResource(iLevelIconResource);
				}
				if(mMaxAllowedLevel>=3){
					//get hardcoded values for level icon resources
					int iLevelIconResource = Constants.getLevelIconResource(mLevelAssignment[2]);
					mLayout.findViewById(R.id.l00_ImageButtonLevel03).setBackgroundResource(iLevelIconResource);
				}
				if(mMaxAllowedLevel>=4){
					//get hardcoded values for level icon resources
					int iLevelIconResource = Constants.getLevelIconResource(mLevelAssignment[3]);
					mLayout.findViewById(R.id.l00_ImageButtonLevel04).setBackgroundResource(iLevelIconResource);
				}
				if(mMaxAllowedLevel>=5){
					//get hardcoded values for level icon resources
					int iLevelIconResource = Constants.getLevelIconResource(mLevelAssignment[4]);
					mLayout.findViewById(R.id.l00_ImageButtonLevel05).setBackgroundResource(iLevelIconResource);
				}
				if(mMaxAllowedLevel>=6){
					//get hardcoded values for level icon resources
					int iLevelIconResource = Constants.getLevelIconResource(mLevelAssignment[5]);
					mLayout.findViewById(R.id.l00_ImageButtonLevel06).setBackgroundResource(iLevelIconResource);
				}
				//if(mMaxAllowedLevel>=7){
					//get hardcoded values for level icon resources
					//int iLevelIconResource = Constants.getLevelIconResource(mLevelAssignment[6]);
					//mLayout.findViewById(R.id.l00_ImageButtonLevel07).setBackgroundResource(iLevelIconResource);
				//}
			//}

			/*if(msg.what==2) {
				mLayout.findViewById(R.id.l00_ImageButtonLevel02).setBackgroundResource(R.drawable.l00_coin40);
			}
			else if(msg.what==3) {
				mLayout.findViewById(R.id.l00_ImageButtonLevel03).setBackgroundResource(R.drawable.l00_coin40);
			}
			else if(msg.what==4) {
				mLayout.findViewById(R.id.l00_ImageButtonLevel04).setBackgroundResource(R.drawable.l00_coin40);
			}
			else if(msg.what==5) {
				mLayout.findViewById(R.id.l00_ImageButtonLevel05).setBackgroundResource(R.drawable.l00_coin40);
			}
			else if(msg.what==6) {
				mLayout.findViewById(R.id.l00_ImageButtonLevel06).setBackgroundResource(R.drawable.l00_coin40);
			} 
			else if(msg.what==7) {
				//TODO remove this (there is no level 7)??? 
				mLayout.findViewById(R.id.l00_ImageButtonLevel07).setBackgroundResource(R.drawable.l00_coin40);
			}*/

			//update players progress
			float fProgress = (float)(mMaxAllowedLevel-1)  + ((float)mProgress  / (100.0f)); 
			PathPoint p = mPath.interpolate(fProgress);
			AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) mPlayer.getLayoutParams();
			lp.x = (int) p.mX;
			lp.y = (int) p.mY;
			int iGold = 700 - ((mMaxAllowedLevel-1)*100) - mProgress;
			mStateTextView.setText(iGold+" gold left");
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

		Intent callingIntent = getIntent();
		if(callingIntent!=null) {
			if(callingIntent.getBooleanExtra(EXTRA_STARTNEW, false)){
				//init the game state
				int[] iLevelAssignment = callingIntent.getIntArrayExtra(EXTRA_DEBUG_LEVELASSIGNMENT);
				initGameState(iLevelAssignment);	
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
		mUiUpdateHandler.sendEmptyMessage(MESSAGE_UPDATE_UI);
		super.onStart();
	}

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
				Toast.makeText(this, "Audio replay is currently not working. Restarting the game or phone might help.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (SecurityException e) {
				Toast.makeText(this, "Audio replay is currently not working. Restarting the game or phone might help.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IllegalStateException e) {
				Toast.makeText(this, "Audio replay is currently not working. Restarting the game or phone might help.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(this, "Audio replay is currently not working. Restarting the game or phone might help.", Toast.LENGTH_LONG).show();
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
			int iLastLevel=mMaxAllowedLevel;
			increaseProgress(iPoints);

			
			if(iPoints>0){
				String message = "";
				message+="Great! ";
				message+="You lost "+iPoints+" gold!";
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

			if (mMaxAllowedLevel-1 > Constants.NUMBER_OF_LEVELS_TO_PLAY)
			{
				//user won
				//finish game
				Toast.makeText(this, "Congratulations - You lost all your wealth! Do you already feel your newly gained freedom!", Toast.LENGTH_LONG).show();
				//TODO show about activity???
				finish();
			} else if (mMaxAllowedLevel>iLastLevel) {
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



			//tell the ui to update the progress 
			mUiUpdateHandler.sendEmptyMessage(MESSAGE_UPDATE_UI);

		}

		//store the state everytime it changes  
		storeGameState();
		Log.d(CLASS_TAG, "Storing game state!");

	}


	private void storeGameState() {
		SharedPreferences state = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_GAMESTATE_FILE, 0);
		SharedPreferences.Editor editor = state.edit();
		editor.putInt(PREFERENCE_PROGRESS, mProgress);
		editor.putInt(PREFERENCE_MAXLEVEL, mMaxAllowedLevel);
		//although the level assignment is part of the game state it doesn't need to be stored each time the game state changes  
		editor.commit();

	}

	private void resumeGameState() {
		SharedPreferences state = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_GAMESTATE_FILE, 0);
		mProgress = state.getInt(PREFERENCE_PROGRESS, 0);
		mMaxAllowedLevel = state.getInt(PREFERENCE_MAXLEVEL, 1);
		//set level assignment of current session
		mLevelAssignment = getNewLevelAssignment();
		for(int i=0; i<Constants.NUMBER_OF_LEVELS_TO_PLAY; i++) {
			mLevelAssignment[i]=state.getInt(PREFERENCE_LEVELASSIGNMENT+(i+1), mLevelAssignment[i]);
		}
		mUiUpdateHandler.sendEmptyMessage(MESSAGE_UPDATE_UI);
	}

	private void initGameState(int[] levelAssignment) {
		SharedPreferences state = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_GAMESTATE_FILE, 0);
		SharedPreferences.Editor editor = state.edit();
		mProgress=0;
		mMaxAllowedLevel=1;
		editor.putInt(PREFERENCE_PROGRESS, mProgress);
		editor.putInt(PREFERENCE_MAXLEVEL, mMaxAllowedLevel);
		mLevelAssignment = getNewLevelAssignment();
		if(levelAssignment!=null) {
			for(int i=0; i<Constants.NUMBER_OF_LEVELS_TO_PLAY; i++) {
				if(levelAssignment.length>i){
					mLevelAssignment[i]=levelAssignment[i];
				}
				editor.putInt(PREFERENCE_LEVELASSIGNMENT+(i+1),mLevelAssignment[i]);
			}
		} 
		editor.commit();
	}

	private int[] getNewLevelAssignment() {
		//TODO change assignment everytime the user initializes a new game!
		//different assignments can be hardcoded once the number of groups/levels is fixed
		//for the moment it is simply always level 0
		int[] assignment = {0,0,0,0,0,0};
 		return assignment;
	}



	private void increaseProgress(int iDelta) {
		mProgress+=iDelta;
		if(mProgress>=100){
			mProgress=0;
			mMaxAllowedLevel++;
		}		
	}


}
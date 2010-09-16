package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * This activity lets the user choose which action to perform
 * @author Peter Rautek
 */

public class MenuActivity extends Activity {
    
	/* deprecated for release:
	private static final String CLASS_TAG = MenuActivity.class.getSimpleName();
	*/
	
	/**
	 * the name of the preference file that stores global user settings 
	 */
	public static final String SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE = "l00_settings";

	/**
	 * global user setting of type boolean that determines wether sound and music is allowed 
	 */
	public static final String PREFERENCE_MUSIC = "music";

	private static final String LOOP_URI = "android.resource://at.ac.tuwien.cg.cgmd.bifth2010/" + R.raw.l00_menu;
	
	
	private MediaPlayer mAudioPlayer = null;
	private CheckBox mCheckboxMusic = null; 
	
	AnimationDrawable mRunningAnimation = null;
	AnimationDrawable mCoinAnimation = null;
	ImageView mImageRun = null;
	ImageView mImageCoin = null;
	TranslateAnimation mTranslationAnimation = null;
	
	
	
	
	
    private OnCheckedChangeListener mSoundSettingChangedListener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			//save application state
			SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
			boolean bMusic = settings.getBoolean(PREFERENCE_MUSIC, true);
		    if(bMusic!=isChecked){
		    	//the state actually changed by user interaction
				SharedPreferences.Editor editor = settings.edit();
			    editor.putBoolean(PREFERENCE_MUSIC, isChecked);
			    editor.commit();
		    	String m = "";
		    	if(isChecked){
		    		m=getResources().getString(R.string.l00_menu_01);
		    	} else {
		    		m=getResources().getString(R.string.l00_menu_02);
		    	}
		    	
		    	Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
		    	if((isChecked)&&(mAudioPlayer!=null)){
		       		//play music if sound is allowed
		    		mAudioPlayer.start();
		    				       		
		       	}
		    	if((!isChecked)&&(mAudioPlayer!=null)){
		       		//stop music if sound is playing and not allowed
		    		if(mAudioPlayer.isPlaying()){
		    			mAudioPlayer.pause();
		    		}
		       	}
		    }
		}    	
    };
    
	private OnPreparedListener mSoundPreparedListener = new OnPreparedListener(){
		@Override
		public void onPrepared(MediaPlayer mp) {
			mAudioPlayer.setLooping(true);
			SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
			boolean bMusic = settings.getBoolean(PREFERENCE_MUSIC, true);
		    if(bMusic){
		    	//play music if sound is allowed
		    	mAudioPlayer.start();
		    }
		}
    };
    
    
    private Handler splashHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                //remove SplashScreen from view
        		ImageView v = (ImageView) findViewById(R.id.l00_ImageViewSplash);
        		if(v!=null){
        			v.setVisibility(View.GONE);
        		}
        		LinearLayout vMenu = (LinearLayout) findViewById(R.id.l00_LinearLayoutMenu);
        		if(vMenu!=null){
        			vMenu.setVisibility(View.VISIBLE);
        		}
                
                super.handleMessage(msg);
        }
    };
    
    Handler mHandlerAnimation = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		AbsoluteLayout al =  (AbsoluteLayout) findViewById(R.id.l00_AbsoluteLayoutAnimation);
    		LinearLayout ll = (LinearLayout) findViewById(R.id.l00_LinearLayoutAnimation);
    		if(ll!=null){
    			AbsoluteLayout.LayoutParams lp = 
					(AbsoluteLayout.LayoutParams) ll.getLayoutParams();
    			
    			
        		
    			lp.x+=3;
    			int width = al.getWidth();
    			if(lp.x>width){
    				lp.x = ll.getWidth() * -1;
    			}
    			al.requestLayout();
    		}
    	};
    };
    
   
    
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l00_menu);
        splashHandler.sendMessageDelayed(new Message(), 3000);
                
        /*  deprecated for release:
        Button buttonNewGame = (Button) findViewById(R.id.l00_ButtonNewGame);
        buttonNewGame.setOnClickListener(mStartClickListener);
        */
        Button buttonContinueGame = (Button) findViewById(R.id.l00_ButtonContinueGame);
        buttonContinueGame.setOnClickListener(mStartClickListener);
        Button buttonHelp = (Button) findViewById(R.id.l00_ButtonHelp);
        buttonHelp.setOnClickListener(mStartClickListener);
        /* deprecated for release:
        Button buttonAbout = (Button) findViewById(R.id.l00_ButtonAbout);
        buttonAbout.setOnClickListener(mStartClickListener);
        */
        Button buttonCredit = (Button) findViewById(R.id.l00_ButtonCredits);
        buttonCredit.setOnClickListener(mStartClickListener);
        mCheckboxMusic = (CheckBox) findViewById(R.id.l00_CheckBoxSound);
        mCheckboxMusic.setOnCheckedChangeListener(mSoundSettingChangedListener );
        
        mAudioPlayer = MediaPlayer.create(this, R.raw.l00_menu);
        mAudioPlayer.setOnPreparedListener(mSoundPreparedListener );
        mAudioPlayer.setOnErrorListener(new OnErrorListener(){

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				//on any error we create a new media player
				mAudioPlayer.release();
				mAudioPlayer = MediaPlayer.create(MenuActivity.this, R.raw.l00_menu);
		        mAudioPlayer.setOnPreparedListener(mSoundPreparedListener );
		        mAudioPlayer.setOnErrorListener(this);
				return true;
			}
        });
        
       
        
        mImageRun = (ImageView) findViewById(R.id.l00_ImageViewAnimationRun);
        mImageRun.setBackgroundResource(R.drawable.l00_animation_run);

        mRunningAnimation = (AnimationDrawable) mImageRun.getBackground();
        
        mImageCoin = (ImageView) findViewById(R.id.l00_ImageViewAnimationCoin);
        mImageCoin.setBackgroundResource(R.drawable.l00_animation_coin);

        mCoinAnimation = (AnimationDrawable) mImageCoin.getBackground();
        
		Timer t = new Timer();
        t.schedule(new TimerTask(){

			@Override
			public void run() {
				mRunningAnimation.start();
				mCoinAnimation.start();
				
			}
        	
        }, 250);
        
        t.schedule(new TimerTask() {
			
        	int counter = 0;
			@Override
			public void run() {
				counter++;
				mHandlerAnimation.sendEmptyMessage(counter);
				
			}
		}, 100, 20);

       
    }
    
    private OnClickListener mStartClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
			boolean bMusic = settings.getBoolean(PREFERENCE_MUSIC, true);
			/*  deprecated for release:
			if(v.getId()== R.id.l00_ButtonNewGame){
			 
				
				
				Intent intentMap = new Intent(getApplicationContext(), MapActivity.class);
				// deprecated for release: intentMap.putExtra(MapActivity.EXTRA_STARTNEW, true);
				intentMap.putExtra(MapActivity.EXTRA_MUSIC_ON, bMusic);
				SharedPreferences state = getSharedPreferences(EntryPoint.SHAREDPREFERENCES_FRAMEWORK_DEBUGSETTINGS, 0);
				if(state.getBoolean(EntryPoint.PREFERENCE_DEBUG_ENABLED, false)){
					int[] iLevel = {-1,-1,-1,-1,-1,-1};
					iLevel[0] = state.getInt(EntryPoint.PREFERENCE_LEVEL_01, -1);
					iLevel[1] = state.getInt(EntryPoint.PREFERENCE_LEVEL_02, -1);
					iLevel[2] = state.getInt(EntryPoint.PREFERENCE_LEVEL_03, -1);
					iLevel[3] = state.getInt(EntryPoint.PREFERENCE_LEVEL_04, -1);
					iLevel[4] = state.getInt(EntryPoint.PREFERENCE_LEVEL_05, -1);
					iLevel[5] = state.getInt(EntryPoint.PREFERENCE_LEVEL_06, -1);
			        // deprecated for release: intentMap.putExtra(MapActivity.EXTRA_DEBUG_LEVELASSIGNMENT, iLevel);
					
				}
				
				
				startActivity(intentMap);
			} else */
			
			if(v.getId()== R.id.l00_ButtonContinueGame){
				Intent intentMap = new Intent(getApplicationContext(), MapActivity.class);
				// deprecated for release: intentMap.putExtra(MapActivity.EXTRA_STARTNEW, false);
				intentMap.putExtra(MapActivity.EXTRA_MUSIC_ON, bMusic);
				startActivity(intentMap);
			}  else if(v.getId()== R.id.l00_ButtonCredits){
				Intent intentCredits = new Intent(getApplicationContext(), CreditsActivity.class);
				startActivity(intentCredits);
			}  else if(v.getId()== R.id.l00_ButtonHelp){
				Intent intentHelp = new Intent(getApplicationContext(), HelpActivity.class);
				startActivity(intentHelp);
			} 
			/*  deprecated for release:
			else if(v.getId()== R.id.l00_ButtonAbout){
			
				Intent intentAbout = new Intent(getApplicationContext(), AboutActivity.class);
				intentAbout.putExtra(AboutActivity.EXTRA_MUSIC_ON, bMusic);
				startActivity(intentAbout);
			} */ 
		}
    };
    
    
    
    @Override
	protected void onResume() {
        //Restore preferences
        SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
        boolean bMusic = settings.getBoolean(PREFERENCE_MUSIC, true);
        //set user interface accordingly
       	mCheckboxMusic.setChecked(bMusic);
       	if(mAudioPlayer!=null){
       		//prepare mediaplayer for audio replay
       		mAudioPlayer.reset();
       		Uri uri = Uri.parse(LOOP_URI);
       		try {
				mAudioPlayer.setDataSource(this, uri);
				mAudioPlayer.prepareAsync();
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
       	
       	
      /*  //triggering the animation
       	mAnimationTimer = new Timer();
        mAnimationTimer.schedule(new TimerTask(){
			@Override
			public void run() {
				mAnimationHandler.sendEmptyMessage(0);				
			}
        }, 0, 250);*/
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		//Stop music if it's running
		if(mAudioPlayer!=null){
			if(mAudioPlayer.isPlaying()){
				mAudioPlayer.pause();
			}
		}
		
		
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		//Stop music if it's running
		if(mAudioPlayer!=null){
			if(mAudioPlayer.isPlaying()){
				mAudioPlayer.stop();
			}
			mAudioPlayer.release();
			mAudioPlayer=null;
		}
		super.onDestroy();
	}

	/* deprecated for release:
	
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Debugging On/Off");
	    return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(featureId==0){
			SharedPreferences state = getSharedPreferences(EntryPoint.SHAREDPREFERENCES_FRAMEWORK_DEBUGSETTINGS, 0);
			boolean bEnabled = state.getBoolean(EntryPoint.PREFERENCE_DEBUG_ENABLED, false);
			SharedPreferences.Editor editor = state.edit();
			bEnabled = !bEnabled;
			editor.putBoolean(EntryPoint.PREFERENCE_DEBUG_ENABLED, bEnabled);
			editor.commit();
			String msg = "Debugging is now ";
			if(bEnabled){
				msg+="enabled!";
			}else {
				msg+="disabled!";
			}
			Toast.makeText(MenuActivity.this, msg, Toast.LENGTH_LONG).show();
			if(bEnabled){
				//this will bring us back to the entry point activity
				finish();
			}
		}
		return super.onMenuItemSelected(featureId, item);
	}
	*/


}
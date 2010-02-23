package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import at.ac.tuwien.cg.cgmd.bifth2010.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * This activity lets the user choose which action to perform
 * @author Peter Rautek
 */

public class MenuActivity extends Activity {
    
	private static final String CLASS_TAG = MenuActivity.class.getSimpleName();
	public static final String SHAREDPREFERENCES_FRAMEWORK_SETTINGS = "l00_settings";
	public static final String PREFERENCE_MUSIC = "music";
	
	SoundPool mSoundPool = null;
	private int mSoundLoopId = -1;
	
	private CheckBox mCheckboxMusic = null; 
	
    private OnCheckedChangeListener mSoundSettingChangedListener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			//save application state
			SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_SETTINGS, 0);
			boolean bMusic = settings.getBoolean(PREFERENCE_MUSIC, true);
		    if(bMusic!=isChecked){
		    	//the state actually changed by user interaction

				SharedPreferences.Editor editor = settings.edit();
			    editor.putBoolean(PREFERENCE_MUSIC, isChecked);
			    editor.commit();

			    
		    	String m = "Music is ";
		    	if(isChecked){
		    		m+="on ";
		    	} else {
		    		m+="off ";
		    	}
		    	m+="now.";
		    	Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
		    	if((isChecked)&&(mSoundLoopId>=0)&&(mSoundPool!=null)){
		       		//play music if sound is allowed
		       		mSoundPool.setLoop(mSoundLoopId, -1);
		       		int iIsPlaying = mSoundPool.play(mSoundLoopId, 1, 1, 1, -1, 1);
		       		Log.d(CLASS_TAG, "Is playing: "+iIsPlaying);
		       	}
		    	if((!isChecked)&&(mSoundLoopId>=0)&&(mSoundPool!=null)){
		       		//stop music if sound is allowed
		       		mSoundPool.stop(mSoundLoopId);
		       	}
				//TODO turn music off/on
		    }

			
		}    	
    };




	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l00_menu);
        Button buttonNewGame = (Button) findViewById(R.id.l00_ButtonNewGame);
        buttonNewGame.setOnClickListener(mStartClickListener);
        Button buttonContinueGame = (Button) findViewById(R.id.l00_ButtonContinueGame);
        buttonContinueGame.setOnClickListener(mStartClickListener);
        Button buttonHelp = (Button) findViewById(R.id.l00_ButtonHelp);
        buttonHelp.setOnClickListener(mStartClickListener);
        Button buttonAbout = (Button) findViewById(R.id.l00_ButtonAbout);
        buttonAbout.setOnClickListener(mStartClickListener);
        Button buttonCredit = (Button) findViewById(R.id.l00_ButtonCredits);
        buttonCredit.setOnClickListener(mStartClickListener);
        mCheckboxMusic = (CheckBox) findViewById(R.id.l00_CheckBoxSound);
        mCheckboxMusic.setOnCheckedChangeListener(mSoundSettingChangedListener );
        
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundLoopId  = mSoundPool.load(this, R.raw.l00_menu, 1);
        
        
    }
    
    private OnClickListener mStartClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(v.getId()== R.id.l00_ButtonNewGame){
				Intent intentMap = new Intent(getApplicationContext(), MapActivity.class);
				intentMap.putExtra(MapActivity.EXTRA_STARTNEW, true);
				startActivity(intentMap);
			} else if(v.getId()== R.id.l00_ButtonContinueGame){
				Intent intentMap = new Intent(getApplicationContext(), MapActivity.class);
				intentMap.putExtra(MapActivity.EXTRA_STARTNEW, false);
				startActivity(intentMap);
			}  else if(v.getId()== R.id.l00_ButtonCredits){
				Intent intentCredits = new Intent(getApplicationContext(), CreditsActivity.class);
				startActivity(intentCredits);
			}  else if(v.getId()== R.id.l00_ButtonHelp){
				Intent intentHelp = new Intent(getApplicationContext(), HelpActivity.class);
				startActivity(intentHelp);
			}  else if(v.getId()== R.id.l00_ButtonAbout){
				Intent intentAbout = new Intent(getApplicationContext(), AboutActivity.class);
				startActivity(intentAbout);
			} 
		}
    };
    
    
    
    @Override
	protected void onResume() {
        //Restore preferences
        SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_SETTINGS, 0);
        boolean bMusic = settings.getBoolean(PREFERENCE_MUSIC, true);
       	mCheckboxMusic.setChecked(bMusic);
       	if((bMusic)&&(mSoundLoopId>=0)&&(mSoundPool!=null)){
       		//play music if sound is allowed
       		mSoundPool.setLoop(mSoundLoopId, -1);
       		mSoundPool.play(mSoundLoopId, 1, 1, 1, -1, 1);
       	}

		
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		//Stop music if it's running
		if(mSoundPool!=null){
			mSoundPool.stop(mSoundLoopId);
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		//Stop music if it's running
		if(mSoundPool!=null){
			mSoundPool.stop(mSoundLoopId);
			mSoundPool.release();
			mSoundPool=null;
		}
		super.onDestroy();
	}

}
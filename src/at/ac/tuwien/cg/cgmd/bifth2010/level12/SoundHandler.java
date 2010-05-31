package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundHandler {
	
	private static boolean mSoundOn = true;
	private SoundPool mSP = null;
	private int mStreamVolume = 100;
	private static Context mContext = null;
	private HashMap<Integer, Integer> mSoundIDs = null;
	
	private static SoundHandler mSingleton = null;
	
	public static SoundHandler getSingleton(){
		if ( mSingleton == null ){
			mSingleton = new SoundHandler( mContext );
		}
		return mSingleton;
	}
	
	public static void setContext( Context context ){
		mContext = context;
	}
	
	private SoundHandler( Context context ){
		mSoundIDs = new HashMap<Integer, Integer>();
		SharedPreferences audiosettings = context.getSharedPreferences(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
		mSoundOn = audiosettings.getBoolean(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.PREFERENCE_MUSIC, true);
		mSP = new SoundPool( Definitions.SOUND_CHANNEL_NUMBER,  AudioManager.STREAM_MUSIC, 100 );
	}
	
	
	public int addSound( int resID ){
		if( mSoundIDs.containsKey(resID)) return mSoundIDs.get(resID);
		else {
			int val = mSP.load(mContext, resID, 1);
			mSoundIDs.put(resID, val);
			return val;
		}
	}
	
	
	public void play( int sampleID ){
		if( mSoundOn && mSP != null ) mSP.play(sampleID, mStreamVolume, mStreamVolume, 1, 0, 1.0f);
	}
	
	
	public void stop(){
		mSP.stop( R.raw.l12_music);
		mSP.release();
		mSP = null;
	}
	
	public void playLoop( int sampleID){
		if( mSoundOn && mSP != null ) mSP.play(sampleID, mStreamVolume, mStreamVolume, 1, -1, 1.0f);
	}

	public void pause(int sampleID ) {
		if( mSP != null ) mSP.pause(sampleID);
	}
}

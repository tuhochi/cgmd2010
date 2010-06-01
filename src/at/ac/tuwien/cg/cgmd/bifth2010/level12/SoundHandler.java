package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
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
	
	public void addResource( int resID ){
		if( mSoundIDs.containsKey(resID)) return;
		mSoundIDs.put(resID, -1 );
	}
	
	
	public void reloadSamples(){
		Set<Integer> keys = mSoundIDs.keySet();
		if( keys.isEmpty() ) return;
		Iterator<Integer> keysiter = keys.iterator();
		do{
			int resID = keysiter.next();
			int val = mSP.load(mContext, resID, 1);
			System.out.println("LOAD SOUNDSAMPLE : "+resID+" ID: "+val);
			mSoundIDs.put(resID, val);
		} while( keysiter.hasNext());
	}
	
	
	public void play( int resID ){
		System.out.println("Trying to play: resID - "+resID+" sampleID - "+mSoundIDs.get(resID));
		if( mSoundOn == false || mSP == null ) return;
		if( mSoundIDs.containsKey(resID) == false ) return;
		mSP.play(mSoundIDs.get(resID), mStreamVolume, mStreamVolume, 1, 0, 1.0f);
	}
	
	public void playLoop( int resID){
		if( mSoundOn == false || mSP == null ) return;
		if( mSoundIDs.containsKey(resID) == false ) return;
		mSP.play(mSoundIDs.get(resID), mStreamVolume, mStreamVolume, 1, -1, 1.0f);
	}
	
	
	public void stop(){
		//mSoundIDs.clear();
		mSingleton = null;
		if( mSP == null ) return;
		mSP.release();
		mSP = null;
	}
	
	public void setSound( boolean sound ){
		mSoundOn = sound;
	}
}

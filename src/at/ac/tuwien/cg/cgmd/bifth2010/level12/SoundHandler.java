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

/** class handling the sound, responsible for playing, loading the soundsamples and managing the soundpool */
public class SoundHandler {
	
	private static boolean mSoundOn = true; /** is the sound on? */
	private SoundPool mSP = null; /** the soundpool */
	private int mStreamVolume = 100; /** the volume */
	private static Context mContext = null; /** app context */
	private HashMap<Integer, Integer> mSoundIDs = null; /** keys == sample IDs, values == loaded sound sample IDs */
	
	private static SoundHandler mSingleton = null; /** singleton */
	
	/** returns, and creates on the first call, the soundhandler singleton */
	public static SoundHandler getSingleton(){
		if ( mSingleton == null ){
			mSingleton = new SoundHandler( mContext );
		}
		return mSingleton;
	}
	
	/** sets the app context */
	public static void setContext( Context context ){
		mContext = context;
	}
	
	/** constructor, creates the hashmap for the loaded sample IDs, initializes the soundpool with the channels and soundsettings */
	private SoundHandler( Context context ){
		mSoundIDs = new HashMap<Integer, Integer>();
		SharedPreferences audiosettings = context.getSharedPreferences(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
		mSoundOn = audiosettings.getBoolean(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.PREFERENCE_MUSIC, true);
		mSP = new SoundPool( Definitions.SOUND_CHANNEL_NUMBER,  AudioManager.STREAM_MUSIC, 100 );
	}
	
	/** sound sample is added (but not loaded!)*/
	public void addResource( int resID ){
		if( mSoundIDs.containsKey(resID)) return;
		mSoundIDs.put(resID, -1 );
	}
	
	/** (re)loads the samples in the hashmap and sets as values the IDs of the loaded sample */
	public void reloadSamples(){
		Set<Integer> keys = mSoundIDs.keySet();
		System.out.println("REALODE SOUNDSAMPLES, #KEYS: "+keys.size());
		if( keys.isEmpty() ){
			System.out.println("NO SOUNDSAMPLE TO LOAD, NO KEYS IN HASHMAP");
			return;
		}
		Iterator<Integer> keysiter = keys.iterator();
		do{
			int resID = keysiter.next();
			int val = mSP.load(mContext, resID, 1);
			System.out.println("LOAD SOUNDSAMPLE : "+resID+" ID: "+val);
			mSoundIDs.put(resID, val);
		} while( keysiter.hasNext());
	}
	
	/** looks up the loaded sample ID from the hashmap and lets the soundpool play it */
	public void play( int resID ){
		
		if( mSoundOn == false || mSP == null ) return;
		if( mSoundIDs.containsKey(resID) == false ){
			System.out.println("Trying to play: resID - "+resID+", but sample not found in SoundMap.");
			return;
		}
		mSP.play(mSoundIDs.get(resID), mStreamVolume, mStreamVolume, 1, 0, 1.0f);
	}
	
	/** looks up the loaded sample ID from the hashmap and lets the soundpool play it in a endless loop, used for background music */
	public void playLoop( int resID){
		if( mSoundOn == false || mSP == null ) return;
		if( mSoundIDs.containsKey(resID) == false ) return;
		mSP.play(mSoundIDs.get(resID), mStreamVolume, mStreamVolume, 1, -1, 1.0f);
	}
	
	/** stops the sound, releases the soundpool and destroys the singleton */
	public void stop(){
		mSingleton = null;
		if( mSP == null ) return;
		mSP.release();
		mSP = null;
	}
	
	/** sets the sound to on or off */
	public void setSound( boolean sound ){
		mSoundOn = sound;
	}
}

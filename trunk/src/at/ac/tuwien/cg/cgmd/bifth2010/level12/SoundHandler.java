package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import java.util.Vector;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundHandler {
	
	private static boolean mSoundOn = true;
	private SoundPool mSP = null;
	private Vector< Integer > mSoundResources = new Vector< Integer >();
	private int mStreamVolume = 100;
	private static Context mContext = null;
	
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
		SharedPreferences audiosettings = context.getSharedPreferences(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
		mSoundOn = audiosettings.getBoolean(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.PREFERENCE_MUSIC, true);
		mSP = new SoundPool( Definitions.SOUND_CHANNEL_NUMBER,  AudioManager.STREAM_MUSIC, 100 );
	}
	
	
	public int addSound( int resID ){
		for( int i = 0; i < mSoundResources.size(); i++ ){
			if( mSoundResources.get(i) == resID ) return i;
		}
		mSoundResources.add(resID);
		return ( mSoundResources.size() - 1 );
	}
	
	
	public void loadSound(){
		addSound(R.raw.l12_music);
		for( int i = 0; i < mSoundResources.size();  i++ ) {
			int loadedmusic = mSP.load(mContext, mSoundResources.get(i), 1);
			System.out.println("LOADEDMUSIC IS "+loadedmusic+" RESOURCE "+mSoundResources.get(i));
		}
		//for( int i = 0; i < mSoundResources.size();  i++ ) System.out.println( mSoundResources.get(i));
		AudioManager am = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
		mStreamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		mSP.play(R.raw.l12_music, mStreamVolume,mStreamVolume, 1, -1, 1.0f);	
	}
	
	public void play( int sampleID ){
		if( mSoundOn && mSP != null ) mSP.play(sampleID, mStreamVolume, mStreamVolume, 1, 0, 1.0f);
	}
	
	
	public void stop(){
		mSP.stop( R.raw.l12_music);
		mSP.release();
		mSP = null;
		mSoundResources.removeAllElements();
	}
}

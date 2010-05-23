package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import java.util.Vector;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundHandler {
	
	private static boolean mSoundOn = true;
	private static SoundPool mSP = null;
	private static Vector< Integer > mSoundResources = new Vector< Integer >();
	private static int mStreamVolume = 100;
	
	private SoundHandler( Context context ){
		SharedPreferences audiosettings = context.getSharedPreferences(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
		mSoundOn = audiosettings.getBoolean(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.PREFERENCE_MUSIC, true);
	}
	
	
	public static void addSound( int resID ){
		for( int i = 0; i < mSoundResources.size(); i++ ){
			if( mSoundResources.get(i) == resID ) return;
		}
		mSoundResources.add(resID);
	}
	
	
	public static void loadSound( Context context ){
		mSP = new SoundPool( Definitions.SOUND_CHANNEL_NUMBER,  AudioManager.STREAM_MUSIC, 100 );
		addSound(R.raw.l12_music);
		for( int i = 0; i < mSoundResources.size();  i++ ) mSP.load(context, mSoundResources.get(i), 1);
		//for( int i = 0; i < mSoundResources.size();  i++ ) System.out.println( mSoundResources.get(i));
		AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		mStreamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		mSP.play(R.raw.l12_music, mStreamVolume,mStreamVolume, 1, -1, 1.0f);	
	}
	
	public static void play( int resID ){
		if( mSoundOn && mSP != null ) mSP.play(resID, mStreamVolume, mStreamVolume, 1, -1, 1.0f);
	}
	
	
	public static void stop(){
		mSP.stop( R.raw.l12_music);
		mSP.release();
		mSP = null;
		mSoundResources.removeAllElements();
	}
}

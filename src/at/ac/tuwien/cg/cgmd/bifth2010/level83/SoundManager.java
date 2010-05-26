package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class SoundManager {

	private static int maxStreams = 10;
	public static SoundManager singleton = null;
	private SoundPool soundPool;
	private Context context;
	
	private HashMap<Integer,Integer> sounds = new HashMap<Integer,Integer>();
	private ArrayList<Integer> playing = new ArrayList<Integer>();
	
	/**
	 * Creates a new SoundManager 
	 * @param context
	 */
	public SoundManager(Context context){
		if(singleton != null)
			return;
		
		singleton = this;
		
		soundPool = new SoundPool(maxStreams,AudioManager.STREAM_MUSIC,0);
		
		if(soundPool == null){
			Log.d("SoundManager","SoundPool creation failed.");	
			return;
		}else
			Log.d("SoundManager","SoundPool created.");	
		
		sounds.put(Constants.MUSIC_BACKGROUND, soundPool.load(context, Constants.MUSIC_BACKGROUND, 1));
		sounds.put(Constants.SOUND_COUNTDOWN, soundPool.load(context, Constants.SOUND_COUNTDOWN,1));
		sounds.put(Constants.SOUND_BYBY, soundPool.load(context, Constants.SOUND_BYBY, 1));
		sounds.put(Constants.SOUND_JUMP_DOWN, soundPool.load(context, Constants.SOUND_JUMP_DOWN, 1));
		sounds.put(Constants.SOUND_JUMP_UP, soundPool.load(context, Constants.SOUND_JUMP_UP, 1));
		sounds.put(Constants.SOUND_DOLLAR, soundPool.load(context,Constants.SOUND_DOLLAR,1));
		sounds.put(Constants.SOUND_ITEM,soundPool.load(context, Constants.SOUND_ITEM, 1));
		
		this.context = context;
	}
	
	public void play(int id,boolean loop, float volume, float rate){
		AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		float vmax = (float)mgr.getStreamVolume(AudioManager.STREAM_MUSIC)	/ (float)mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		int streamID = soundPool.play(sounds.get(id), volume*vmax, volume*vmax, 1, (loop)?-1:0, rate);
		
//		if(streamID != 0)
//			playing.add(streamID);
//		
	
		Log.d("SoundManager", "Play");
		
	}
	
	public void playBackGround(){	
		play(Constants.MUSIC_BACKGROUND,true,0.2f,1.0f);
	}
	
//	public void pauseAll(){
//		for(Integer id : playing)
//			soundPool.pause(id);	
//	}
//	
//	public void resumeAll(){
//		for(Integer id:playing)
//			soundPool.resume(id);
//	}
	public void dispose(){
		singleton = null;
		soundPool.release();
		soundPool = null;
	}
}

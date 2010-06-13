package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Sound {
	
	static SoundPool sp=new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	
	static boolean soundOn=true;
	
	int soundID=-1;
	//MediaPlayer sound;
	static Context context;
	
	public Sound () {
		
	}
	
	static public void setContext(Context _context) {
		context=_context;
	}
	
	boolean create(int resourceID) {
		soundID=sp.load(context, resourceID, 1);
		
		return true;
	}
	
	void start() {
		if (soundOn)
			sp.play(soundID, 1.0f, 1.0f, 1, 0, 1.0f);
	}
}

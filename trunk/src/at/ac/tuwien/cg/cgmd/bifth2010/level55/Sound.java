package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import java.io.IOException;
import android.content.Context;
import android.media.MediaPlayer;

public class Sound {
	public Sound () {
		
	}
	
	static public void setContext(Context _context) {
		context=_context;
	}
	
	boolean create(int resourceID) {
		sound=MediaPlayer.create(context, resourceID);
		
		if (sound!=null) {
			return true;
		} else {
			return false;
		}
	}
	
	void start() {
		try {
			sound.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sound.start();
	}
	
	void pause() {
		sound.pause();
	}
	
	void stop() {
		sound.stop();
	}
	
	MediaPlayer sound;
	static Context context;
}

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
			try {
				sound.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}
	
	void start() {
		/*try {
			sound.prepareAsync();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}*/
		/*if (sound.isPlaying()) {
			sound.seekTo(0);
		} else {
			sound.start();
		}*/
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

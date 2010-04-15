package at.ac.tuwien.cg.cgmd.bifth2010.level66;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.MediaPlayer;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Sound {
	private static SoundPool sounds;
	private static int aviator;
	private static MediaPlayer music;
	private static boolean sound = false;
	
	public static void loadSound(Context context) {
	    //sound = SilhouPreferences.sound(context); // should there be sound?
	    sounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	    
	    aviator = sounds.load(context, R.raw.l66_starting, 1);
	    
	    //music = MediaPlayer.create(context, R.raw.silhouette2);
	}
	
	public static void playSelect() {
	    if (!sound) return; // if sound is turned off no need to continue
	    sounds.play(aviator, 1, 1, 1, 0, 1);
	}
	
	public static final void playMusic() {
	    if (!sound) return;
	    if (!music.isPlaying()) {
	    music.seekTo(0);
	    music.start();
	    }
	}
	
	public static final void pauseMusic() {
	    if (!sound) return;
	    if (music.isPlaying()) music.pause();
	}
	
	public static final void release() {
	    if (!sound) return;
	    sounds.release();
	    music.stop();
	    music.release();
	}
}
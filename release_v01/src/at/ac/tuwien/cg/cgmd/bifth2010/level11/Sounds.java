package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import android.util.Log;

/**
 * manages all in game sounds. 
 * @author g11
 */
public class Sounds {
    private static final String LOG_TAG = Sounds.class.getSimpleName();
	public static Sounds singleton;
	public static java.util.HashMap <Integer, SoundFile> soundMap; 

	private SoundFile[] soundFiles;
	private GameAudio gameAudio;
	private SoundFile soundFile;
	private AudioStream audioStream;

	public Sounds() {
		Sounds.singleton = this;
		Sounds.soundMap = new java.util.HashMap<Integer, SoundFile> ();
		gameAudio = new GameAudio();
		if (!GameActivity.singleton.isMusicAndSoundOn)
			gameAudio.mute();
	}
	
	public void add(int id) {
		//Log.i(LOG_TAG, "add()");
		
		soundFile = gameAudio.createSoundFile(id);
		
		if(soundFiles==null) {
			soundFiles = new SoundFile[1];
			soundFiles[0]=soundFile;
			
		} else {
			SoundFile[] newarray = new SoundFile[soundFiles.length+1];
			
			for(int i=0;i<soundFiles.length;i++) {
				newarray[i]=soundFiles[i];
			}
			
			newarray[soundFiles.length]=soundFile;
			soundFiles = newarray;
			
			
		}
		
		Sounds.soundMap.put(id,soundFile);
	}
	
	public void play(int id) {
			audioStream = Sounds.soundMap.get(id).play();
			if (audioStream != null)
				audioStream.setRate(1.0f);
	}
}
package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import android.content.Intent;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.Constants;

/**
 * The SessionState is a convenience class that can be used to easy correct communication between levels and the framework.  
 * @author Peter
 *
 */
public class SessionState {
	
	private static final String EXTRA_SESSIONSTATE_MUSIC = "music";
	private static final String EXTRA_SESSIONSTATE_PROGRESS = "progress";
	private static final String EXTRA_SESSIONSTATE_LEVEL = "level";
	
	
	private Bundle mBundle = null;

	/**
	 * Constructor using a bundle
	 * @param b the bundle
	 */
	public SessionState(Bundle b) {
		mBundle  = b;
	}

	/**
	 * Constructor (empty)
	 */
	public SessionState() {
	}

	/**
	 * Describes the state that is stored in this SessionState object. 
	 * 
	 * @return True if the sound and music is on. False otherwise. If the variable is not set, it returns false.
	 */
	public boolean isMusicAndSoundOn(){
		boolean bIsOn = false;
		if((mBundle!=null)&&(mBundle.containsKey(EXTRA_SESSIONSTATE_MUSIC))){
			bIsOn = mBundle.getBoolean(EXTRA_SESSIONSTATE_MUSIC, false);
		}
		return bIsOn;
	}

	/**
	 * Set the state that is stored in this SessionState object. 
	 * 
	 * @param bIsOn Set the sound/music on or off
	 */
	public void setMusicAndSoundOn(boolean bIsOn){
		if (mBundle==null) {
			mBundle = new Bundle();
		}
		mBundle.putBoolean(EXTRA_SESSIONSTATE_MUSIC, bIsOn);
	}

	/**
	 * Describes the state that is stored in this SessionState object. 
	 * 
	 * @return Returns the progress the user made. Progress is always between 0 and 100. It describes the progress in percent from the previous to the next level. Returns 0 if the variable is not set. 
	 */
	public int getProgress(){
		int iProgress = 0;
		if((mBundle!=null)&&(mBundle.containsKey(EXTRA_SESSIONSTATE_PROGRESS))){
			iProgress = mBundle.getInt(EXTRA_SESSIONSTATE_PROGRESS, 0);
		}
		return iProgress;
	}

	/**
	 * Set the state that is stored in this SessionState object. 
	 * 
	 * @param iProgress Set the progress made
	 */
	public void setProgress(int iProgress){
		if (mBundle==null) {
			mBundle = new Bundle();
		}
		iProgress = Math.max(0, Math.min(iProgress,100));
		mBundle.putInt(EXTRA_SESSIONSTATE_PROGRESS, iProgress);
	}


	/**
	 * Describes the state that is stored in this SessionState object. 
	 * 
	 * @return Returns the level that the user chose.  
	 */
	public int getLevel(){
		int iLevel = 0;
		if((mBundle!=null)&&(mBundle.containsKey(EXTRA_SESSIONSTATE_LEVEL))){
			iLevel = mBundle.getInt(EXTRA_SESSIONSTATE_LEVEL, 0);
		}
		return iLevel;
	}

	/**
	 * Set the state that is stored in this SessionState object. 
	 * 
	 * @param iLevel Set the level number that was chosen by the user (always 1-6).
	 */
	public void setLevel(int iLevel){
		if (mBundle==null) {
			mBundle = new Bundle();
		}
		iLevel = Math.max(0, Math.min(iLevel, Constants.NUMBER_OF_LEVELS_TO_PLAY));
		mBundle.putInt(EXTRA_SESSIONSTATE_LEVEL, iLevel);
	}

	/**
	 * Returns the Session state as a bundle.
	 * @return The bundle representing this SessionState object.
	 * 
	 */
	public Bundle asBundle() {
		return mBundle;
	}

	/**
	 * Returns the Session state as a Intent.
	 * @return The intent representing this SessionState object.
	 * 
	 */
	public Intent asIntent() {
		Intent data = new Intent();
		data.putExtras(this.asBundle());
		return data;
	}

	
}

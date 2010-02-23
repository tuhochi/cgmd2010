package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import android.content.Intent;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.Constants;

public class SessionState {
	
	private static final String EXTRA_SESSIONSTATE_MUSIC = "music";
	private static final String EXTRA_SESSIONSTATE_PROGRESS = "progress";
	private static final String EXTRA_SESSIONSTATE_LEVEL = "level";
	
	
	private Bundle mBundle = null;

	public SessionState(Bundle b) {
		mBundle  = b;
	}
	
	public SessionState() {

	}

	public boolean isMusicAndSoundOn(){
		boolean bIsOn = false;
		if((mBundle!=null)&&(mBundle.containsKey(EXTRA_SESSIONSTATE_MUSIC))){
			bIsOn = mBundle.getBoolean(EXTRA_SESSIONSTATE_MUSIC, false);
		}
		return bIsOn;
	}
	
	public void setMusicAndSoundOn(boolean bIsOn){
		if (mBundle==null) {
			mBundle = new Bundle();
		}
		mBundle.putBoolean(EXTRA_SESSIONSTATE_MUSIC, bIsOn);
	}
	
	public int getProgress(){
		int iProgress = 0;
		if((mBundle!=null)&&(mBundle.containsKey(EXTRA_SESSIONSTATE_PROGRESS))){
			iProgress = mBundle.getInt(EXTRA_SESSIONSTATE_PROGRESS, 0);
		}
		return iProgress;
	}
	
	public void setProgress(int iProgress){
		if (mBundle==null) {
			mBundle = new Bundle();
		}
		iProgress = Math.max(0, Math.min(iProgress,100));
		mBundle.putInt(EXTRA_SESSIONSTATE_PROGRESS, iProgress);
	}

	public int getLevel(){
		int iLevel = 0;
		if((mBundle!=null)&&(mBundle.containsKey(EXTRA_SESSIONSTATE_LEVEL))){
			iLevel = mBundle.getInt(EXTRA_SESSIONSTATE_LEVEL, 0);
		}
		return iLevel;
	}
	
	public void setLevel(int iLevel){
		if (mBundle==null) {
			mBundle = new Bundle();
		}
		iLevel = Math.max(0, Math.min(iLevel, Constants.NUMBER_OF_LEVELS_TO_PLAY));
		mBundle.putInt(EXTRA_SESSIONSTATE_LEVEL, iLevel);
	}

	public Bundle asBundle() {
		return mBundle;
	}

	
}

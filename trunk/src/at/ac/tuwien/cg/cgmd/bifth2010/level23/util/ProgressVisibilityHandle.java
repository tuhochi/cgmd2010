package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.io.Serializable;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;

public class ProgressVisibilityHandle implements Runnable,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int visibility;
	@Override
    public void run() {
        LevelActivity.instance.boostProgress.setVisibility(visibility);
    }
};

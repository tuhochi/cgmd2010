package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;

public class ProgressVisibilityHandle implements Runnable{
	public int visibility;
	@Override
    public void run() {
        LevelActivity.instance.boostProgress.setVisibility(visibility);
    }
};

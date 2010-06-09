package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.io.Serializable;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;

public class ProgressVisibilityHandle implements Runnable,Serializable{
	/**
	 * This class implements Runnable and Serializable and sets the visibility of the boostProgress
	 * @author Markus Ernst
	 * @author Florian Felberbauer
	 */
	private static final long serialVersionUID = 1L;
	public int visibility;
	
	/**
	 * Overrides the run method and sets the visibility of the boostProgress 
	 */
	@Override
    public void run() {
        LevelActivity.instance.boostProgress.setVisibility(visibility);
    }
};

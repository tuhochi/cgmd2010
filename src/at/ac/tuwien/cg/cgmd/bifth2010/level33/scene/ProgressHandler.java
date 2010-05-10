package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class ProgressHandler extends SessionState{
	
	private int stoneGoldDecrease = 3;
	private int barrelGoldDecrease = 8;
	private int thrashGoldIncrease = 5;
	private int springGoldDecrease = 1;
	
	private int actuallyProgress = 0;
	
	public static boolean isLevelCompleted = false;
	
	public ProgressHandler(){
		this.actuallyProgress = this.getProgress();
	}
	
	
	public void collectStone(){
		actuallyProgress = actuallyProgress + stoneGoldDecrease;
		checkNewProgress();
	}
	
	public void collectBarrel(){
		actuallyProgress = actuallyProgress + barrelGoldDecrease;
		checkNewProgress();
	}
	
	public void collectTrash(){
		actuallyProgress = actuallyProgress - thrashGoldIncrease;
		checkNewProgress();
	}
	
	public void collectSpring(){
		actuallyProgress = actuallyProgress + springGoldDecrease;
		checkNewProgress();
	}
	
	private void checkNewProgress(){
		if(actuallyProgress>=100)
		{
			actuallyProgress=100;
			isLevelCompleted=true;
		}
		if(actuallyProgress<0)
			actuallyProgress=0;
	}
	
	public int getActualllyProgress(){
		return actuallyProgress;
	}
	

}

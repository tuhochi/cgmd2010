package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * 
 * The progressHandler handles the hole progress. Every time an item will be picked up the amount
 * of gold will be changed. The value is only between 0 and 100. If the new amount would be below 0 or 
 * more than 100, the amount will be set to 0 or 100.
 *
 */
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
	
	/**
	 * A stone was picked up. You loose some gold, your gold amount will be decreased
	 */
	public void collectStone(){
		actuallyProgress = actuallyProgress + stoneGoldDecrease;
		checkNewProgress();
	}
	
	/**
	 * A barrel was picked up. You loose some gold, your gold amount will be decreased
	 */
	public void collectBarrel(){
		actuallyProgress = actuallyProgress + barrelGoldDecrease;
		checkNewProgress();
	}
	
	/**
	 * A trash was picked up. You get some gold, your gold amount will be increased
	 */
	public void collectTrash(){
		actuallyProgress = actuallyProgress - thrashGoldIncrease;
		checkNewProgress();
	}
	
	/**
	 * A spring was picked up. You loose a few gold, your gold amount will be decreased
	 */
	public void collectSpring(){
		actuallyProgress = actuallyProgress + springGoldDecrease;
		checkNewProgress();
	}
	
	/**
	 * Checks if the new amount of gold is between 0 and 100. If it isn't it will be set.
	 * Also the level will finish if 100% was reached
	 */
	private void checkNewProgress(){
		if(actuallyProgress>=100)
		{
			actuallyProgress=100;
			isLevelCompleted=true;
		}
		if(actuallyProgress<0)
			actuallyProgress=0;
	}
	
	/**
	 * Returns the actually amount of gold. This is necessary for the progressBar
	 * @return actuallyProgress			
	 */
	public int getActualllyProgress(){
		return actuallyProgress;
	}
	

}
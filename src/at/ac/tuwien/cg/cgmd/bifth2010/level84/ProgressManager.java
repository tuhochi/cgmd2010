package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.app.Activity;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
/**
 * main class for handling statistics and results
 * @author Gerald, Georg
 */

public class ProgressManager extends SessionState {

	/** remaining money **/
	private int remainingValue;
	/** total (start-) money **/
	private int totalValue;
	/** our activity **/
	private Activity activity;
	
	/** array for storing statistics of hits per gem type **/
	private int[] gemsHit = new int[4];
	/** array for storing statistics of breaks per gem type **/
	private int[] gemsBreak = new int[4];
	
	/**
	 * create a new ProgressManager
	 * @param activity
	 */
	public ProgressManager(Activity activity) {
		this.activity = activity;
	}
	
	/**
	 * set the total (start-) amount of money
	 * @param moneyToSpend
	 */
	public void setMaxMoney(int moneyToSpend)
	{
		totalValue = moneyToSpend;
		remainingValue = moneyToSpend;
	}
	
	/**
	 * get the remaining money
	 * @return remaining money
	 */
	public int getRemainingValue()
	{
		return remainingValue;
	}

	/**
	 * get the start-money-amount
	 * @return total (start-) money
	 */
	public int getStartValue()
	{
		return totalValue;
	}
	
	/**
	 * lose money if gem hits a drain (amount of money dependent on the draintype)
	 * @param drainType
	 */
	public void loseMoneyByHit(int drainType) {
		remainingValue -= drainType * LevelActivity.GEM_BASE_VALUE;
		updateGemStatsByHit(drainType);
//		if (remainingValue <= 0) {
//			remainingValue = 0;
//			this.activity.finish();
//		}
	}
	
	/**
	 * lose money if gem breaks (amount of money dependent on the draintype)
	 * @param drainType
	 */
	public void loseMoneyByBreak(int drainType)
	{
		remainingValue -= drainType * LevelActivity.GEM_BASE_VALUE / 10;
		updateGemStatsByBreak(drainType);
//		if (remainingValue <= 0) {
//			remainingValue = 0;
//			this.activity.finish();
//		}
	}
	
	/**
	 * increase the hit-statistic of the drainType 
	 * @param drainType
	 */
	private void updateGemStatsByHit(int drainType)
	{
		gemsHit[drainType-1]++;
	}
	
	/**
	 * increase the break-statistic of the drainType 
	 * @param drainType
	 */
	private void updateGemStatsByBreak(int drainType)
	{
		gemsBreak[drainType-1]++;
	}
	
	/**
	 * get the hit-statistic of the drainType 
	 * @param drainType
	 * @return
	 */
	public int getGemStatsHit(int drainType)
	{
		return gemsHit[drainType-1];
	}
	
	/**
	 * get the break-statistic of the drainType 
	 * @param drainType
	 * @return
	 */
	public int getGemStatsBreak(int drainType)
	{
		return gemsBreak[drainType-1];
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState#getProgress()
	 */
	public int getProgress()
	{
		return (int)(100f - (float)remainingValue/(float)totalValue * 100f);
	}
}

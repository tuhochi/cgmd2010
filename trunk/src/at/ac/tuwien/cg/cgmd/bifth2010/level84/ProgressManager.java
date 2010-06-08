package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.app.Activity;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class ProgressManager extends SessionState {

	private int remainingValue;
	private int totalValue;
	private Activity activity;
	
	private int[] gemsHit = new int[3];
	private int[] gemsMiss = new int[3];
	
	public ProgressManager(Activity activity) {
		this.activity = activity;
	}
	
	public void setMaxMoney(int moneyToSpend)
	{
		totalValue = moneyToSpend;
		remainingValue = moneyToSpend;
	}
	
	public int getRemainingValue()
	{
		return remainingValue;
	}
	
	public int getStartValue()
	{
		return totalValue;
	}
	
	/**
	 * define how much money is lost dependent on the draintype
	 * @param drainType
	 */
	public void loseMoneyByHit(int drainType) {
		remainingValue -= drainType * LevelActivity.GEM_BASE_VALUE;
		updateGemStatsByHit(drainType);
		if (remainingValue <= 0) {
			remainingValue = 0;
			this.activity.finish();
		}
	}
	
	
	/**
	 * define how much money is lost dependent on the draintype if the gem breaks
	 * @param drainType
	 */
	public void loseMoneyByBreak(int drainType)
	{
		remainingValue -= drainType * LevelActivity.GEM_BASE_VALUE / 10;
		updateGemStatsByMiss(drainType);
		if (remainingValue <= 0) {
			remainingValue = 0;
			this.activity.finish();
		}
	}
	
	private void updateGemStatsByHit(int drainType)
	{
		gemsHit[drainType]++;
	}
	
	private void updateGemStatsByMiss(int drainType)
	{
		gemsMiss[drainType]++;
	}
	
	public int getGemStatsHit(int drainType)
	{
		return gemsHit[drainType-1];
	}
	
	public int getGemStatsMiss(int drainType)
	{
		return gemsMiss[drainType-1];
	}
	
	public int getProgress()
	{
		return (int)(100f - (float)remainingValue/(float)totalValue * 100f);
	}
}

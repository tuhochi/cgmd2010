package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.app.Activity;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class ProgressManager extends SessionState {

	private int remainingValue;
	private int totalValue;
	private Activity activity;
	
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
		if (remainingValue <= 0) {
			remainingValue = 0;
			this.activity.finish();
		}
	}
	
	public int getProgress()
	{
		return (int)(100f - (float)remainingValue/(float)totalValue * 100f);
	}
}

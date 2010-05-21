package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class ProgressManager extends SessionState {

	private int progress = 0; // must be between 0-100
	private int maxMoney = 0;
	private int actualMoney = 0;
	private int moneyProgress = 0;
	
	/** values if gems hit a drain **/
	private final int drainClosedHit = 0;
	private final int drainRoundHit = 1000;
	private final int drainDiamondHit = 4000;
	private final int drainRectHit = 3000;
	private final int drainOctHit = 2000;
	
	/** values if gems break **/
	private final int drainClosedBreak = 200;
	private final int drainRoundBreak = drainRoundHit / 20;
	private final int drainDiamondBreak = drainDiamondHit / 20;
	private final int drainRectBreak = drainRectHit / 20;
	private final int drainOctBreak = drainOctHit / 20;
	
	// the more difficult the figure of the gem is ...
	// ... the more points you can lose ?!
	
//	public void addMoney(int drainType)
//	{
//		int value = 0;
//		gamepoints += value;
//	}
	
	public void setMaxMoney(int moneyToSpend)
	{
		this.maxMoney = moneyToSpend;
		actualMoney = moneyToSpend;
	}
	
	public int getActualMoney()
	{
		return actualMoney;
	}
	
	/**
	 * define how much money is lost dependent on the draintype
	 * @param drainType
	 */
	public void loseMoneyByHit(int drainType) {
		int value = 0;
		switch (drainType) {
			case ModelDrain.CLOSED: value = drainClosedHit; break;
			case ModelDrain.ROUND: value = drainRoundHit; break;
			case ModelDrain.DIAMOND: value = drainDiamondHit; break;
			case ModelDrain.RECT: value = drainRectHit; break;
			case ModelDrain.OCT: value = drainOctHit; break;
		}
		
		actualMoney -= value;
		if (actualMoney < 0)
			actualMoney = 0;
	}
	
	
	/**
	 * define how much money is lost dependent on the draintype if the gem breaks
	 * @param drainType
	 */
	public void loseMoneyByBreak(int drainType)
	{
		//TODO: random if gem breaks or not
		int value = 0;
		switch (drainType) {
			case ModelDrain.CLOSED: value = drainClosedBreak; break;
			case ModelDrain.ROUND: value = drainRoundBreak; break;
			case ModelDrain.DIAMOND: value = drainDiamondBreak; break;
			case ModelDrain.RECT: value = drainRectBreak; break;
			case ModelDrain.OCT: value = drainOctBreak; break;
		}
		
		actualMoney -= value;
		if (actualMoney < 0) 
			actualMoney = 0;
	}
	
	public int getMoneyProgress()
	{
		int moneyStep = this.maxMoney / 5;
		
		if (actualMoney > (moneyStep * 5)){	moneyProgress = 10;}
		if (actualMoney > (moneyStep * 4)){	moneyProgress = 20;}
		if (actualMoney > (moneyStep * 3)){	moneyProgress = 30;}
		if (actualMoney > (moneyStep * 2)){	moneyProgress = 40;}
		if (actualMoney == 0){	moneyProgress = 50;}
		//Log.i("PROGRESS","-> " + moneyProgress);

		return moneyProgress;
	}
	
	public void updatePointProgress(int actualprogress)
	{
		progress = actualprogress;
	}
	
	public int getPointProgress()
	{
		//TODO: formel für fortschritt
		
		return progress;
	}
	
}

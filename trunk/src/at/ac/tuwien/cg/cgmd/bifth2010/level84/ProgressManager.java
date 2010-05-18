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
	private final int drainRoundHit = 10000;
	private final int drainDiamondHit = 40000;
	private final int drainRectHit = 30000;
	private final int drainOctHit = 20000;
	
	/** values if gems break **/
	private final int drainClosedBreak = 25000;
	private final int drainRoundBreak = drainRoundHit / 2;
	private final int drainDiamondBreak = drainDiamondHit / 2;
	private final int drainRectBreak = drainRectHit / 2;
	private final int drainOctBreak = drainOctHit / 2;
	
	
	/**
	 * drainTypes
	 * 0 ... closed
	 * 1 ... round
	 * 2 ... diamond
	 * 3 ... rect
	 * 4 ... oct
	 */
	
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
	public void loseMoneyByHit(int drainType)
	{
		int value = 0;
		switch (drainType)
		{
			case 0: value = drainClosedHit;
			case 1: value = drainRoundHit;
			case 2: value = drainDiamondHit;
			case 3: value = drainRectHit;
			case 4: value = drainOctHit;
		}
		
		actualMoney -= value;
		if (actualMoney < 0) { actualMoney = 0; }
	}
	
	
	/**
	 * define how much money is lost dependent on the draintype if the gem breaks
	 * @param drainType
	 */
	public void loseMoneyByBreak(int drainType)
	{
		//TODO: random if gem breaks or not
		int value = 0;
		switch (drainType)
		{
			case 0: value = drainClosedBreak;
			case 1: value = drainRoundBreak;
			case 2: value = drainDiamondBreak;
			case 3: value = drainRectBreak;
			case 4: value = drainOctBreak;
		}
		
		actualMoney -= value;
		if (actualMoney < 0) { actualMoney = 0; }
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

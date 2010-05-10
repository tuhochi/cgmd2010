package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class ProgressManager extends SessionState {

	private int progress = 0; // must be between 0-100
	private int moneyToSpend = 0;
	private int actualMoney = 0;
	
	
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
		this.moneyToSpend = moneyToSpend;
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
	public void loseMoney_by_hit(int drainType)
	{
		int value = 0;
		switch (drainType)
		{
		case 0: value = 0;
		case 1: value = 1000;
		case 2: value = 4000;
		case 3: value = 2000;
		case 4: value = 3000;
		}
		
		actualMoney -= value;
	}
	
	/**
	 * define how much money is lost dependent on the draintype if the gem breaks
	 * @param drainType
	 */
	public void loseMoney_by_break(int drainType)
	{
		int value = 0;
		switch (drainType)
		{
			case 0: value = 5000 / 2;
			case 1: value = 1000 / 2;
			case 2: value = 3000 / 2;
			case 3: value = 2000 / 2;
			case 4: value = 4000 / 2;
		}
		
		actualMoney -= value;
	}
	
	public void setProgress(int actualprogress)
	{
		progress = actualprogress;
	}
	
	public int getProgress()
	{
		//TODO: formel für fortschritt
		return progress;
	}
	
}

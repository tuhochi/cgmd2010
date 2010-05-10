package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class ProgressManager extends SessionState {

	private int progress = 0; // must be between 0-100
	private int maxMoney = 0;
	private int actualMoney = 0;
	
	/** values if gems hit a drain **/
	private int drain_closed_hit = 0;
	private int drain_round_hit = 1000;
	private int drain_diamond_hit = 4000;
	private int drain_rect_hit = 2000;
	private int drain_oct_hit = 3000;
	
	/** values if gems break **/
	private int drain_closed_break = 5000;
	private int drain_round_break = drain_round_hit / 2;
	private int drain_diamond_break = drain_diamond_hit / 2;
	private int drain_rect_break = drain_rect_hit / 2;
	private int drain_oct_break = drain_oct_hit / 2;
	
	
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
	public void loseMoney_by_hit(int drainType)
	{
		int value = 0;
		switch (drainType)
		{
		case 0: value = drain_closed_hit;
		case 1: value = drain_round_hit;
		case 2: value = drain_diamond_hit;
		case 3: value = drain_rect_hit;
		case 4: value = drain_oct_hit;
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
			case 0: value = drain_closed_break;
			case 1: value = drain_round_break;
			case 2: value = drain_diamond_break;
			case 3: value = drain_rect_break;
			case 4: value = drain_oct_break;
		}
		
		actualMoney -= value;
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

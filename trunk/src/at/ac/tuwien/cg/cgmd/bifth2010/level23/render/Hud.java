package at.ac.tuwien.cg.cgmd.bifth2010.level23.render;

import java.util.TimerTask;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.Button;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TimeUtil;

public class Hud 
{
	private Button goldButton;
	private Button moneyButton;
	
	private TimerTask burnTask;
	
	private class BurnTimer extends TimerTask
	{

		@Override
		public void run() 
		{
			Settings.BALLOON_SPEED -= Settings.BURN_BOOST;
			moneyButton.setActive(true);
		}
		
	}
	
	public Hud()
	{
		float topBounds = RenderView.getInstance().getTopBounds();
		float rightBounds = RenderView.getInstance().getRightBounds();
		goldButton = new Button(10, 10, new Vector2(0,topBounds-10));
		moneyButton = new Button(10, 10, new Vector2(rightBounds-10,topBounds-10));
		burnTask = new BurnTimer();
	}
	
	public boolean testPressed(float x, float y)
	{
		if(goldButton.isPressed(x, y))
		{
			Settings.BALLOON_SPEED += Settings.GOLD_BOOST;
			return true;
		}
			
		if(moneyButton.isPressed(x, y) && moneyButton.isActive())
		{
			moneyButton.setActive(false);
			Settings.BALLOON_SPEED += Settings.BURN_BOOST;
			TimeUtil.getInstance().scheduleTimer(burnTask, Settings.BURN_BOOST_TIME);
			return true;
		}
		
		return false;			
	}
	
	public void render()
	{
		goldButton.render();
		moneyButton.render();
	}
}

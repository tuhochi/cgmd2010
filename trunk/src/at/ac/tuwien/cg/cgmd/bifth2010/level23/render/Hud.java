package at.ac.tuwien.cg.cgmd.bifth2010.level23.render;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.Button;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.BurnTimer;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.SoundManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TimeUtil;

/**
 * The Class Hud provides the HUD for activating boni and movement arrows
 *
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class Hud 
{
	public static Hud instance;
	
	/** The gold bar button */
	private Button goldButton;
	
	/** The money button. */
	private Button moneyButton;
	
	/** The class handling time utilities */
	private TimeUtil timeUtil; 
	
	private BurnTimer burnTimer = new BurnTimer();

	private boolean wasRestored=false;
	
	/**
	 * Instantiates a new hud, including the buttons and the timer for boost operation.
	 */
	public Hud()
	{
		init();
		instance=this;
	}
	
	private void init()
	{
		float topBounds = RenderView.getInstance().getTopBounds();
		float rightBounds = RenderView.getInstance().getRightBounds();
		goldButton = new Button(10, 10, new Vector2(0,topBounds-10));
		moneyButton = new Button(10, 10, new Vector2(rightBounds-10,topBounds-10));
		moneyButton.preprocess();
		goldButton.preprocess();
		timeUtil = TimeUtil.getInstance();
	}
	
	public void readFromStream(DataInputStream dis) throws IOException
	{
		moneyButton.setActive(dis.readBoolean());
	}
	
	public void writeFromStream(DataOutputStream dos) throws IOException
	{
		dos.writeBoolean(moneyButton.isActive());
		wasRestored=true;
	}
	
	/**
	 * Checks if the money button is active
	 * @return true if active, else otherwise
	 */
	public boolean isMoneyButtonActive() {
		return moneyButton.isActive();
	}
	
	/**
	 * Sets the active state of the money button 
	 * @param b true if button should be active, false otherwise
	 */
	public void setMoneyButtonActive(boolean b) {
		moneyButton.setActive(b); 
	}
	/**
	 * Tests if pressed
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return true, if pressed, false otherwise
	 */
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
			timeUtil.scheduleTimer(burnTimer);
			SoundManager.instance.startBoostSound();
			return true;
		}
		
		return false;			
	}
	
	/**
	 * Renders the HUD 
	 */
	public void render()
	{
		goldButton.render();
		moneyButton.render();
	}
	
	public void preprocess()
	{
		moneyButton.preprocess();
		goldButton.preprocess();
	}
	
	public void reset()
	{
		if(!wasRestored)
			init();
		else
			wasRestored=false;
	}
}

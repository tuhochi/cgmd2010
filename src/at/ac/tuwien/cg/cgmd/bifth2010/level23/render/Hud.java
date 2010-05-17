package at.ac.tuwien.cg.cgmd.bifth2010.level23.render;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.ProgressBar;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
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
	
	private BurnTimer burnTimer;

	private boolean wasRestored=false;
	
	private int boostAudioId;
	
	private ProgressBar progressBar;
	
	public float burnProgress = 1;
	
	/**
	 * Instantiates a new hud, including the buttons and the timer for boost operation.
	 */
	public Hud()
	{
		init();
		boostAudioId = SoundManager.instance.requestPlayer(R.raw.l23_boost_neu,true);
		burnTimer = new BurnTimer(boostAudioId);
		instance=this;
	}
	
	/**
	 * Initializes the HUD
	 */
	private void init()
	{
		float topBounds = RenderView.instance.getTopBounds();
		float rightBounds = RenderView.instance.getRightBounds();
		float aspectRatio = RenderView.instance.getAspectRatio();
		goldButton = new Button(15, 15, new Vector2(5,(topBounds-15)/2));
		moneyButton = new Button(15, 15, new Vector2(rightBounds-20,(topBounds-15)/2));
		progressBar = new ProgressBar();
		moneyButton.preprocess();
		goldButton.preprocess();
		progressBar.preprocess();
		timeUtil = TimeUtil.instance;
	}
	
	/**
	 * Reading from stream
	 * @param dis Stream to read from
	 */
	public void readFromStream(DataInputStream dis) throws IOException
	{
		moneyButton.setActive(dis.readBoolean());
	}
	
	/**
	 * Writing to stream 
	 * @param dos Stream to write to
	 */
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
			SoundManager.instance.startPlayer(boostAudioId);
			return true;
		}
		
		return false;			
	}
	
	/**
	 * Renders the HUD 
	 */
	public void render()
	{
		if(!moneyButton.isActive())
			progressBar.render(burnProgress);
		goldButton.render();
		moneyButton.render();
	}
	
	
	/**
	 * Loads the vertex buffers for buttons
	 */
	public void preprocess()
	{
		moneyButton.preprocess();
		goldButton.preprocess();
	}
	
	/**
	 * Resets the HUD
	 */
	public void reset()
	{
		if(!wasRestored)
			init();
		else
			wasRestored=false;
	}
}

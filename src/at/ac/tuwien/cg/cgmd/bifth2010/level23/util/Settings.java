package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;

/**
 * The Class Settings centralizes storage of setting variables
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class Settings 
{
	/** The constant for balloon speed. */
	public static int OBSTACLE_START = 300;
	
	/** The constant for balloon speed. */
	public static float BALLOON_STARTSPEED = 0.00025f*RenderView.instance.getTopBounds();
	
	/** The constant for balloon speed. */
	public static float BALLOON_SPEED = BALLOON_STARTSPEED;
	//units per millisecond
	/** The Constant MOVE_SPEED. */
	public static final float MOVE_SPEED = 0.15f;
	
	/** The boolean if GLES11 is supported. */
	public static boolean GLES11Supported = false;
	
	//item effects
	/** The Constant BURN_BOOST. */
	public static final float BURN_BOOST = 1.75f;
	//time in milliseconds
	/** The Constant BURN_BOOST_TIME. */
	public static final long BURN_BOOST_TIME = 2500;
	
	/** The Constant GOLD_BOOST_TIME. */
	public static final long GOLD_BOOST_TIME = 5000;
	
	/** The Constant GOLD_BOOST. */
	public static final float GOLD_BOOST = 1.2f;

	/** The MainChar width. (will be multiplied by aspect ratio) */
	public static final float MAINCHAR_WIDTH = 25f;
	/** The MainChar height. */
	public static final float MAINCHAR_HEIGHT = 25f;
	
	/** The MainChar start position x-coordinate. */
	public static final float MAINCHAR_STARTPOSX = 50 - MAINCHAR_WIDTH/2f;
	/** The MainChar start position y-coordinate. */
	public static final float MAINCHAR_STARTPOSY = 3f;
	
	/** Will be multiplied with balloonHeight for Score */
	public static final float SCOREHEIGHT_MODIFIER = 0.02f;
	
	public static final int MAX_HEIGHT = 4000;
	
	public static boolean SOUND_ENABLED;
}

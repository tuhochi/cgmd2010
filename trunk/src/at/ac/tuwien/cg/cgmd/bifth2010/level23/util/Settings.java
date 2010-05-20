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
	public static float BALLOON_STARTSPEED = 0.0005f*RenderView.instance.getTopBounds();
	
	/** The constant for balloon speed. */
	public static float BALLOON_SPEED = BALLOON_STARTSPEED;
	//units per millisecond
	/** The Constant MOVE_SPEED. */
	public static final float MOVE_SPEED = 0.125f;
	
	/** The boolean if GLES11 is supported. */
	public static boolean GLES11Supported = false;
	
	//item effects
	/** The Constant BURN_BOOST. */
	public static final float BURN_BOOST = 0.0005f*RenderView.instance.getTopBounds();
	//time in milliseconds
	/** The Constant BURN_BOOST_TIME. */
	public static final long BURN_BOOST_TIME = 2500;
	
	/** The Constant GOLD_BOOST. */
	public static final float GOLD_BOOST = 0.01f;

	/** The MainChar width. (will be multiplied by aspect ratio) */
	public static final float MAINCHAR_WIDTH = 25f;
	/** The MainChar height. */
	public static final float MAINCHAR_HEIGHT = 25f;
	
	/** The MainChar start position x-coordinate. */
	public static final float MAINCHAR_STARTPOSX = 50 - MAINCHAR_WIDTH/2f;
	/** The MainChar start position y-coordinate. */
	public static final float MAINCHAR_STARTPOSY = 3f;
	
	/** Will be multiplied with balloonHeight for Score */
	public static final float SCOREHEIGHT_MODIFIER = 0.01f;
}

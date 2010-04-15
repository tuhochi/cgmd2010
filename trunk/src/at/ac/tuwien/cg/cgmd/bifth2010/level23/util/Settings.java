package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

/**
 * The Class Settings centralizes storage of setting variables
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class Settings 
{
	/** The constant for balloon speed. */
	public static int OBSTALE_START = 200;
	/** The constant for balloon speed. */
	public static float BALLOON_SPEED = 0.08f;
	//units per millisecond
	/** The Constant MOVE_SPEED. */
	public static final float MOVE_SPEED = 0.1f;
	
	/** The boolean if GLES11 is supported. */
	public static boolean GLES11Supported = false;
	
	//item effects
	/** The Constant BURN_BOOST. */
	public static final float BURN_BOOST = 0.1f;
	//time in milliseconds
	/** The Constant BURN_BOOST_TIME. */
	public static final long BURN_BOOST_TIME = 5000;
	
	/** The Constant GOLD_BOOST. */
	public static final float GOLD_BOOST = 0.01f;

	public static final float MAINCHAR_WIDTH = 25f;
	public static final float MAINCHAR_HEIGHT = 45f;
	
	public static final Vector2 MAINCHAR_STARTPOS = new Vector2(50 - MAINCHAR_WIDTH/2f,0);
	
	
	public static Vector2 MAINCHARPOS;
}

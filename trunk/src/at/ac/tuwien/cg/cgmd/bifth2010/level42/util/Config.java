package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;

/**
 * This Class represents the configuration for the level 42
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Config
{
	public static boolean GLES11 = false;
	public static long FPS_UPDATE_INTERVAL = 1000;
	public static long GAMETIME_UPDATE_INTERVAL = 1000;
	
	/** the total level gametime in ms */
	public static long GAMETIME = 120000;
	
	/** GAMEPLAY */

	/** the milliseconds of a long press are divided by this to get the force strength */
	public static int PRESS_TIME_TO_FORCE_DIVISOR = 15;
	
	/** the maximum milliseconds for a long press */
	public static long MAX_LONG_PRESS_TIME = 400;
	
	/** The Constant TOUCH_DEADZONE. */
	public static final int TOUCH_DEADZONE = 30;
	
	/** PHYSICS / UNIVERSE. */

	/** center position of the universe / orbits */
	public static Vector3 UNIVERSE_CENTER = new Vector3(0,0,0);
	
	/** entityname for the planet */
	public static String PLANET_NAME = "GoldPlanet";
	
	/** prefix for the satellite entities */
	public static String SATELLITE_PREFIX = "Satellite_";
	
	/** defines the maximum of the orbit main axis */
	public static float UNIVERSE_CENTERLENGTH_LIMIT = 30;
	
	/** defines the maximum of the orbit secondary axis */
	public static float UNIVERSE_DIRLENGTH_LIMIT = 30;
	
	/** defines the maximum of the orbit main axis */
	public static float FORCEFIELD_CENTERLENGTH_LIMIT = 10;
	
	/** defines the maximum of the orbit secondary axis */
	public static float FORCEFIELD_DIRLENGTH_LIMIT = 10;
	
	public static float FORCEFIELD_NEW_CENTERLENGTH = 20;
	public static float FORCEFIELD_NEW_DIRLENGTH = 20;
	
	public static float FORCEFIELD_CENTERLENGTH_SCALESPEED = 40;
	public static float FORCEFIELD_DIRLENGTH_SCALESPEED = 30;
	
	/** defines the maximum relative speed for satellites */
	public static float UNIVERSE_SPEED_LIMIT = 8;
	
	/** ratio between speed and rotation angle */
	public static float SATELLITE_SPEEDROTA_RATIO = 4f;
	
	/** ratio between speed and rotation angle */
	public static float INTERSATELLITE_SPEEDROTA_RATIO = 6f;
	
	/** the required penetration depth on collisions */
	public static float COLLISION_PENETRATION_DEPTH = 0.2f;
	
	/** minimal force to change the motion into a directional motion */
	public static float MIN_STRENGTH_FOR_DIRECTIONAL = 10f;
	
	/** minimal force to change the motion into a directional motion */
	public static float MIN_SPEED_FOR_UNDAMPED_DIRECTIONAL = 20f;
	
	public static int COUNT_NEAREST_ENTITIES = 5;
	
	/** the selection vector gets divided by this factor */
	public static float SELECTION_FORCE_FACTOR = 0.8f;
	
}
